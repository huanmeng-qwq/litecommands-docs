# @Execute

> 这是一个核心注解
>
> 在扫描类的时候如果方法带有该注解
>
> 才会将该方法作为一个命令函数处理下一步操作

#### 示例

```java
@Execute
public String execute() {
    return "execute();";    
}
```
