# 快速开始

> 本章通过编写一个快速的KookBC插件demo样例来快速展示Litecommand使用

[快速开始项目代码](https://github.com/DAYGoodTime/litecommands-docs/tree/main/example/quickstart)

#### 引入KooKBC依赖 (使用其litecommand的实现)

```xml

<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://www.jitpack.io</url>
    </repository>
    <repository>
        <id>panda-repository</id>
        <url>https://repo.panda-lang.org/releases</url>
    </repository>
</repositories>
<dependencies>
<dependency>
    <groupId>com.github.SNWCreations</groupId>
    <artifactId>KooKBC</artifactId>
    <version>0.31.0</version>
    <scope>provided</scope>
</dependency>
</dependencies>
```

#### 编写一个简单命令类

`ExampleCommand.java`

```java

@Command(name = "example", aliases = {"exp", "exp2"})
@Prefix("/")
@Description(value = {"a simple command example", "second description"})
public class ExampleCommand {

    @Execute(name = "set mode")
    @Description("set user mode")
    public void SetMode(
    ) {
    }

    @Execute(name = "set server")
    @Description("set user server")
    public void SetServer(
    ) {
    }

    @Execute(name = "help")
    @Description("get help")
    public void Help() {
    }
}
```

- `@Command`注解用来表示一个根命令类 此处为注册一个 example命令。同时还可以设置多个别名，此处为exp 或者 exp2
- `@Prefix`用来设置命令的前缀，此处为 '/' 即 /example
- `@Description` 用来设置根命令的描述，可以设置多个描述
- `@Execute` 设置命令的执行器`executor`。execute也可以设置多个别名。现在，我们有三个命令。
    - /example set mode 或 /example sm
    - /example set name 或 /example sn
    - /example help

#### 注册命令

我们将在插件启动后对命令进行注册。\
`PluginMain.java`

```java

@Override
public void onEnable() {
    LiteKookFactory
            .builder(this)
            .commands(ExampleCommand.class);//推荐使用类方式注册,可以支持@Inject注入依赖
    //            .commands(new ExampleCommand())
}
```

一个基本的命令就这样完成了。当然，我们还需要添加一些内容。\
`ExampleCommand.java`

```java

@Execute(name = "set mode")
public void SetMode(
        @Context User user,
        @Arg("mode") String mode
) {
}
```

`@Context`的作用是为了给命令执行的添加上下文。在此处例子当中，插件会尝试添加JKook User对象。\
`@Arg` 为命令添加必选参数,则执行该命令需要输入 /example set mode 模式1 `@Arg("mode")`
当中的mode是为了给schema生成提供参考，如果不添加，则使用默认名字。

#### 自义定解析

很多时候，你需要将纯文本参数转换成枚举类或者复杂数据类型，这个时候你就需要设置自定义解析器。\
回到命令类当中，在需要添加自义定解析的参数当中添加@Key注解来制定特定参数的解析器(如果不添加则对所有拥有该参数的命令生效)
`ExampleCommand.java`

```java

@Execute(name = "set server")
@Description("set user server")
public void SetServer(
        @Context User user,
        @Arg("server") @Key("server") Server server
) {
}
```

为server标注一个ArgumentKey(默认使用Arg注解的名字,如果相同则不需要使用@Key注解)，用来注册对应的handler。然后我们回到命令注册那里

<!-- tabs:start -->

#### **PluginMain.java**

```java
LiteKookFactory
        .builder(this)
        .

commands(new ExampleCommand())
        .

argument(Server .class, ArgumentKey.of("server"),new

ServerResolver());
```

如果不使用ArgumentKey.of("mode")标记特定参数解析。则对所有命令生效

#### **ServerResolver.java**

```java
// 针对Mode参数的解释器和建议提示。
// CommandSender对应其平台的CommandSender
// String代表着解析结果
// invocation表示命令的上下文
// argument 表示参数对象
// s 代表着原始输入(已分割好)
public class ServerResolver extends ArgumentResolver<CommandSender, Server> {
    @Override
    protected ParseResult<Server> parse(Invocation<CommandSender> invocation, Argument<Server> argument, String s) {
        if (Arrays.asList("server1", "server2").contains(s)) {
            return ParseResult.success(Server.valueOf(s));
        } else {
            return ParseResult.failure("错误的服务器类型");
        }
    }

    @Override
    public SuggestionResult suggest(Invocation<CommandSender> invocation, Argument<Server> argument, SuggestionContext context) {
        return SuggestionResult.of("server1", "server2");
    }
}

```

<!-- tabs:end -->

此时用户输入/example set mode 模式3 的时候，会返回`请输入正确的模式`的错误文本。\
在支持建议补全的平台当中，suggest也会返回该参数可以选择的列表。

> 具体的说明可以查看 [命令解析](/parser/)

#### 命令上下文的注入

很多时候，插件会有自己的`用户系统`
，与平台用户对其进行绑定，这个时候许多命令操作需要从后台查询权限、账号等。那么就需要在命令里插入大量的与命令操作无关的代码。\
那么上下文的注入能够显著减少这方面的冗余。\
现在我们回到命令类当中。\
`ExampleCommand.java`

```java
public class ExampleCommand {

    private final AccountMapper mapper;

    // 如果你没有任何需要的依赖,自然也不需要使用@Inject注入了
    @Inject
    public ExampleCommand(AccountMapper mapper) {
        this.mapper = mapper;
    }

    @Execute(name = "set mode")
    @Description("set user mode")
    public void SetMode(
            @Context User user,
            @Context Account account,
            @Arg("mode") @Key("mode") String mode
    ) {
    }
    //others ....
}
```

给Account编写一个上下文提供器。

`AccountContextProvider.java`

```java
public class AccountContextProvider implements ContextProvider<CommandSender, Account> {
    private final AccountMapper accountMapper;

    public AccountContextProvider(AccountMapper accountMapper) {
        this.accountMapper = accountMapper;
    }

    @Override
    public ContextResult<Account> provide(Invocation<CommandSender> invocation) {
        // 判断sender是否为平台的User
        if (invocation.sender() instanceof User) {
            User user = (User) invocation.sender();
            // 进行正常的‘账号’查询
            Account account = accountMapper.getAccountById(Integer.parseInt(user.getId()));
            if (account == null) {
                return ContextResult.error("用户未找到");
            } else {
                return ContextResult.ok(() -> account);
            }
        } else {
            return ContextResult.error("请通过用户交互进行命令");
        }
    }
}
```

注册依赖注入和上下文提供器。

`PluginMain.java`

```java
LiteKookFactory
        .builder(this)
        .bind(AccountMapper .class, AccountMapper::new)// 给需要AccountMapper的命令注入实例
        .context(Account .class,new AccountContextProvider(new AccountMapper()))// 注册Account的上下文提供器
        .commands(ExampleCommand .class)
        .argument(String .class, ArgumentKey.of("mode"), new ModeResolver())
        .invalidUsage(new BasicInvalidUsageHandler());
        // more registry
```

#### 设置命令执行失败的提示

用户输入不符合命令要求的时候会返回一个默认错误文本。可惜，这对于面向非开发人员的用户交互来说，这个文本多少有点晦涩，例如
`Invalid usage of command! (INVALID_USAGE)` \
于是我们需要增强用户的交互体验，我们需要设置命令的错误返回。

<!-- tabs:start -->

#### **BasicInvalidUsageHandler.java**

这里通过输出命令的Schematic作为命令交互提示 \
Schematic 代表着该执行器的schema。
你可以通过自定义SchematicGenerator来定制schema的内容。

```java
public class BasicInvalidUsageHandler implements InvalidUsageHandler<CommandSender> {
    @Override
    public void handle(Invocation<CommandSender> invocation, InvalidUsage<CommandSender> result, ResultHandlerChain<CommandSender> chain) {
        Schematic schematic = result.getSchematic();
        InvalidUsage.Cause cause = result.getCause();
        String reason;
        switch (cause) {
            // 例如输入了: example abc
            // 但是没注册abc这个子命令，这里就是UNKNOWN_COMMAND
            case UNKNOWN_COMMAND:
                reason = "未知指令";
                break;
            // 例如输入了: example print text
            // 但是text这个参数的类型是 <num:int> text不是int，这里就是INVALID_ARGUMENT
            // 该类型是由解析器传递的: ParseResult.failure(FailedReason.of(InvalidUsage.Cause.INVALID_ARGUMENT));
            case INVALID_ARGUMENT:
                reason = "错误的参数";
                break;
            // 例如注册指令: example <location>
            // location是一个需要接收3个参数的类型
            // 但是用户输入了: exmaple 0 100
            // 用户只输入了2个参数 缺一个，这里就是MISSING_ARGUMENT
            case MISSING_ARGUMENT:
                reason = "需要参数";
                break;
            // 例如注册指令: exmaple [text] [location]
            // 但是用户输入了: exmaple text 10 20
            // 这里就是MISSING_PART_OF_ARGUMENT
            case MISSING_PART_OF_ARGUMENT:
                reason = "参数缺失";
                break;
            // 例如输入了: example print text 1000
            // 1000就多出来的参数， 这里就是TOO_MANY_ARGUMENTS
            case TOO_MANY_ARGUMENTS:
                reason = "参数过多";
                break;
            default:
                reason = "未知错误";
                break;
        }
        StringBuilder example = new StringBuilder();
        example.append("错误原因:").append(reason).append("\n");
        schematic.all().forEach((it) -> {
            //添加每一条指令的schema
            example.append(it).append("\n");
        });
        example.deleteCharAt(example.length() - 1);//删除最后一个换行符
        chain.resolve(invocation, example);// 执行result的结果处理 例如sender.sendMessage(reason) 或者 message.reply(reason)
    }
}
```

#### **DescSchematicGenerator.java**

该生成器可以生成命令下的所有子命令的样例。用户输入/example set 会输出 \

- /example set mode <mode> # set user mode
- /example set server <server> # set user server

```java
public class DescSchematicGenerator extends SimpleSchematicGenerator<CommandSender> {

    public DescSchematicGenerator(ValidatorService<CommandSender> validatorService, ParserRegistry<CommandSender> parserRegistry) {
        super(SchematicFormat.angleBrackets(), validatorService, parserRegistry);
    }

    // 这里返回Schematic的原始内容。通过去重平铺后,new Schematic(schematics)来创建Schematic
    @Override
    protected Stream<String> generateRaw(SchematicInput<CommandSender> schematicInput) {
        CommandExecutor<CommandSender> executor = schematicInput.getExecutor();
        // 此处获得命令的route内容，例如/example set mode,则为 set mode
        String base = schematicInput.collectRoutes().stream()
                .skip(1)// 这里去除掉第一个元素 代表去除掉指令一级名称，下面用label替换
                .map(CommandRoute::getName)
                .collect(Collectors.joining(" "))
                + " ";
        if (base.trim().isEmpty()) {
            base = schematicInput.getInvocation().label() + " ";
        } else {
            base = schematicInput.getInvocation().label() + " " + base;
        }
        // 这里获取命令类下的所有子命令描述
        Stream<String> routeScheme = generateRoute(schematicInput, schematicInput.getLastRoute(), base);
        // 如果有executor则和上面的结果合并
        if (executor != null) {
            Stream<String> executorScheme = Stream.of(base + generateExecutor(schematicInput, executor));
            return Stream.concat(routeScheme, executorScheme);
        }
        return routeScheme;
    }

    @Override
    protected Stream<String> generateRoute(SchematicInput<CommandSender> input, CommandRoute<CommandSender> route, String base) {
        // 为每个子router生成RouterSchema(递归)
        Stream<String> children = route.getChildren().stream()
                .flatMap(subRoute -> generateRoute(input, subRoute, base + subRoute.getName() + " "));
        // 为每个Route下的executors生成schema
        Stream<String> executors = route.getExecutors().stream()
                .filter(executor -> isVisible(input, executor))
                .map(executor -> {
                    String prefix = base;
                    if (executor.getArguments().isEmpty()) {
                        prefix = base.substring(0, base.length() - 1);// 如果没有参数 那就应该移除掉最后的一个空格字符
                    }
                    return prefix + generateExecutor(input, executor);
                });
        // 合并schema
        return Stream.concat(executors, children);
    }

    // 为具体的executor生成schema
    @Override
    protected String generateExecutor(SchematicInput<CommandSender> input, CommandExecutor<CommandSender> executor) {
        String string = executor.getArguments().stream()
                .map(argument -> String.format(generateArgumentFormat(input, argument), generateArgumentName(input, argument)))
                .collect(Collectors.joining(" "));
        StringBuilder sb = new StringBuilder(string);
        List<String> desc = executor.meta().get(Meta.DESCRIPTION);// 获取该指令执行器的介绍信息
        // 合并描述
        if (!desc.isEmpty()) {
            sb.append(" # ");
            sb.append(String.join(", ", desc));
        }
        return sb.toString();// 返回例如: example set mode <mode> # 该指令的简介信息
    }

    @Override
    protected String generateArgumentFormat(SchematicInput<CommandSender> input, Argument<?> argument) {
        if (argument.getProfile(LiteralProfile.NAMESPACE).isPresent()) {
            return argument.getName();
        }
        return this.isOptional(input, argument) ? this.format.optionalArgumentFormat() : this.format.argumentFormat();
    }
}
```

#### **PluginMain.java**

在命令注册当中添加`错误的使用命令`的处理类和SchematicGenerator

```java
LiteKookFactory
        .builder(this)
        .commands(new ExampleCommand())
        .argument(String .class, ArgumentKey.of("mode"),new ModeResolver())
        .invalidUsage(new BasicInvalidUsageHandler())
        .self((builder, internal) ->
        builder.schematicGenerator(new DescSchematicGenerator(
                internal.getValidatorService(),
                 internal.getWrapperRegistry())
            )
        );
// 如果你只需要注册自己的SchematicGenerator.你可以修改成。
// .schematicGenerator(new CustomSchematicGenerator())

```

#### **CustomSchematicGenerator.java**

当然，你也可以自己写一个非常简单的SchematicGenerator,或者不写。则此时Schematic会使用自己默认的生成器

```java
public class CustomSchematicGenerator implements SchematicGenerator<CommandSender> {

    // 简单获取命令的DESCRIPTION作为schema
    @Override
    public Schematic generate(SchematicInput<CommandSender> schematicInput) {
        List<String> desc = schematicInput.getLastRoute().meta().get(Meta.DESCRIPTION);
        return new Schematic(desc);
    }
}
```

<!-- tabs:end -->

> 关于InvalidUsage详细说明可以看 [InvalidUsage](/invalidusage/)



