# 自定义解析

通常在开发中一般都推荐继承`ArgumentResolver`来实现解析和命令补全提示

`ArgumentResolver`已经继承了`Parser`和`Suggester`接口

##### 注册
[注册](/register/?id=参数解析器)


##### 自定义类型
```java
public class User {
    private final String name;
    private final int age;

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return this.name;
    }
}
```
<!-- tabs:start -->
#### **ArgumentResolver**
```java
public class UserArgumentResolver extends ArgumentResolver<CommandSender, User> {
    @Override
    protected ParseResult<User> parse(Invocation<CommandSender> invocation, Argument<User> context, String argument) {
        User user = userManager.findUser(argument);

        if (user != null) {
            return ParseResult.success(user);
        }

        return ParseResult.failure("用户未找到");
    }
}
```
#### **Parser**
```java
public class UserParser implements Parser<CommandSender, User> {
    @Override
    public final ParseResult<User> parse(Invocation<CommandSender> invocation, Argument<User> argument, RawInput rawInput) {
        User user = userManager.findUser(rawInput.next());

        if (user != null) {
            return ParseResult.success(user);
        }

        return ParseResult.failure("用户未找到");
    }
}
```

<!-- tabs:end -->


##### 自定义类型的指令补全(Suggester)

<!-- tabs:start -->
#### **ArgumentResolver**
```java
public class UserArgumentResolver extends ArgumentResolver<CommandSender, User> {
    @Override
    protected ParseResult<User> parse(Invocation<CommandSender> invocation, Argument<User> context, String argument) {
        // ...
    }

    @Override
    public SuggestionResult suggest(Invocation<CommandSender> invocation, Argument<Player> argument, SuggestionContext context) {
        return userManager.getUsers().stream()
                .map(User::getName)
                .collect(SuggestionResult.collector());
        // 或者
        // return SuggestionResult.of("User1", "user2");
    }

}
```
#### **Suggester**
```java
public class UserSuggester implements Suggester<CommandSender, User> {
    // 与ArgumentResolver的一致
    @Override
    public SuggestionResult suggest(Invocation<CommandSender> invocation, Argument<Player> argument, SuggestionContext context) {
        return userManager.getUsers().stream()
                .map(User::getName)
                .collect(SuggestionResult.collector());
        // 或者
        // return SuggestionResult.of("User1", "user2");
    }
}
```

<!-- tabs:end -->