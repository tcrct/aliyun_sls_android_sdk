package com.syt.aliyun.sdk.enums;

/**
 * 表示HTTP的请求方法。
 * 
 */
public enum HttpMethod {
	/**
	 * DELETE方法。
	 */
	DELETE("DELETE"),

	/**
	 * GET方法。
	 */
	GET("GET"),

	/**
	 * HEAD方法。
	 */
	HEAD("HEAD"),

	/**
	 * POST方法。
	 */
	POST("POST"),

	/**
	 * PUT方法。
	 */
	PUT("PUT"),

	/**
	 * OPTION方法。
	 */
	OPTIONS("OPTIONS");

	private final String text;

	private HttpMethod(final String text) {
		this.text = text;
	}

	public String toString() {
		return this.text;
	}
}
