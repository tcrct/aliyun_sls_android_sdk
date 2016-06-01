package com.syt.aliyun.sdk.log.entity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import com.syt.aliyun.sdk.common.Consts;
import com.syt.aliyun.sdk.enums.HttpMethod;
import com.syt.aliyun.sdk.kit.ToolsKit;

public class RequestMessage {
	private HttpMethod method = HttpMethod.GET; // HTTP Method. default GET.
	private URI endpoint;
	private String resourcePath;
	private Map<String, String> headers = new HashMap<String, String>();

	public HttpMethod getMethod() {
		return method;
	}

	public void setMethod(HttpMethod method) {
		this.method = method;
	}

	public URI getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(URI endpoint) {
		this.endpoint = endpoint;
	}

	public String getResourcePath() {
		return resourcePath;
	}

	public void setResourcePath(String resourcePath) {
		this.resourcePath = resourcePath;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public InputStream getContent() {
		return content;
	}

	/*
	public String getContentString() {
		if (ToolsKit.isEmpty(content))
			throw new NullPointerException("InputStream对象不能为空");
		StringBuilder buffer = new StringBuilder();
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(content));
			String line = "";
			while ((line = in.readLine()) != null) {
				buffer.append(new String(line.getBytes(), Consts.UTF_8_ENCODING));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null)
					in.close();
				if (content != null)
					content.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return buffer.toString();
	}
*/
	public void setContent(InputStream content) {
		this.content = content;
	}

	public long getContentLength() {
		return contentLength;
	}

	public void setContentLength(long contentLength) {
		this.contentLength = contentLength;
	}

	private InputStream content;
	private long contentLength;
}
