FROM openjdk:17-jdk
WORKDIR /opt
ENV PORT 8080
EXPOSE 7210
COPY target/*.jar /opt/app.jar
ENTRYPOINT exec java $JAVA_OPTS -jar app.jar

# sudo docker build -t umc:0.0.1 .
# sudo docker compose up
# sudo docker run -it umc:v2
# docker run --name saghnash/mysql:v2 -p 3306:3306 -e MYSQL_ROOT_PASSWORD=root -d  mysql:8.0.27
# docker volume create volume-name
#sudo docker run -it -e MYSQL_ROOT_PASSWORD=root saghnash/mysql:v2
# mvn clean package dskiptests
