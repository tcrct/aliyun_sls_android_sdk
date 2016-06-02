package com.syt.aliyun.sdk.log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.zip.Deflater;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.syt.aliyun.sdk.common.Consts;
import com.syt.aliyun.sdk.enums.HttpMethod;
import com.syt.aliyun.sdk.exception.LogException;
import com.syt.aliyun.sdk.kit.Base64Kit;
import com.syt.aliyun.sdk.kit.JsonKit;
import com.syt.aliyun.sdk.kit.ToolsKit;
import com.syt.aliyun.sdk.log.entity.LogContent;
import com.syt.aliyun.sdk.log.entity.LogItem;
import com.syt.aliyun.sdk.log.entity.PutLogsRequest;
import com.syt.aliyun.sdk.log.entity.RequestMessage;

public class LogClient {
	private String httpType;
	private String hostName;
	private String accessId;
	private String accessKey;
	private String sourceIp;
	private String securityToken;
	
	/**
	 * 
	 * @param endpoint
	 * @param accessId
	 * @param accessKey
	 * @param securityToken
	 */
	public LogClient(String endpoint, String accessId, String accessKey, String securityToken) {
		this(endpoint, accessId, accessKey, null, securityToken);
	}

	/**
	 * 创建LOG客户端
	 * @param endpoint			SLS的域名地址
	 * @param accessId			访问ID
	 * @param accessKey			访问KEY
	 * @param sourceIp			本机IP
	 * @param securityToken	安全令牌
	 */
	public LogClient(String endpoint, String accessId, String accessKey, String sourceIp, String securityToken) {
		if (ToolsKit.isEmpty(endpoint))
			throw new NullPointerException("endpoint is null");
		if (ToolsKit.isEmpty(accessId))
			throw new NullPointerException("accessId is null");
		if (ToolsKit.isEmpty(accessKey))
			throw new NullPointerException("accessKey is null");
		if (ToolsKit.isEmpty(securityToken))
			throw new NullPointerException("securityToken is null");

		if (endpoint.startsWith("http://")) {
			hostName = endpoint.substring(7);
			httpType = new String("http://");
		} else if (endpoint.startsWith("https://")) {
			hostName = endpoint.substring(8);
			httpType = new String("https://");
		} else {
			hostName = endpoint;
			httpType = new String("http://");
		}

		while (hostName.endsWith("/")) {
			hostName = hostName.substring(0, hostName.length() - 1);
		}

		if (ToolsKit.isIpAddress(hostName)) {
			throw new IllegalArgumentException("EndpontInvalid", new Exception("The ip address is not supported"));
		}

		this.accessId = accessId;
		this.accessKey = accessKey;
		this.sourceIp = sourceIp;
		if (ToolsKit.isEmpty(sourceIp)) {
			this.sourceIp = ToolsKit.getLocalMachineIp();
		}
		if (ToolsKit.isEmpty(securityToken)) {
			throw new NullPointerException("securityToken is null");
		}
		this.securityToken = securityToken;
	}

	public RequestMessage getRequestMessage(PutLogsRequest request) throws LogException {
		if (ToolsKit.isEmpty(request)) throw new NullPointerException("request is null");
		
		String project = request.getProject();		
		if (ToolsKit.isEmpty(project)) throw new NullPointerException("project is null");
		
		String logStore = request.GetLogStore();
		if (ToolsKit.isEmpty(logStore)) throw new NullPointerException("logStore is null");
		
		String topic = request.GetTopic();
		if (ToolsKit.isEmpty(topic)) throw new NullPointerException("topic is null");

		String source = request.GetSource();
		source = ToolsKit.isEmpty(source) ? this.sourceIp : source;
		request.SetSource(source);
		
		List<LogItem> logItems = request.GetLogItems();		
		if (logItems.size() > Consts.CONST_MAX_PUT_LINES) {
			throw new LogException("InvalidLogSize","logItems' length exceeds maximum limitation : " +Consts.CONST_MAX_PUT_LINES + " lines", "");
		}
		
		// 初始化header
		Map<String, String> headParameter = builderHeadParams(project);
		byte[] logBytes = toByteArray(request);
		if (logBytes.length > Consts.CONST_MAX_PUT_SIZE) {
			throw new LogException("InvalidLogSize", "logItems' size exceeds maximum limitation : " + String.valueOf(Consts.CONST_MAX_PUT_SIZE)+ " bytes", "");
		}
		//压缩前的body内容大小
		headParameter.put(Consts.CONST_X_SLS_BODYRAWSIZE, String.valueOf(logBytes.length));
		// GZIP压缩
		ByteArrayOutputStream out = new ByteArrayOutputStream(logBytes.length);
		Deflater compresser = new Deflater();
		compresser.setInput(logBytes);
		compresser.finish();
		byte[] buf = new byte[10240];
		while (compresser.finished() == false) {
			int count = compresser.deflate(buf);
			out.write(buf, 0, count);
		}
		logBytes = out.toByteArray();		
		String resourceUri = "/logstores/" + logStore +"/shards/lb";
		RequestMessage message = builderRequestMessage(project, resourceUri, headParameter, logBytes);
		return message;
	}
	
