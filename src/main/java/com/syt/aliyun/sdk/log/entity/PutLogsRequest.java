package com.syt.aliyun.sdk.log.entity;

import java.util.ArrayList;
import java.util.List;

public class PutLogsRequest extends Request {
	private static final long serialVersionUID = 7226856831224917838L;
	private String mLogStore;
	private String mTopic;
	private String mSource;
	private ArrayList<LogItem> mlogItems;

	public PutLogsRequest(String project, String logStore, String topic, List<LogItem> logItems) {
		super(project);
		mLogStore = logStore;
		mTopic = topic;
		mlogItems = new ArrayList<LogItem>(logItems);
	}

	public String GetLogStore() {
		return mLogStore;
	}

	public void SetLogStore(String logStore) {
		mLogStore = logStore;
	}

	public String GetTopic() {
		return mTopic;
	}

	public void SetTopic(String topic) {
		mTopic = topic;
	}

	public String GetSource() {
		return mSource;
	}

	public void SetSource(String source) {
		mSource = source;
	}

	public ArrayList<LogItem> GetLogItems() {
		return mlogItems;
	}

	public void SetlogItems(List<LogItem> logItems) {
		mlogItems = new ArrayList<LogItem>(logItems);
	}
}
