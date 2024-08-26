# 注册

##### 指令
```java
LiteKookFactory.builder(plugin)
    .commands(LiteCommand.class, new ExampleCommands());// 你要注册的指令，可以传class或者对象，如果传class请注意查看 TODO
```


##### 参数解析器
解析器包括了 [Suggester](#参数提示)(参数提示,Tab补全)和[Parser](#参数解析)(参数解析)

```java
LiteKookFactory.builder(plugin)
    .argument(User.class, new UserArgumentResovler());
```

##### 参数提示
```java
LiteKookFactory.builder(plugin)
    .argumentSuggester(User.class, new UserSuggester());
```

##### 参数解析
```java
LiteKookFactory.builder(plugin)
    .argumentParser(User.class, new UserParser());
```