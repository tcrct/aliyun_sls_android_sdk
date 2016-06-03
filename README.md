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
      System.out.println(messages.getEndpoint());				
      //客户端自行实现的HttpClient, 必须以POST方式提交
      //HttpClient.post(message);
      } catch (LogException e) {
      	e.printStackTrace();
      }

