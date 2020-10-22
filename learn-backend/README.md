# Learning App Backend Service

How to start the Learning App Backend Service application
---

Note: To run the service locally, make sure the value of the `USE_DISTRIBUTED_CACHE` variable in the application class is `false`.

1. Run `mvn clean install` to build your application
2. Start application with `java -jar target/learn-backend-0.0.1-SNAPSHOT.jar server config.yml`
3. To check that your application is running enter url `http://localhost:8080`

