package com.syt.aliyun.sdk.log.entity;

import com.syt.aliyun.sdk.enums.HttpMethod;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class RequestMessage {
	private HttpMethod method = HttpMethod.GET; // HTTP Method. default GET.
	private URI endpoint;
	private String resourcePath;
	private Map<String, String> headers = new HashMap<String, String>();
	private byte[] body;
	private long contentLength;

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

	public long getContentLength() {
		return contentLength;
	}

	public void setContentLength(long contentLength) {
		this.contentLength = contentLength;
	}

	public byte[] getBody() {
		return body;
	}

	public void setBody(byte[] body) {
		this.body = body;
	}


}
