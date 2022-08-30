FROM openjdk:8-alpine
EXPOSE 8081 8081
WORKDIR /usr/src/app
COPY *.jar /usr/src/app/
CMD ["java","-Xmx1G", "-Xms1G", "-XX:+UseG1GC", "-jar", "*.jar"]
