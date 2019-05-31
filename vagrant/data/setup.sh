#!/bin/bash
rm -rf /project
mkdir -p /project
cd /project

project="big-data-analysis"
git clone https://github.com/aysenuroruc/$project.git
dir=$(pwd)

cd $dir/$project/log-creator
chmod +x gradlew
./gradlew build -x test

docker build . -t log-creator:0.0.1

cd $dir/$project/log-consumer
chmod +x gradlew
./gradlew build -x test

docker build . -t log-consumer:0.0.1

cd $dir/$project/log-watcher
chmod +x gradlew
./gradlew build -x test

docker build . -t log-watcher:0.0.1

cd $dir