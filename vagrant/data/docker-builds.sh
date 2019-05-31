#!/bin/bash
dir=$(pwd)

rm -rf /project
mkdir -p /project
cd /project

project="big-data-analysis"
git clone https://github.com/aysenuroruc/$project.git

cd /project/$project/log-creator
chmod +x gradlew
dos2unix gradlew
./gradlew build -x test

docker build . --no-cache -t aysenuroruc/log-creator:0.0.1
docker push aysenuroruc/log-creator:0.0.1

cd /project/$project/log-consumer
chmod +x gradlew
dos2unix gradlew
./gradlew build -x test

docker build . --no-cache -t aysenuroruc/log-consumer:0.0.1
docker push aysenuroruc/log-consumer:0.0.1

cd /project/$project/log-watcher
chmod +x gradlew
dos2unix gradlew
./gradlew build -x test

docker build . --no-cache -t aysenuroruc/log-watcher:0.0.1
docker push aysenuroruc/log-watcher:0.0.1

cd $dir
rm -rf /project