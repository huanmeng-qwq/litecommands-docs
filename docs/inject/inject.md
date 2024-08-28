# @Inject

> 在命令构造函数中使用
>
> 在通过`dev.rollczi.litecommands.LiteCommandsBuilder#commands(java.lang.Object...)`方法注册命令时
>
> 如果传递了class，将自动寻找该class包含`@Inject`注解的构造函数来创建对象
> 
> 还可以自动注入带有`@Bind`的方法参数
>
> 如果该class有空构造函数可以不标注该注解
>
> 自动传递的`bind`声明的内容
> 
> 类似于web开发中 `@Autowired` 或 `@Resource` `自动注入`

#### 添加命令依赖

```java
builder.bind(UserService.class, () -> userService);
```

还可以用其它重载方法添加:

- `dev.rollczi.litecommands.LiteCommandsBuilder#bind(java.lang.Class<T>, dev.rollczi.litecommands.bind.BindProvider<T>)`
- `dev.rollczi.litecommands.LiteCommandsBuilder#bindUnsafe`

#### 示例

<!-- tabs:start -->

#### **UserCommand.java**

```java
public class UserCommand {
    private final UserService service;
    
    @Inject
    public ExampleCommand(UserService service) {
        this.service = service;
    }
    //或者不使用构造器注入
    @Execute(name = "bind")
    public void example(
            @Bind UserService service
    ) {
        //service operation
    }
}
```

#### **Main.java**

```java
LiteKookFactory
        .builder(this)
        .bind(UserService.class, UserService::new)
        .commands(UserCommand.class)
```

<!-- tabs:end -->