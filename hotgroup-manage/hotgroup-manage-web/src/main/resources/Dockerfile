FROM openjdk:8-alpine
EXPOSE 8091 8091
WORKDIR /usr/src/app
COPY *.jar /usr/src/app/
CMD ["java","-Xmx1G", "-Xms1G", "-XX:+UseG1GC", "-jar", "*.jar"]
