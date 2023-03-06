# Change from jar to war

First go to the pom.xml file

Then change the packaging tag from jar to war

```
    <packaging>jar</packaging>
    <packaging>war</packaging>
```

Then move to the dependency with artifactId spring-boot-starter-web and add the
exclusions tag like

```
    <exclusions>
        <exclusion>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-tomcat</artifactId>
        </exclusion>
    </exclusions>
```
