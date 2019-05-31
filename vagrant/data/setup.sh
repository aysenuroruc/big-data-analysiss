#!/bin/bash
dir=$(pwd)

rm -rf /project
mkdir -p /project
cd /project


git clone https://github.com/wurstmeister/kafka-docker.git
cd kafka-docker
sed -i s/192.168.99.100/192.168.3.2/g docker-compose-single-broker.yml
docker-compose -f docker-compose-single-broker.yml down
docker-compose -f docker-compose-single-broker.yml up -d

docker stop log-creator && docker rm -v log-creator
docker stop log-watcher && docker rm -v log-watcher
docker stop log-consumer && docker rm -v log-consumer
docker stop prometheus && docker rm -v prometheus
docker run -d -v /project/raw/:/raw -e RAW_PATH=/raw --name log-creator --memory=256m aysenuroruc/log-creator:0.0.1
docker run -d -v /project/raw/:/raw -e RAW_PATH=/raw --name log-watcher --memory=256m aysenuroruc/log-watcher:0.0.1
docker run -d --name log-consumer -p 8080:8080 --memory=256m aysenuroruc/log-consumer:0.0.1

cp /vagrant_data/prometheus.yml /project
docker run -d -p 9090:9090 -v /project/prometheus.yml:/etc/prometheus/prometheus.yml --name prometheus prom/prometheus

cd $dir