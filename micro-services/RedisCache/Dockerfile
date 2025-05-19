# this is base image
FROM openjdk:21

WORKDIR /usr/src/myapp

# Copy the JAR file into the container
COPY target/RedisCache-0.0.1-SNAPSHOT.jar app.jar

# Run the Spring Boot app
CMD ["java", "-jar", "app.jar"]

EXPOSE 9001
#docker build -t redis-springboot-app .
#
#docker run  -p 9001:9001 redis-springboot-app


#FROM openjdk:21
#
#WORKDIR /usr/src/myapp
#
## Create the package directory
#RUN mkdir -p com/redisCache
#
## Copy the Java file into the correct package folder
#COPY src/main/java/com/redisCache/Test.java com/redisCache/Test.java
#
## Compile the Java file
#RUN javac com/redisCache/Test.java
#
## Run the class using its fully qualified name
#CMD ["java", "com.redisCache.Test"]

#docker build -t myjavaimage .
#docker run --rm --name javaProject1 myjavaimage
#| Part                  | Meaning                                                              |
#| --------------------- | -------------------------------------------------------------------- |
#| `docker run`          | Command to start a new container from an image.                      |
#| `--rm`                | Automatically **removes the container** after it stops.              |
#| `--name javaProject1` | Assigns the container a custom name: `javaProject1`.                 |
#| `myjavaimage`         | The name of the Docker **image** to use when starting the container. |


# this is author name
#FROM ubuntu
#MAINTAINER rishabh
## update command
#RUN apt update
## start up executable command
#CMD ["echo","this is my first image"]

#// docker build -t myUbuntuImage
#
#// docker run --name myUbuntuImage -it

