#!/bin/bash
dir=$(pwd)

rm -rf /project
mkdir -p /project
cd /project

cp /vagrant_data/docker-compose.yml /project


cd $dir