package com.syt.aliyun.sdk;

import com.syt.aliyun.sdk.exception.LogException;
import com.syt.aliyun.sdk.kit.SLSKit;
import com.syt.aliyun.sdk.kit.ToolsKit;
import com.syt.aliyun.sdk.log.LogClient;
import com.syt.aliyun.sdk.log.entity.*;

import java.util.*;

/**
 * Hello world!
 *
 */
public class App {

	public static void main(String[] args) {
		String project = "qingplus";
		String logStore = "phone_log";
		String topic = "/analytic/test/laotnag1";
//		String tokenUrl = "http://ws.sythealth.com/ws/fit/common/getstsmessage?tokenid=6f82d35073d24770b7b7b1ba2981cbce&product=fit&flag=logo";
//		String json = HttpKit.duang().url(tokenUrl).get();
//		System.out.println(json);
		SLSToken token = new SLSToken();
		token.setAccessId("STS.CBDmaf8ZEFBfbpbXtoTeCBKxj");
		token.setAccessKeySecret("CeMPiKeURJtR4WYUHbjEgRpUT4mNBpG4sRwwLg7jtGFM");
		token.setEndpoint("http://cn-qingdao.log.aliyuncs.com");
		token.setSecurityToken("CAIS8QF1q6Ft5B2yfSjIooHxJtvS1YVk8YCNYFbTvHQ6WOpvrY7TiDz2IHFMfXdoAOwWv/kwmGBY7vwflqBcVpJeWXbDacYoL0KNXNHiMeT7oMWQweEuqv/MQBq+aXPS2MvVfJ+KLrf0ceusbFbpjzJ6xaCAGxypQ12iN+/i6/clFKN1ODO1dj1bHtxbCxJ/ocsBTxvrOO2qLwThjxi7biMqmHIl0D8kufnhmZbEsUaH0AGm8IJP+dSteKrDRtJ3IZJyX+2y2OFLbafb2EZSkUMbrP4o1PUYqWaa4Y3NUwELvg/nPPfZ6cF1Kwt0dgSqijfgThSWGoABQEufmcSfHFD+OFoptVQ606EKnVOwv49o3Q6cphJuI/mMO1i9odaoeqYiDOwU/SLtOJ6L761M2HCoLq7jm5DVgkZy4KbBWFMZJzGp9HxZh9sOD18nbHoZG7etF7NGWsZG9PKsTT8lQHF30SHiUYCr0xWhJtk9cn/Q+RNg1j/qCG0=");
		token.setProject(project);
		token.setStore(logStore);
		SLSMessage message = buildPostInfo();

		String result = SLSKit.duang().project(project).store(logStore).topic(topic).token(token).items(message).execute();
		System.out.println(result); //如果返回不是200的字符串，则有可能是token过期了
	}

	private  static SLSMessage  buildPostInfo() {

		SLSMessage slsMessage = new SLSMessage();
		slsMessage.setRequestId(java.util.UUID.randomUUID().toString());
		slsMessage.setTarget(checkValue("/analytic/test/laotnag1"));
		slsMessage.setClientIp("192.168.0.39");

		Map<String, String> header = new HashMap<String, String>();
		header.put("user-agent", "Fintess Android");
		header.put("content-type", "json");
		slsMessage.setHeader(header);

		Map<String, String> logItemMap = new HashMap<String,String>();
		logItemMap.put("userid",  "laotang");
		logItemMap.put("system", "android_888"  );
		logItemMap.put("message", "it's a test message_888");
//		String paramsString = JsonKit.toJson(logItemMap);
//		paramsString = paramsString.replace("\"", "\\\"");
		slsMessage.setBody(logItemMap);

		return slsMessage;
	}

	private static String checkValue(String value) {
		return ToolsKit.isEmpty(value) ? "" : value;
	}

	public static void main1(String[] args) {
		String keyId = "keyId";
		String keySecret = "keySecret";
		String securityToken = "securityToken";		

		// 生产环境中要改为单例的
		LogClient client = new LogClient("http://cn-qingdao.log.aliyuncs.com", keyId, keySecret, securityToken);
		for (int i = 201; i < 210; i++) {
			String project = "qingplus";
			String logStore = "phone_log";
			String topic = "pv";
			List<LogItem> logItems = new ArrayList<LogItem>();
			LogItem logItem = new LogItem((int) (new Date().getTime() / 1000));
			logItem.PushBack("userid", i + "");
			logItem.PushBack("system", "android_" + i);
			logItem.PushBack("message", "it's a test message_" + i);
			logItem.PushBack(new LogContent("my name","laotang"));
			ArrayList<LogContent> contents = new ArrayList<LogContent>();
			contents.add(new LogContent("my name","laotang"));
			contents.add(new LogContent("my name2","laotang2"));
			contents.add(new LogContent("my name3","laotang3"));
			contents.add(new LogContent("my name4","laotang4"));
			contents.add(new LogContent("my name5","laotang5"));
			logItem.SetLogContents(contents);
			logItems.add(logItem);
			PutLogsRequest request = new PutLogsRequest(project, logStore, topic, logItems);
			try {				
				RequestMessage messages = client.getRequestMessage(request);
			    //客户端自行实现的HttpClient, 必须以POST方式提交, 以AsyncHttpClient为例
				/*
				 String url = messages.getEndpoint()+messages.getResourcePath();
                 ApiHttpClient.client.removeAllHeaders();		//先删除所有header
                 Map<String,String> headers = messages.getHeaders();
                 for (Iterator it = headers.entrySet().iterator(); it.hasNext(); ) {
                     Map.Entry e = (Map.Entry) it.next();
                     ApiHttpClient.client.addHeader(e.getKey().toString(),e.getValue().toString());
                 }
                 String contentType = headers.get("Content-Type");
                 InputStreamEntity sEntity = new InputStreamEntity(messages.getContent(), messages.getContentLength());
                 ApiHttpClient.client.post(context, url, sEntity, contentType, new AsyncHttpResponseHandler() {
                     @Override
                     public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
							client.checkResponse(statusCode, new String(responseBody));//判断是否提交成功
                     }

                     @Override
                     public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                     }
                 });
                 */
			} catch (LogException e) {
				e.printStackTrace();
			}
		}
	}
}
