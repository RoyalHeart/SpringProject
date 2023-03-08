# Change from jar to war to deploy on jboss

First go to the pom.xml file

Then change the packaging tag from jar to war

```xml
    <packaging>jar</packaging>
    <packaging>war</packaging>
```

Then move to the dependency with

```xml
<artifactId>spring-boot-starter-web</artifactId>
```

and add the
exclusions tag:

```xml
    <exclusions>
        <exclusion>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-tomcat</artifactId>
        </exclusion>
    </exclusions>
```

Then run in the terminal

```shell
mvn clean install
```

to clean target folder and generate the war file and go to jboss server to add deployment
