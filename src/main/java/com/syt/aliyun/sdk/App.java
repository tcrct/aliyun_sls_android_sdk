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
		String keyId = "";
		String keySecret = "";
		String securityToken = "";		

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
			    //客户端自行实现的HttpClient, 必须以POST方式提交
			    //HttpClient.body(messages.getContent()).header(messages.getHeaders()).url(endPointUrl).post()
			} catch (LogException e) {
				e.printStackTrace();
			}
		}
	}
}
