package com.syt.aliyun.sdk.log.entity;

import java.io.Serializable;

public class LogContent implements Serializable {
	private static final long serialVersionUID = 6042186396863898096L;
	public String mKey;
	public String mValue;

	public LogContent() {
	}

	public LogContent(String key, String value) {
		mKey = key;
		mValue = value;
	}

	public String GetKey() {
		return mKey;
	}

	public String GetValue() {
		return mValue;
	}
}
