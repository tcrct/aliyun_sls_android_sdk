# aliyun_sls_android_sdk
阿里云SLS日志服务Android端SDK

根据阿里云服务器端SDK改造，只提供GZIP方式压缩后进行提交，仅支持提交功能，不支持查询，删除等其它相关功能。

用法：

      String project = "project";
      String logStore = "logstore";
      String topic = "topic";
      List<LogItem> logItems = new ArrayList<LogItem>();
      LogItem logItem = new LogItem((int) (new Date().getTime() / 1000));
      logItem.PushBack("userid");
      logItem.PushBack("system", "android");
      logItem.PushBack("message", "it's a test message");
      logItems.add(logItem);
      PutLogsRequest request = new PutLogsRequest(project, logStore, topic, logItems);
      try {
      RequestMessage messages = client.getRequestMessage(request);
      String endpoint = messages.getEndpoint()+messages.getResourcePath();	
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
		        client.checkResponse(statusCode, new String(responseBody)); //判断是否提交成功
		 		}
		
		 @Override
		 public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
		
		 }
		 });
		 */
      } catch (LogException e) {
      	e.printStackTrace();
      }

