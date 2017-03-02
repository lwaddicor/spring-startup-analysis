# Spring Start Analyser

This project is designed as a module which you can bring in via maven
and it will produce a graph similar to below which shows the
construction times of each spring bean.

![Example image of it running](./readme-images/example.png)

## How to use

Import the module as a maven dependency.

    <dependency>
        <groupId>com.github.lwaddicor</groupId>
        <artifactId>spring-startup-analysis</artifactId>
        <version>1.1.0</version>
    </dependency>

Either add to your component scanning the following base package

    com.github.lwaddicor.springstartupanalysis

 or

    <bean class="com.github.lwaddicor.springstartupanalysis.web.SpringStartupController"/>
    <bean class="com.github.lwaddicor.springstartupanalysis.StartProgressBeanPostProcessor"/>

Now you should be able to start up your servlet and navigate to the endpoint. For example

    http://localhost:8080/spring-startup/

And view something very similar to the example.

### Credits

TreeMap algorithm from [peterdmv/treemap](https://github.com/peterdmv/treemap) was used to calculate the treemap.