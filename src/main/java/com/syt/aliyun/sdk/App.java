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
		String keyId = "keyId";
		String keySecret = "keySecret";
		String securityToken = "securityToken";		

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
				 String responseBody = "{\"errorCode\":\"201\", \"errorMessage\":\"AsyncHttpClient is null\"}";
				client.checkResponse(201,responseBody);
			    //客户端自行实现的HttpClient, 必须以POST方式提交, 以AsyncHttpClient为例
				/*
				 String url = messages.getEndpoint()+messages.getResourcePath();
                 ApiHttpClient.client.removeAllHeaders();		//先删除所有header
                 Map<String,String> headers = messages.getHeaders();
                 for (Iterator it = headers.entrySet().iterator(); it.hasNext(); ) {
                     Map.Entry e = (Map.Entry) it.next();
                     ApiHttpClient.client.addHeader(e.getKey().toString(),e.getValue().toString());
                 }
                 String contentType = headers.get("Content-Type");
                 InputStreamEntity sEntity = new InputStreamEntity(messages.getContent(), messages.getContentLength());
                 ApiHttpClient.client.post(context, url, sEntity, contentType, new AsyncHttpResponseHandler() {
                     @Override
                     public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
							client.checkResponse(statusCode, new String(responseBody));//判断是否提交成功
                     }

                     @Override
                     public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                     }
                 });
                 */
			} catch (LogException e) {
				e.printStackTrace();
			}
		}
	}
}
