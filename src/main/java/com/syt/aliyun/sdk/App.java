package com.syt.aliyun.sdk;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.syt.aliyun.sdk.exception.LogException;
import com.syt.aliyun.sdk.log.LogClient;
import com.syt.aliyun.sdk.log.entity.LogContent;
import com.syt.aliyun.sdk.log.entity.LogItem;
import com.syt.aliyun.sdk.log.entity.PutLogsRequest;
import com.syt.aliyun.sdk.log.entity.RequestMessage;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		String keyId = "STS.MQd1GPPQLxTEM4Gr7ZjRfXnAc";
		String keySecret = "5H9C9JRxTRFrJcfMUoGuHyNRBQGaK2Q1jupgwAgjnpsq";
		String securityToken = "CAES8gIIARKAAXIYW7vXgiTBmQMk0EWfYmCVC4+IoPO0TiZsggqPrkSYTXDFYe5oyXnCnG87iTS3nqO7HmF5we6vBY7Dp/L9LXw7tWYnHQ3jKt9AxVbRgtwiC4XiAJFq/jtDzVyF1WTpR1K9IvnvgCwGo4tQGg6m+AmWnmepNWLx8IocobKYp9F+Gh1TVFMuTVFkMUdQUFFMeFRFTTRHcjdaalJmWG5BYyISMzkwMTYwMTQ4OTU3NDg4MDI1KglHdWVzdFJvbGUwnc3W+tAqOgZSc2FNRDVCSgoBMRpFCgVBbGxvdxIbCgxBY3Rpb25FcXVhbHMSBkFjdGlvbhoDCgEqEh8KDlJlc291cmNlRXF1YWxzEghSZXNvdXJjZRoDCgEqShAxMTQzODU2MzMwMzQxNDQ0UgUyNjg0MloPQXNzdW1lZFJvbGVVc2VyYABqEjM5MDE2MDE0ODk1NzQ4ODAyNXIJZ3Vlc3Ryb2xleMT4+fHMioQC";

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
				String endpoint = messages.getEndpoint()+messages.getResourcePath();
				System.out.println(endpoint);
			} catch (LogException e) {
				e.printStackTrace();
			}
		}
	}
}
