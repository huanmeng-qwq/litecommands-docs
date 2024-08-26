# 命令执行步骤

> `example lite <text>`

- 在输入会首先平台会将指令内容传递到LiteCommands中
- LiteCommands会先通过`ParseableInput`解析整个命令文本
- 再创建`InvocationContext`设置平台给定的`Context`内容
    - 也就是经常用到的`@Context`支持的类型，会在这一步设置
- 然后创建整个执行的上下文 `Invocation`，它包含了此次执行的命令名字，使用的别名(label)，上面解析出来的`ParseableInput`，执行者
- 然后LiteCommands会通过`ParseableInput`来查找命令路由(`CommandRoute`)，这一步仅找到子命令级别
- 接下来是该路由下的所有执行器(`CommandExecutor`)，可以理解为我们正常写的方法体
- 这一步将会根据输入命令文本来过来执行器
- 找到匹配的执行器后首先会经过一轮轮的验证是否允许执行
    - 首当其冲的是`Validator`接口，会先将所有注册了的`Validator`的对象验证方法执行一遍
        - 执行的过程中如果有一个不通过则直接中断
        - 权限检查就在这一步执行
        - `@Cooldown`接口声明的冷却时间，也会在这一步判断
        - `KookBC`平台的`@Result`接口也会在这里做`ResultType`的设置，它永远会通过
    - 接下来会根据方法的参数类型和注解获取所需要的对象和解析值
    - 执行所有注册的`MethodValidator`接口的对象的`validate`方法
        - 如果这一步被阻止将被中断
    - 然后会根据执行器指定调度器执行方法体

