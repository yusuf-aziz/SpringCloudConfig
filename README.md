# Spring Cloud Config with MongoDB
## Configure Server

Spring Cloud Config Server MongoDB enables seamless integration of the regular Spring Cloud Config Server with MongoDB to manage external properties for applications across all environments.

Create a standard Spring Boot application, like this:
```
package com.github.yusuf.config.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.mongodb.EnableMongoConfigServer;

@SpringBootApplication
@EnableMongoConfigServer
public class ServerConfigApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServerConfigApplication.class, args);
	}
}
```
Configure the application's spring.data.mongodb.* properties in application.yml, like this:
```
spring:
  data:
    mongodb:
      uri: mongodb://localhost/config-db
```
Add some documents to the config-db mongo database, like this:
```
db.userservice.insert({   
    "label" : "india",
    "profile" : "dev",
    "source" : {
        "user" : {
            "name" : "Hiba",
            "city" : "Delhi",
            "language" : "English",
            "state" : "Delhi"
        }
    }
});

db.userservice.insert({
    "label" : "india",
    "profile" : "prod",
    "source" : {
        "user" : {
            "name" : "Yusuf",
            "city" : "Jaipur",
            "language" : "Hindi",
            "state" : "Rajasthan"
        }
    }
});
```
The application-name is identified by the collection's name and a MongoDB document's profile and label values represent the Spring application's profile and label respectively. Note that documents with no profile or label values will have them considered default. All properties must be listed under the source key of the document.

## Configure Client

Create a standard Spring Boot application, like this:
```
package com.github.yusuf.config.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class ConfigClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfigClientApplication.class, args);
	}
}

@RefreshScope
@RestController
class UserRestController {

	@Value("${user.name}")
	private String name;

	@Value("${user.city}")
	private String city;

	@Value("${user.language}")
	private String language;

	@Value("${user.state}")
	private String state;

	@RequestMapping("/getUserDetails")
	String getUserDetails() {
		return "Name :" + name + "<p>City :" + city + "<p>State :" + state + "<p>Language :" + language;
	}
}
```
Property fields value will be injected from the config server on the basis of **profile** and **label**

Add bootstrap.properties in the resources
```
spring.cloud.config.uri:http://localhost:9004
spring.application.name:userservice
spring.cloud.config.failFast:true
spring.cloud.config.profile:dev
spring.cloud.config.label:india
```
This is the default behaviour for any application which has the Spring Cloud Config Client on the classpath. When a config client starts up it binds to the Config Server (via the bootstrap configuration property spring.cloud.config.uri) and initializes Spring Environment with remote property sources.

The net result of this is that all client apps that want to consume the Config Server need a bootstrap.properties (or an environment variable) with the server address in spring.cloud.config.uri

## Get the user details

Open the http://localhost:9005/getUserDetails url in the browser and you will get user information on the basis of provided value for the profile and label in the bootstrap.properties

```
City :Delhi

State :Delhi

Language :English

Name :Hiba
```

Now, change the profile in the bootstrap.properties from **dev** to **prod** and you will get the different result
```
City :Jaipur

State :Rajasthan

Language :Hindi

Name :Yusuf
```
