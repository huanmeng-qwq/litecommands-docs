# 自定义解析

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
public class UserArgumentResolver extends ArgumentResolver<CommandSender, User> {
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
#### **Parser**
```java
public class UserArgumentResolver extends ArgumentResolver<CommandSender, User> {
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