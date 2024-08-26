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

@Execute(name = "example")
public String execute() {
    return "example";    
}
```

#### 参数

| 参数   | 作用                   |
|------|----------------------|
| name | 表示子命令的名称<br/>多级用空格分割 |
