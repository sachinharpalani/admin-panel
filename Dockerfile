FROM java:8-alpine
MAINTAINER Your Name <you@example.com>

ADD target/uberjar/admin-panel.jar /admin-panel/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/admin-panel/app.jar"]
