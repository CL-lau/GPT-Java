<h1 style="text-align: center; color: hotpink; -webkit-animation: rainbow 5s infinite; -moz-animation: rainbow 5s infinite; -o-animation: rainbow 5s infinite; animation: rainbow 5s infinite;">ChatGPT Java API</h1>

OpenAI ChatGPT 的SDK。觉得不错请右上角Star. 修改自 [Github 仓库](https://github.com/PlexPt/chatgpt-java)

# 中文语料库

[中文语料库 67万+问题，欢迎拿去炼丹](https://github.com/PlexPt/chatgpt-corpus)


# 功能特性

|     功能      |                         特性                         |
|:-----------:|:--------------------------------------------------:|
|   GPT 3.5   |                         支持                         |
|   GPT 4.0   |                         支持                         |
| GPT 3.5-16k |                         支持                         |
| GPT 4.0-32k |                         支持                         |
|    函数调用     |                         支持                         |
|    流式对话     |                         支持                         |
|    阻塞式对话    |                         支持                         |
|     前端      |                         支持                         |
|     上下文     |                         支持                         |
|   计算Token   | [jtokkit](https://github.com/knuddelsgmbh/jtokkit) |
|   多KEY轮询    |                         支持                         |
|     代理      |                         支持                         |
|    反向代理     |                         支持                         |
| Embedding转换 |                         支持                         |


## 使用指南

[//]: # (最新版本 [![Maven Central]&#40;https://img.shields.io/maven-central/v/com.github.plexpt/chatgpt&#41;]&#40;https://maven-badges.herokuapp.com/maven-central/com.github.plexpt/chatgpt&#41;)

maven
```

```

gradle
```

```



### 最简使用

也可以使用这个类进行测试 [ConsoleChatGPT](src/main/java/com/clau/chatgpt/ConsoleChatGPT.java)

```java
      //国内需要代理
      Proxy proxy = Proxys.http("127.0.0.1", 1081);
     //socks5 代理
    // Proxy proxy = Proxys.socks5("127.0.0.1", 1080);

      ChatGPT chatGPT = ChatGPT.builder()
                .apiKey("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")
                .proxy(proxy)
                .apiHost("https://api.openai.com/") //反向代理地址
                .build()
                .init();
                
        String res = chatGPT.chat("写一段七言绝句诗，题目是：火锅！");
        System.out.println(res);

```


### 进阶使用

```java
      //国内需要代理 国外不需要
      Proxy proxy = Proxys.http("127.0.0.1", 1080);

      ChatGPT chatGPT = ChatGPT.builder()
                .apiKey("sk-*****************************")
                .proxy(proxy)
                .timeout(900)
                .apiHost("https://api.openai.com/") //反向代理地址
                .build()
                .init();
     
        Message system = Message.ofSystem("你现在是一个一直打拼的程序员，主要复杂Java开发。");
        Message message = Message.of("写一个高水平的五子棋游戏！");

        ChatCompletion chatCompletion = ChatCompletion.builder()
                .model(ChatCompletion.Model.GPT_3_5_TURBO.getName())
                .messages(Arrays.asList(system, message))
                .maxTokens(3000)
                .temperature(0.9)
                .build();
        ChatCompletionResponse response = chatGPT.chatCompletion(chatCompletion);
        Message res = response.getChoices().get(0).getMessage();
        System.out.println(res);

```
### 函数调用（Function Call）

```java
      //国内需要代理 国外不需要
          Proxy proxy = Proxys.http("127.0.0.1", 1080);

                  chatGPT = ChatGPT.builder()
                  .apiKey("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")
                  .timeout(900)
                  .proxy(proxy)
                  .apiHost("https://api.openai.com/") //代理地址
                  .build()
                  .init();

        List<ChatFunction> functions = new ArrayList<>();
        ChatFunction function = new ChatFunction();
        function.setName("getCurrentWeather");
        function.setDescription("获取给定位置的当前天气");
        function.setParameters(ChatFunction.ChatParameter.builder()
        .type("object")
        .required(Arrays.asList("location"))
        .properties(JSON.parseObject("{\n" +
        "          \"location\": {\n" +
        "            \"type\": \"string\",\n" +
        "            \"description\": \"The city and state, e.g. San Francisco, " +
        "CA\"\n" +
        "          },\n" +
        "          \"unit\": {\n" +
        "            \"type\": \"string\",\n" +
        "            \"enum\": [\"celsius\", \"fahrenheit\"]\n" +
        "          }\n" +
        "        }"))
        .build());
        functions.add(function);

        Message message = Message.of("上海的天气怎么样？");
        ChatCompletion chatCompletion = ChatCompletion.builder()
        .model(ChatCompletion.Model.GPT_3_5_TURBO_0613.getName())
        .messages(Arrays.asList(message))
        .functions(functions)
        .maxTokens(8000)
        .temperature(0.9)
        .build();
        ChatCompletionResponse response = chatGPT.chatCompletion(chatCompletion);
        ChatChoice choice = response.getChoices().get(0);
        Message res = choice.getMessage();
        System.out.println(res);
        if ("function_call".equals(choice.getFinishReason())) {

        FunctionCallResult functionCall = res.getFunctionCall();
        String functionCallName = functionCall.getName();

        if ("getCurrentWeather".equals(functionCallName)) {
        String arguments = functionCall.getArguments();
        JSONObject jsonObject = JSON.parseObject(arguments);
        String location = jsonObject.getString("location");
        String unit = jsonObject.getString("unit");
        String weather = getCurrentWeather(location, unit);

        callWithWeather(weather, res, functions);
        }
        }


    private void callWithWeather(String weather, Message res, List<ChatFunction> functions) {


        Message message = Message.of("上海的天气怎么样？");
        Message function1 = Message.ofFunction(weather);
        function1.setName("getCurrentWeather");
        ChatCompletion chatCompletion = ChatCompletion.builder()
        .model(ChatCompletion.Model.GPT_3_5_TURBO_0613.getName())
        .messages(Arrays.asList(message, res, function1))
        .functions(functions)
        .maxTokens(8000)
        .temperature(0.9)
        .build();
        ChatCompletionResponse response = chatGPT.chatCompletion(chatCompletion);
        ChatChoice choice = response.getChoices().get(0);
        Message res2 = choice.getMessage();
        //上海目前天气晴朗，气温为 22 摄氏度。
        System.out.println(res2.getContent());
        }

    public String getCurrentWeather(String location, String unit) {
        return "{ \"temperature\": 22, \"unit\": \"celsius\", \"description\": \"晴朗\" }";
        }

```

### 流式使用

```java
      //国内需要代理 国外不需要
      Proxy proxy = Proxys.http("127.0.0.1", 1080);

      ChatGPTStream chatGPTStream = ChatGPTStream.builder()
                .timeout(600)
                .apiKey("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")
                .proxy(proxy)
                .apiHost("https://api.openai.com/")
                .build()
                .init();

                
        ConsoleStreamListener listener = new ConsoleStreamListener();
        Message message = Message.of("写一段七言绝句诗，题目是：火锅！");
        ChatCompletion chatCompletion = ChatCompletion.builder()
                .messages(Arrays.asList(message))
                .build();
        chatGPTStream.streamChatCompletion(chatCompletion, listener);

```

### 流式配合Spring SseEmitter使用

参考 [SseStreamListener](src/main/java/com/clau/chatgpt/listener/SseStreamListener.java)

参考Demo https://github.com/PlexPt/chatgpt-online-springboot

```java
  

    @GetMapping("/chat/sse")
    @CrossOrigin
    public SseEmitter sseEmitter(String prompt) {
       //国内需要代理 国外不需要
       Proxy proxy = Proxys.http("127.0.0.1", 1080);

       ChatGPTStream chatGPTStream = ChatGPTStream.builder()
                .timeout(600)
                .apiKey("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")
                .proxy(proxy)
                .apiHost("https://api.openai.com/")
                .build()
                .init();
        
        SseEmitter sseEmitter = new SseEmitter(-1L);

        SseStreamListener listener = new SseStreamListener(sseEmitter);
        Message message = Message.of(prompt);

        listener.setOnComplate(msg -> {
            //回答完成，可以做一些事情
        });
        chatGPTStream.streamChatCompletion(Arrays.asList(message), listener);


        return sseEmitter;
    }

```



## 多KEY自动轮询

只需替换chatGPT构造部分

```
chatGPT = ChatGPT.builder()
        .apiKeyList(
               // 从数据库或其他地方取出多个KEY
                Arrays.asList("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",
                        "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",
                        "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",
                        "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",
                        ))
        .timeout(900)
        .proxy(proxy)
        .apiHost("https://api.openai.com/") //代理地址
        .build()
        .init();
```

## 上下文

参考  [ChatContextHolder.java](src/main/java/com/clau/chatgpt/util/ChatContextHolder.java) 



# 常见问题

|                              问                              |                              答                              |
| :----------------------------------------------------------: | :----------------------------------------------------------: |
|                         KEY从哪来？                          | 手动注册生成：ai.com(需要海外手机号)、或者成品独享帐号：[购买](https://fk.fq.mk/?code=YT0xJmI9Mg%3D%3D) |
|                        哪些地区不能用                        | **以下国家IP不支持使用：中国(包含港澳台) 俄罗斯 乌克兰 阿富汗 白俄罗斯 委内瑞拉 伊朗 埃及!!** |
|                         有封号风险吗                         |              充值的没有。你免费白嫖不封你封谁。              |
|                  我是尊贵的Plus会员，能用吗                  |             能用，照封不误。PLUS调用API没啥区别              |
|                        GPT4.0 怎么用                         |          申请 https://openai.com/waitlist/gpt-4-api          |
|                  api.openai.com ping不通？                   |                   禁ping，用curl测试连通性                   |
|                          显示超时？                          |                        IP不好，换个IP                        |
| 显示`Your access was terminated due to violation of our policies`... |                       你号没了，下一个                       |
| 显示`That model is currently overloaded with other requests. You can retry your request` |                   模型过载，官方炸了，重试                   |
|                      生成的图片不能用？                      |                   图片是它瞎编的，洗洗睡吧                   |
|                          如何充值？                          |                   用国外信用卡，国内的不行                   |
|                    没有国外信用卡怎么办？                    |         用这个, 扫码免费开卡并充值![](pic/depay.jpg)         |
|                         返回http 401                         |                     API 密钥写错了/没写                      |
|                         返回http 429                         |            请求超速了，或者官方超载了。充钱可解决            |
|                         返回http 500                         |                          服务器炸了                          |
|                                                              |                                                              |

---





### 注册教程

https://juejin.cn/post/7173447848292253704

https://mirror.xyz/boxchen.eth/9O9CSqyKDj4BKUIil7NC1Sa1LJM-3hsPqaeW_QjfFBc

#### 另外请看另一个项目 [ChatGPT中文使用指南](https://github.com/PlexPt/awesome-chatgpt-prompts-zh)


# Star History

[![Star History Chart](https://api.star-history.com/svg?repos=CL-lau/GPT-Java&type=Date)](https://star-history.com/#CL-lau/GPT-Java&Date)
