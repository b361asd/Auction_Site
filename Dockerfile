FROM maven:3.8.4-openjdk-11-slim AS maven_build
WORKDIR /app
COPY . /app
RUN mvn -f /app/pom.xml package

FROM tomcat:10.1.0-jdk11-openjdk-slim-bullseye
COPY --from=maven_build ./app/target/*.war $CATALINA_HOME/webapps/auctionsite.war
EXPOSE 8888
CMD [ "catalina.sh", "run" ]
