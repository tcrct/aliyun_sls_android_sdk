package com.syt.aliyun.sdk;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.syt.aliyun.sdk.exception.LogException;
import com.syt.aliyun.sdk.log.LogClient;
import com.syt.aliyun.sdk.log.entity.LogItem;
import com.syt.aliyun.sdk.log.entity.PutLogsRequest;
import com.syt.aliyun.sdk.log.entity.RequestMessage;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		String keyId = "STS.HSwXqgzv7MxLL9ZAbdmEWvFxx";
		String keySecret = "7krC7RLDZJcs7tq2v6AZy3Tvay9xUjJrWik99cPqvKxN";
		String securityToken = "CAES8gIIARKAATBkGS2BTzDk9ON+tJSEDxAE6d6vt1b7PRfGdRrQj52YlSqbzM+FTYoXfLmvyYN6mXSO0YrEaMmedGJY/x0UpX7VEUf5hVixCH4GG2DpYLwpg2cfif31CXKKmsV97T8wguNmSiApQLOepV27xS4T/C2KpDE/8L61cYeyDSCPcjlIGh1TVFMuSFN3WHFnenY3TXhMTDlaQWJkbUVXdkZ4eCISMzkwMTYwMTQ4OTU3NDg4MDI1KglHdWVzdFJvbGUwr5yG0NAqOgZSc2FNRDVCSgoBMRpFCgVBbGxvdxIbCgxBY3Rpb25FcXVhbHMSBkFjdGlvbhoDCgEqEh8KDlJlc291cmNlRXF1YWxzEghSZXNvdXJjZRoDCgEqShAxMTQzODU2MzMwMzQxNDQ0UgUyNjg0MloPQXNzdW1lZFJvbGVVc2VyYABqEjM5MDE2MDE0ODk1NzQ4ODAyNXIJZ3Vlc3Ryb2xleMT4+fHMioQC";

		// 生产环境中要改为单例的
		LogClient client = new LogClient("http://cn-qingdao.log.aliyuncs.com", keyId, keySecret, securityToken);

		for (int i = 111; i < 200; i++) {
			String project = "project";
			String logStore = "logstore";
			String topic = "topic";
			List<LogItem> logItems = new ArrayList<LogItem>();
			LogItem logItem = new LogItem((int) (new Date().getTime() / 1000));
			logItem.PushBack("userid", i + "");
			logItem.PushBack("system", "android_" + i);
			logItem.PushBack("message", "it's a test message_" + i);
			logItems.add(logItem);
			PutLogsRequest request = new PutLogsRequest(project, logStore, topic, logItems);
			try {
				RequestMessage messages = client.getRequestMessage(request);
				System.out.println(messages.getEndpoint());
			} catch (LogException e) {
				e.printStackTrace();
			}
		}
	}
}
