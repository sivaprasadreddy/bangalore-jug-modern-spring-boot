# Modern Spring Boot Application Development

## Run application

```shell
$ ./mvnw spring-boot:test-run
```

## Run application using Docker Compose in JVM mode

```shell
# Using Buildpacks 
$ ./mvnw spring-boot:build-image -DskipTests
# Using Dockerfile
$ docker build -t sivaprasadreddy/modern-spring-boot .

$ docker compose -f complete.yml up --build -d
$ docker compose -f complete.yml logs -f
```

## Run application using Docker Compose in GraalVM Native mode

```shell
$ ./mvnw -Pnative spring-boot:build-image
$ docker compose -f complete.yml up --build -d
$ docker compose -f complete.yml logs -f
$ curl http://localhost:8080/api/bookmarks | jq .
```
