# final-frameworks

[![GitHub Workflow Status](https://img.shields.io/github/workflow/status/likly/final-frameworks/CI)](https://github.com/likly/final-frameworks/actions?query=workflow%3ACI)
[![GitHub](https://img.shields.io/github/license/likly/final-frameworks)](http://www.apache.org/licenses/LICENSE-2.0.html)
[![Maven Central](https://img.shields.io/maven-central/v/org.ifinal.finalframework.frameworks/final-frameworks?label=maven&color=success)](https://mvnrepository.com/search?q=org.ifinal.finalframework)
![Sonatype Nexus (Releases)](https://img.shields.io/nexus/r/org.ifinal.finalframework.frameworks/final-frameworks?server=https%3A%2F%2Foss.sonatype.org%2F)
![Sonatype Nexus (Releases)](https://img.shields.io/nexus/s/org.ifinal.finalframework.frameworks/final-frameworks?server=https%3A%2F%2Foss.sonatype.org%2F)
![GitHub Repo stars](https://img.shields.io/github/stars/likly/final-frameworks)
![GitHub top language](https://img.shields.io/github/languages/top/likly/final-frameworks)
[![GitHub language count](https://img.shields.io/github/languages/count/likly/final-frameworks)](https://github.com/likly/final-frameworks)
[![Sonar Quality Gate](https://img.shields.io/sonar/alert_status/likly_final-frameworks?server=https%3A%2F%2Fsonarcloud.io)](https://scrutinizer-ci.com/g/likly/final-frameworks/)
![Sonar Coverage](https://img.shields.io/sonar/coverage/likly_final-frameworks?server=https%3A%2F%2Fsonarcloud.io)

## What

`final-frameworks`致力于提供简单、灵活且功能强大的`java`开发脚手架。

## Quick Start

* [Define Entity with Annotation](docs/quick-start/define-entity-with-annotation.md)：使用`@Annotation`
  定义实体。
* [Operating CRUD with Mapper](docs/quick-start/operating-crud-with-mapper.md)：使用`Mapper`操作`CRUD`。
* [Query base on Annotation](docs/quick-start/query-base-on-annotation.md): 使用基于`@Annotation`
  的`Query`。
* [Start with Service](docs/quick-start/start-with-service.md)：从`Service`开始。
* [Define Rest Api in Controller](docs/quick-start/define-rest-api-in-controller.md): 定义`RestApi`。
* [Sharding with annotation](docs/quick-start/sharding-with-annotation.md)：基于`@Annotation`的分库分表。

## Features

* [Readable Json](docs/features/readable-json.md)：对日期、枚举等数据类型进行序列化增强，提高`JSON`可读性。
* [Global Exception Handler](docs/features/global-exception-handler.md): 全局异常处理。
* [Global Result Wrapper](docs/features/global-result-wrapper.md): 全局结果集封装。


* [通用的CURD](docs/crud.md)：定义通用的CURD方法，统一数据的持久化。
* [简明的查询](docs/query.md)：基于注解的声明式查询，使查询简单明了。
* [强大的WEB](docs/web.md)
  * [统一的结果集](docs/web.md#统一的结果集): 对`@RestController`的结果进行统一拦截封装
  * [全局异常处理](docs/web.md#全局异常处理)：对系统中抛出的`Exception`进行全局的拦截处理，转化为统一的结果集。
  * [TokenAuth认证机制](docs/auth.md)：简化的权限认证机制。
* [极简的监控](docs/monitor.md)
  * [简单的操作日志](docs/monitor.md#简化的操作日志)
* Devops
  * [热更新](docs/hotswap.md)

## Modules

* [`final-annotation`](final-annotation/README.md): 定义基本的`Annotation`和`接口`。
* [`final-framework`](final-framework/README.md): 对常用技术进行封装及增强，如`spring`、`myabtis`、`json`等。
* [`final-data`](final-data/README.md): `mybatis`、`redis`、`sharding-sphere`、`cache`等。
* [`final-auto`](final-auto/README.md)：基于`APT`自动化生成配置文件及模板代码，提升开发效率。
* [`final-boot`](final-boot/README.md): 基于`Spring Boot`，提供开箱即用的`starters`。

## Developer's Guide

* [日志规范](docs/code-rules/logger.md) : 日志规范
* [Check Style](https://github.com/likly/checkstyle) : 代码规范

## Dependencies

### Core

|                          Dependency                          |       Description        |                           Version                            |
| :----------------------------------------------------------: | :----------------------: | :----------------------------------------------------------: |
|       [Lombok](https://github.com/rzwitserloot/lombok)       |     简化对象封装工具     | ![Maven Central](https://img.shields.io/maven-central/v/org.projectlombok/lombok?label=1.8.16) |
|    [Spring Boot](https://spring.io/projects/spring-boot)     |   Spring容器及MVC框架    | [![Maven Central](https://img.shields.io/maven-central/v/org.springframework.boot/spring-boot-starter-parent?label=2.3.3-RELEASE)](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot) |
|    [Mybatis](https://mybatis.org/mybatis-3/zh/index.html)    |         ORM框架          | ![Maven Central](https://img.shields.io/maven-central/v/org.mybatis/mybatis?label=3.5.6) |
|           [Dubbo](http://dubbo.apache.org/zh-cn/)            |      分布式RPC调用       | ![Maven Central](https://img.shields.io/maven-central/v/org.apache.dubbo/dubbo-spring-boot-starter?label=2.7.8) |
| [ShardingSphere](https://shardingsphere.apache.org/document/current/cn/overview/) |       分库分表组件       | ![Maven Central](https://img.shields.io/maven-central/v/org.apache.shardingsphere/shardingsphere-jdbc-core?label=5.0.0-alpha) |
|                          Zookeeper                           | 分布式注册中心、配置中心 |                                                              |
|                                                              |                          |                                                              |

### Plugins

|                          Dependency                          |       Description        |                           Version                            |
| :----------------------------------------------------------: | :----------------------: | :----------------------------------------------------------: |
| [PageHelper](https://github.com/pagehelper/Mybatis-PageHelper) |     Mybatis分页插件      | ![Maven Central](https://img.shields.io/maven-central/v/com.github.pagehelper/pagehelper?label=5.2.0) |
|        [javapoet](https://github.com/square/javapoet)        |      Java源代码生成      | ![Maven Central](https://img.shields.io/maven-central/v/com.squareup/javapoet?label=1.13.0) |
|                        Velocity-Core                         |                          | ![Maven Central](https://img.shields.io/maven-central/v/org.apache.velocity/velocity-engine-core?label=2.1) |
|                        Velocity-Tools                        |                          | ![Maven Central](https://img.shields.io/maven-central/v/org.apache.velocity.tools/velocity-tools-generic?label=3.0) |
|                                                              |                          |                                                              |
|                                                              |                          |                                                              |
|                                                              |                          |                                                              |
|                                                              |                          |                                                              |
|                                                              |                          |                                                              |
|                                                              |                          |                                                              |

### Test

|                          Dependency                          |       Description        |                           Version                            |
| :----------------------------------------------------------: | :----------------------: | :----------------------------------------------------------: |
|                          H2Database                          |        内存数据库        | ![Maven Central](https://img.shields.io/maven-central/v/com.h2database/h2?label=1.4.200) |
|                                                              |                          |                                                              |
|                                                              |                          |                                                              |
|                                                              |                          |                                                              |
|                                                              |                          |                                                              |
|                                                              |                          |                                                              |
|                                                              |                          |                                                              |

## Contact

欢迎有技术情怀的同学一起成长！

* <a href="mailto:likly@ilikly.com?subject=Concat from github">MailTO:likly@ilikly.com</a>

## Thanks

* 感谢<a href="https://www.jetbrains.com/"><img src="https://www.jetbrains.com/apple-touch-icon.png" width="64" height="64">
  jetbrains</a>提供的免费授权。
