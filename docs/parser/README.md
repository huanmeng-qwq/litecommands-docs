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
    
    @Override
    public final Range getRange(Argument<TYPE> argument) {
        return Range.ONE; // 代表该参数仅解析一个元素
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
    public SuggestionResult suggest(Invocation<CommandSender> invocation, Argument<User> argument, SuggestionContext context) {
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
    public SuggestionResult suggest(Invocation<CommandSender> invocation, Argument<User> argument, SuggestionContext context) {
        return userManager.getUsers().stream()
                .map(User::getName)
                .collect(SuggestionResult.collector());
        // 或者
        // return SuggestionResult.of("User1", "user2");
    }
}
```

<!-- tabs:end -->

##### 针对指定的参数指定参数提示内容

> 目标:
>
> /example test &lt;num>
>
> num为int
>
> num默认情况下将会提示0-10的数字提示
>
> 我们需要指定num的提示结果为2-5的数字

```java

@Command(name = "example")
class ExampleCommand {
    public void test(@Arg("num") @Key("customKey") int i) {

    }
}

LiteKookFactory.builder(plugin)
    .argumentSuggestion(int.class, ArgumentKey.of("customKey"),SuggestionResult.of("2","3","4","5"));
```

`customKey`为自定义的一个key，可以根据具体情况不同而设置

通过`@Key`注解指定该参数的key后，将有限查找已注册对应key的`指令提示/Suggester`

除了通过`argumentSuggestion`方法，也可以使用`#argumentParser(TypeRange, ArgumentKey, ParserChained)`
等包含ArgumentKey类似的方法，本文仅展示了最便携的方式