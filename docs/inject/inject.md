# @Inject

> 在命令构造函数中使用
>
> 在通过`dev.rollczi.litecommands.LiteCommandsBuilder#commands(java.lang.Object...)`方法注册命令时
>
> 如果传递了class，将自动寻找该class包含`@Inject`注解的构造函数来创建对象
>
> 如果该class有空构造函数可以不标注该注解
>
> 自动传递的`bind`声明的内容

#### 添加内容

```java
builder.bind(UserService.class, ()-> userService);
```

还可以用其它重载方法添加:

- `dev.rollczi.litecommands.LiteCommandsBuilder#bind(java.lang.Class<T>, dev.rollczi.litecommands.bind.BindProvider<T>)`
- `dev.rollczi.litecommands.LiteCommandsBuilder#bindUnsafe`

#### 示例

```java
public class UserCommand {
    private final UserService service;
    
    @Inject
    public ExampleCommand(Server service) {
        this.service = service;
    }

    // ...
}
```