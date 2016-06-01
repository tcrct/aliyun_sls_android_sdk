package com.syt.aliyun.sdk.log.entity;

import java.io.Serializable;

public class Request implements Serializable {
	private static final long serialVersionUID = -5830692390140453699L;
	private String mProject;

	public Request(String project) {
		mProject = project;
	}

	public String getProject() {
		return mProject;
	}

//	public String getParam(String key) {
//		if (mParams.containsKey(key)) {
//			return (String) mParams.get(key);
//		}
//		return new String();
//	}
}