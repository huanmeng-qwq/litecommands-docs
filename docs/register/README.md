# 注册

##### Factory

`Factory`由各个平台实现提供

| 平台                  | Factory                                               |
|---------------------|-------------------------------------------------------|
| Bukkit/Spigot/Paper | dev.rollczi.litecommands.bukkit.LiteBukkitFactory     |
| BungeeCord          | dev.rollczi.litecommands.bungee.LiteBungeeFactory     |
| Fabric              | dev.rollczi.litecommands.fabric.LiteFabricFactory     |
| JDA                 | dev.rollczi.litecommands.jda.LiteJDAFactory           |
| KookBC              | snw.kookbc.impl.command.litecommands.LiteKookFactory  |
| Minestom            | dev.rollczi.litecommands.minestom.LiteMinestomFactory |
| Sponge              | dev.rollczi.litecommands.sponge.LiteSpongeFactory     |
| Velocity            | dev.rollczi.litecommands.velocity.LiteVelocityFactory |

##### 指令

```java
Factory.builder(plugin)
    .commands(LiteCommand.class, new ExampleCommands());// 你要注册的指令，可以传class或者对象，如果传class请注意查看 TODO
```

##### 参数解析器

解析器(ArgumentResolver)包括了 [Suggester](#参数提示)(参数提示,Tab补全)和[Parser](#参数解析)(参数解析)

```java
Factory.builder(plugin)
    .argument(User.class, new UserArgumentResovler());
```

##### 参数提示

```java
Factory.builder(plugin)
    .argumentSuggester(User.class, new UserSuggester());
```

##### 参数解析

```java
Factory.builder(plugin)
    .argumentParser(User.class, new UserParser());
```

