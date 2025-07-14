# 个人Java开发工具集——Alpha

> 该项目仍处于高活跃更新态，各版本之间差距极大，GA正式版本发布后将稳定

## 版本日志

[版本日志](VERSION.md)

## 使用方法

### 依赖引入

```xml
<dependency>
    <groupId>com.chenjiabao.open</groupId>
    <artifactId>devtools</artifactId>
    <version>${version}</version>
 </dependency>
```

### 邮件工具

在发送验证码时，这很有用

```java
import com.chenjiabao.open.utils.MailUtils;

```

### 接口返回工具

默认200成功，不传参数。

```java
import com.chenjiabao.open.utils.model.ApiResponse;
ApiResponse.builder();
```

### 雪花生成ID算法

构造函数的machineId参数为机器ID，在分布式中这是必须的，且每台机器不一致的，单体应用可随意传值。

```java
import com.chenjiabao.open.utils.SnowflakeUtils;

SnowflakeUtils snowflakeUtils = new SnowflakeUtils(1L);

String id = snowflakeUtils.nextId();
```

你也可以使用Spring Bean注入。
