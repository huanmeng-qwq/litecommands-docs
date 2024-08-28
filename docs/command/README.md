## @Command

> 这是一个核心注解
>
> 在扫描类的时候如果class带有该注解
>
> 才会将该方法作为一个命令体处理下一步操作

### 示例

```java
@Command(name = "example")
public class ExampleCommand {
    // ....
}



```

### 参数

| 参数   | 作用                   |
|------|----------------------|
| name | 表示子命令的名称<br/>多级用空格分割 |

## @RootCommand

> 该注解将会把所有包含`@Execute`注解的方法
>
> 注册为根命令

### 示例

```java
@RootCommand
public RootCommands {
    @Execute(name = "example")
    public void example(@Arg("arg") String arg) {
        //   /example 123
        //   arg = 123
        
    }
    
    @Execute(name = "liteTest print")
    public void example(@OptionalArg("arg") String arg) {
        //   /liteTest print
        //   arg = null
    }
}

```

## @Shortcut

> 该注解可以用在`@Execute`注解的方法当中，用于给原本的命令起一个“短别名”


### 示例

```java
@Command(name = "example")
public class ExampleCommand {
    
    @Execute(name = "help")
    @Shortcut("help me")
    public void Help(){}
}
```

此处的命令既可以是 `/example help` 也可以是 `/help me`