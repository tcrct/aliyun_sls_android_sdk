package com.syt.aliyun.sdk.log.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class LogItem implements Serializable {
	private static final long serialVersionUID = -3488075856612935955L;
	public int mLogTime;
	public ArrayList<LogContent> mContents = new ArrayList<LogContent>();

	public LogItem() {
		mLogTime = ((int) (new Date().getTime() / 1000L));
	}

	public LogItem(int logTime) {
		mLogTime = logTime;
	}

	public LogItem(int logTime, ArrayList<LogContent> contents) {
		mLogTime = logTime;
		SetLogContents(contents);
	}

	public void SetTime(int logTime) {
		mLogTime = logTime;
	}

	public int GetTime() {
		return mLogTime;
	}

	public void PushBack(String key, String value) {
		PushBack(new LogContent(key, value));
	}

	public void PushBack(LogContent content) {
		mContents.add(content);
	}

	public void SetLogContents(ArrayList<LogContent> contents) {
		mContents = new ArrayList(contents);
	}

	public ArrayList<LogContent> GetLogContents() {
		return mContents;
	}
}
