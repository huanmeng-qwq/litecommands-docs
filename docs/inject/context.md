# @Context

> 将在执行方法时自动传递带有该注解的参数
>
> 自动传递的对象存放在`Invocation#context`对象里面
>
> 该对象仅平台实现的时候指定内容，开发者无法自行修改

#### 示例

```java
@Execute
public void execute(@Context CommandSender sender) {
    sender.sendMessage("你的名字是: " + sender.getName());
}
```

#### 内置

不同平台默认内置了不同的内容

但是它们都会内置一个SENDER，这取决于你用的哪个平台

| 平台                  | class                                            |
|---------------------|--------------------------------------------------|
| Bukkit/Spigot/Paper | org.bukkit.command.CommandSender                 |
| BungeeCord          | net.md_5.bungee.api.CommandSender                |
| Fabric              | net.minecraft.server.command.ServerCommandSource |
| JDA                 | net.dv8tion.jda.api.entities.User                |
| KookBC              | snw.jkook.command.CommandSender                  |
| Minestom            | net.minestom.server.command.CommandSender        |
| Sponge              | org.spongepowered.api.command.CommandCause       |
| Velocity            | com.velocitypowered.api.command.CommandSource    |

#### 类型判断

如果类型没有注册时

该注解将判断子父类之间的关系自动处理