	/**
	 * 构建请求头公用参数
	 * @param project			项目名称
	 * @return
	 */
	 private Map<String, String> builderHeadParams(String project) {
			HashMap<String, String> headParameter = new HashMap<String, String>();
			headParameter.put(Consts.CONST_USER_AGENT, Consts.CONST_USER_AGENT_VALUE);
			headParameter.put(Consts.CONST_DATE, ToolsKit.parseRfc822Date(new Date()));
			headParameter.put(Consts.CONST_CONTENT_TYPE, Consts.CONST_SLS_JSON);
			headParameter.put(Consts.CONST_X_SLS_COMPRESSTYPE, Consts.CONST_GZIP_ENCODING);

			if (ToolsKit.isNotEmpty(project)) {
				headParameter.put(Consts.CONST_HOST, project + "." + this.hostName);
			} else {
				headParameter.put(Consts.CONST_HOST, this.hostName);
			}

			headParameter.put(Consts.CONST_X_SLS_APIVERSION,  Consts.DEFAULT_API_VESION);
			headParameter.put(Consts.CONST_X_SLS_SIGNATUREMETHOD, Consts.HMAC_SHA1);
			if (ToolsKit.isNotEmpty(securityToken)) {
				headParameter.put(Consts.CONST_X_ACS_SECURITY_TOKEN, securityToken);
			}
			return headParameter;
		}
	 
	 /**
	  * 构建请求对象
	  * @param project					项目
	  * @param resourceUri			请求资源URI
	  * @param headers				请求头集合
	  * @param body						请求内容
	  * @return
	  * @throws LogException
	  */
	 private RequestMessage builderRequestMessage(String project, String resourceUri,  Map<String, String> headers, byte[] body) throws LogException {
			if (body.length > 0) {
				headers.put(Consts.CONST_CONTENT_MD5, MD5(body));
			}
			headers.put(Consts.CONST_CONTENT_LENGTH, String.valueOf(body.length));
			getSignature(this.accessId, this.accessKey, headers,resourceUri);
			RequestMessage request = buildRequest(getHostURI(project), resourceUri, headers,new ByteArrayInputStream(body), body.length);
			return request;
	 }
	 
	 /**
	  * 请求消息对象
	  * @param endpoint				域名
	  * @param resourceUri			请求URI
	  * @param headers				请求头
	  * @param content				请求内容(InputStream)
	  * @param size						请求内容大小
	  * @return
	  */
	 private  RequestMessage buildRequest(URI endpoint,
				String resourceUri,
				Map<String, String> headers,
				InputStream content, long size) {
			RequestMessage request = new RequestMessage();
			request.setMethod(HttpMethod.POST);
			request.setEndpoint(endpoint);
			request.setResourcePath(resourceUri);
			request.setHeaders(headers);
			request.setContent(content);
			request.setContentLength(size);
			return request;
		}
	 
	 /**
	  * 根据project确定请求域名地址
	  * @param project
	  * @return
	  */
		private URI getHostURI(String project) {
			String endPointUrl = this.httpType + this.hostName;
			if (project != null && !project.isEmpty()) {
				endPointUrl = this.httpType + project + "." + this.hostName;
			}
			try {
				return new URI(endPointUrl);
			} catch (URISyntaxException e) {
				throw new IllegalArgumentException("EndpointInvalid", e);
			}

		}

		/**
		 * MD5加密
		 * @param bytes
		 * @return
		 */
	 private String MD5(byte[] bytes) {
			try {
				MessageDigest md = MessageDigest.getInstance(Consts.CONST_MD5);
				String res = new BigInteger(1, md.digest(bytes)).toString(16).toUpperCase();
				StringBuilder zeros = new StringBuilder();
				for (int i = 0; i + res.length() < 32; i++) {
					zeros.append("0");
				}
				return zeros.toString() + res;
			} catch (NoSuchAlgorithmException e) {
				throw new RuntimeException("Not Supported signature method " + Consts.CONST_MD5, e);
			}
		}
	 
