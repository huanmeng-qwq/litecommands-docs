# 定义参数

```java
@Excute
public void execute(@Arg int number) {
    System.out.println(number);
}
```

# @Arg

该注解用来声明方法参数为一个指令参数，如果方法内参数不标注该注解将无法被正确解析注册

##### 指定参数名

```java

@Excute
public void execute(@Arg("num") int number) {
    System.out.println(number);
}
```

| 参数    | 作用       |
|-------|----------|
| value | 表示该参数的名字 |

默认不设置时将通过反射获取参数的名字
> 一般情况下编译后的代码，参数名字会被替换成自动生成的
>
> 这种情况需要设置构建系统的参数
>
> 以下是主流构建系统的设置示例

<!-- tabs:start -->

#### **build.gradle**

```groovy
withType(JavaCompile) {
    options.compilerArgs << "-parmeters"
}
```

#### **build.gradle.kts**

```kotlin
tasks.compileJava {
    options.compilerArgs.add("-parmeters")
}
```

#### **pom.xml**

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>省略，自行修改</version>
    <configuration>
        <source>...</source>
        <target>...</target>
        <compilerArgs>
            <arg>-parameters</arg>
        </compilerArgs>
    </configuration>
</plugin>
```

<!-- tabs:end -->

# 类型支持

LiteCommands内置的

* int/Integer
* double/Double
* float/Float
* short/Short
* long/Long
* BigInteger
* UUID
* 等Java常用类型

KookBC提供的

* User
* Guild
* Channel
* NonCategoryChannel
* TextChannel
* VoiceChannel
* Role
* CustomEmoji