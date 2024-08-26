# 指令用法提示

自定义`在用户输入指令没成功匹配`后的提示

##### 修改Schematic生成器

本文中的代码生成出的最终样式为:

未知指令

example test &lt;num> # 指令介绍信息

example print &#91;num] # 指令介绍信息

example execute &lt;num> &#91;state] # 指令介绍信息

---

###### SchematicFormat

`SchematicFormat`是定义`必选参数`和`可选参数`的前后缀

`SchematicFormat.angleBrackets()` 则对应:
&lt;必选参数名> &#91;可选参数名]


<!-- tabs:start -->

#### **Main.java**

```java
// 由于DescSchematicGenerator用到ValidatorService和WrapperRegistry所以这里使用selfProcessor获取internal对象
// 如果不需要的话可以直接builder.schematicGenerator(new DescSchematicGenerator());
builder.selfProcessor((builder, internal) -> {
    builder.schematicGenerator(
       
        new DescSchematicGenerator(SchematicFormat.angleBrackets(),  
            internal.getValidatorService(),
            internal.getWrapperRegistry())
    );
});
```

#### **DescSchematicGenerator.java**

```java
public class DescSchematicGenerator extends SimpleSchematicGenerator<CommandSender> {
    public DescSchematicGenerator(SchematicFormat format, ValidatorService<CommandSender> validatorService, WrapperRegistry wrapperRegistry) {
        super(format, validatorService, wrapperRegistry);
    }
    @Override
    protected Stream<String> generateRaw(SchematicInput<CommandSender> schematicInput) {
        CommandExecutor<CommandSender> executor = schematicInput.getExecutor();
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
        Stream<String> routeScheme = generateRoute(schematicInput, schematicInput.getLastRoute(), base);
        if (executor != null) {
            Stream<String> executorScheme = Stream.of(base + generateExecutor(schematicInput, executor));
            return Stream.concat(routeScheme, executorScheme);
        }
        return routeScheme;
    }
    @Override
    protected Stream<String> generateRoute(SchematicInput<CommandSender> input, CommandRoute<CommandSender> route, String base) {
        Stream<String> children = route.getChildren().stream()
            .flatMap(subRoute -> generateRoute(input, subRoute, base + subRoute.getName() + " "));
        Stream<String> executors = route.getExecutors().stream()
            .filter(executor -> isVisible(input, executor))
            .map(executor -> {
                String prefix = base;
                if (executor.getArguments().isEmpty()) {
                    prefix = base.substring(0, base.length() - 1);// 如果没有参数 那就应该移除掉最后的一个空格字符
                }
                return prefix + generateExecutor(input, executor);
            });
        return Stream.concat(executors, children);
    }
    @Override
    protected String generateExecutor(SchematicInput<CommandSender> input, CommandExecutor<CommandSender> executor) {
        String string = executor.getArguments().stream()
            .map(argument -> String.format(generateArgumentFormat(input, argument), generateArgumentName(input, argument)))
            .collect(Collectors.joining(" "));
        StringBuilder sb = new StringBuilder();
        List<String> desc = executor.meta().get(Meta.DESCRIPTION);// 获取该指令的介绍信息
        if (!desc.isEmpty()) {
            sb.append(" # ");
            sb.append(String.join(", ", desc));
        }
        return string + sb;// 返回例如: example test <num> # 该指令的简介信息
    }
}
```

<!-- tabs:end -->

##### 方案一 自定义消息风格

```java
// 返回的object内容将被传递resultHandler处理
builder.message(LiteMessages.INVALID_USAGE, (Message<Object, InvalidUsage<?>>) invalidUsage -> {
    InvalidUsage.Cause cause1 = invalidUsage.getCause();
    String reason1;
    switch (cause1) {
        case UNKNOWN_COMMAND -> {
            reason1 = "未知指令";
            break;
        }
        case INVALID_ARGUMENT -> {
            reason1 = "错误的参数";
            break;
        }
        case MISSING_ARGUMENT -> {
            reason1 = "需要参数";
            break;
        }
        case MISSING_PART_OF_ARGUMENT -> {
            reason1 = "参数缺失";
            break;
        }
        case TOO_MANY_ARGUMENTS -> {
            reason1 = "参数过多";
            break;
        }
        default -> {
            reason1 = "未知错误";
            break;
        }
    }
    StringBuilder sb = new StringBuilder();
    sb.append(reason1).append('\n');
    for (String schema : invalidUsage.getSchematic().all()) {
        sb.append(schema).append('\n');
    }
    return sb.toString();
})
```

##### 方案二 处理器风格

<!-- tabs:start -->

#### **Main.java**

```java
LiteKookFactory.builder(plugin)
    .invalidUsage(new ExampleInvalidUsageHandler())
```

#### **ExampleInvalidUsageHandler.java**

```java
public class ExampleInvalidUsageHandler implements InvalidUsageHandler<CommandSender> {
    @Override
    public void handle(Invocation<CommandSender> invocation, InvalidUsage<CommandSender> result, ResultHandlerChain<CommandSender> chain) {
Schematic schematic = result.getSchematic();

        InvalidUsage.Cause cause = result.getCause();
        String reason;
        switch (cause) {
            case UNKNOWN_COMMAND -> {
                reason = "未知指令";
                break;
            }
            case INVALID_ARGUMENT -> {
                reason = "错误的参数";
                break;
            }
            case MISSING_ARGUMENT -> {
                reason = "需要参数";
                break;
            }
            case MISSING_PART_OF_ARGUMENT -> {
                reason = "参数缺失";
                break;
            }
            case TOO_MANY_ARGUMENTS -> {
                reason = "参数过多";
                break;
            }
            default -> {
                reason = "未知错误";
                break;
            }
        }
        chain.resolve(invocation, reason);// 执行result的结果处理 例如sender.sendMessage(reason) 或者 message.reply(reason)
        schematic.all().forEach((it) -> {
            chain.resolve(invocation, it); // 把每一条指令的概述也传递给resultHandler 
        });
    }
}
```

<!-- tabs:end -->

