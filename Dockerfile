FROM java:8-alpine
MAINTAINER Your Name <you@example.com>

ADD target/uberjar/poytikset.jar /poytikset/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/poytikset/app.jar"]