	 /**
	  * 签名，需要引入commons-codec-1.4.jar
	  * @param accessid				accessId
	  * @param accesskey 			accesskey		
	  * @param headers				request headers
	  * @param resourceUri			资源URI地址
	  * @param urlParams				get方式时的请求参数
	  */
		private void getSignature(String accessid, String accesskey, Map<String, String> headers, String resourceUri) {
			StringBuilder builder = new StringBuilder();
			builder.append(HttpMethod.POST.toString()).append(Consts.LINE_SEPARATOR);
			builder.append(ToolsKit.getMapValue(headers, Consts.CONST_CONTENT_MD5)).append(Consts.LINE_SEPARATOR); //加密方式
			builder.append(ToolsKit.getMapValue(headers, Consts.CONST_CONTENT_TYPE)).append(Consts.LINE_SEPARATOR);//加密内容类型
			builder.append(ToolsKit.getMapValue(headers, Consts.CONST_DATE)).append(Consts.LINE_SEPARATOR);//加密随机值
			builder.append(getCanonicalizedHeaders(headers)).append(Consts.LINE_SEPARATOR);
			builder.append(resourceUri);
			String signature = getSignature(accesskey, builder.toString());
			headers.put(Consts.CONST_AUTHORIZATION, Consts.CONST_HEADSIGNATURE_PREFIX + accessid + ":" + signature);
		}
		
		/**
		 * 过滤Headers里不需要进行签名验证的元素，生成要签名的字符串
		 * @param headers
		 * @return
		 */
		private String getCanonicalizedHeaders(Map<String, String> headers) {
			Map<String, String> treeMap = new TreeMap<String, String>(headers);
			StringBuilder builder = new StringBuilder();
			boolean isFirst = true;
			for (Map.Entry<String, String> entry : treeMap.entrySet()) {
				String key = entry.getKey();
				if (!key.startsWith(Consts.CONST_X_SLS_PREFIX) && !key.startsWith(Consts.CONST_X_ACS_PREFIX)) {
					continue;
				}
				if (isFirst) {
					isFirst = false;
				} else {
					builder.append(Consts.LINE_SEPARATOR);
				}
				builder.append(key).append(":").append(entry.getValue());
			}
			return builder.toString();
		}
		
		/**
		 * 取得签名字符串
		 * @param accesskey					accessKey
		 * @param data							需要生成签名的原字符串
		 * @return
		 */
		private static String getSignature(String accesskey, String data) {
			try {
				byte[] keyBytes = accesskey.getBytes(Consts.UTF_8_ENCODING);
				byte[] dataBytes = data.getBytes(Consts.UTF_8_ENCODING);
				Mac mac = Mac.getInstance(Consts.HMAC_SHA1_JAVA);
				mac.init(new SecretKeySpec(keyBytes, Consts.HMAC_SHA1_JAVA));
				return new String(Base64Kit.encode(mac.doFinal(dataBytes))); 
				
			} catch (UnsupportedEncodingException e) { 
				throw new RuntimeException("Not Supported encoding method "+ Consts.UTF_8_ENCODING, e);
			} catch (NoSuchAlgorithmException e) {
				throw new RuntimeException("Not Supported signature method " + Consts.HMAC_SHA1, e);
			} catch (InvalidKeyException e) {
				throw new RuntimeException("Failed to calcuate the signature", e);
			}
		}
		
		/**
		 * 将请求对象更改为JSONObject后，再取得byts返回
		 * @param request
		 * @return
		 */
		private byte[] toByteArray(PutLogsRequest request) {
			String topic = request.GetTopic();
			String source = request.GetSource();
			List<LogItem> logItems = request.GetLogItems();
			
			Map<String,Object> jsonObj = new HashMap<String,Object>();
			if (ToolsKit.isNotEmpty(topic)) {
				jsonObj.put("__topic__", topic);
			}
			if (ToolsKit.isNotEmpty(source)) {
				jsonObj.put("__source__",source);
			} else {
				jsonObj.put("__source__",sourceIp);
			}
			List<Map<String,Object>> logsArray = new ArrayList<Map<String,Object>>();
			for(Iterator<LogItem> it = logItems.iterator(); it.hasNext();){
				LogItem item = it.next();
				Map<String,Object> jsonObjInner = new HashMap<String,Object>();
				jsonObjInner.put("__time__", item.mLogTime);
				for (LogContent content : item.mContents) {
					jsonObjInner.put(content.mKey, content.mValue);
				}
				logsArray.add(jsonObjInner);
			}
			jsonObj.put("__logs__", logsArray);
			
			
			System.out.println(JsonKit.toJson(jsonObj));
			
			return JsonKit.toJson(jsonObj).getBytes();
		}
}
