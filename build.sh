#!/bin/bash

echo "remove old jar file"
cd target/
rm -rf server-0.0.1-SNAPSHOT.jar
rm -rf server-0.0.1-SNAPSHOT.jar.original

echo "build new jar file"
cd ../ && ./mvnw clean package -DskipTests

echo "run server"
cd target/
nohup java -jar server-0.0.1-SNAPSHOT.jar &
