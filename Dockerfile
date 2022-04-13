FROM maven:3.8.5-openjdk-17 AS maven_build
WORKDIR /app
COPY . /app
RUN mvn -f /app/pom.xml package

FROM tomcat:10.0.20-jdk17-openjdk-bullseye
COPY --from=maven_build ./app/target/*.war $CATALINA_HOME/webapps/auctionsite.war
EXPOSE 8888
CMD [ "catalina.sh", "run" ]
