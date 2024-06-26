---
formatter: "@formatter:off"
layout: post 
title: Start Service
subtitle: start-with-service 
description: start-with-service 
tags: []
menus:
    - quick-start
    - start-service 
date: 2020-11-27 23:21:17 +800 
version: 1.0
formatter: "@formatter:on"
---

# Start Service

## What

`fianl-service` define an abstract service of `AbsService` like `AbsMapper` in `final-mybatis`.

## How

### Import Dependency

* maven

```xml

<dependencies>
    <dependency>
        <groupId>org.ifinalframework.starter</groupId>
        <artifactId>final-boot-starter-service</artifactId>
        <version>1.2.1</version>
    </dependency>
    <dependency>
        <groupId>org.ifinalframework.auto</groupId>
        <artifactId>final-auto-service</artifactId>
        <version>1.2.1</version>
        <optional>true</optional>
    </dependency>
</dependencies>
```

### Define Service

Define the services of `PersonService` and `PersonServiceImpl` with `AbsService`.

* PersonService

```java
public interface PersonService extends AbsService<Long, Person, PersonMapper> {

}
```

* PersonServiceImpl

```java
class PersonServiceImpl extends AbsServiceImpl<Long, Person, PersonMapper> implements PersonService {

    public PersonServiceImpl(PersonMapper repository) {
        super(repository);
    }

}
```

### Use Auto Service

#### Import Auto Service

* maven

```xml

<dependency>
    <groupId>org.ifinalframework.auto</groupId>
    <artifactId>final-auto-service</artifactId>
    <version>1.2.1</version>
    <optional>true</optional>
</dependency>
```

> The service and implements could be generated by `final-auto-service`.