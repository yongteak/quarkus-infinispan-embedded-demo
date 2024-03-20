#!/bin/bash

# 기본 패키지 업데이트
sudo apt update

# 기본 빌드 도구 설치
sudo apt install -y build-essential curl git maven zlib1g-dev

# GraalVM 설치
sudo mkdir -p /usr/local/graalvm
sudo curl -L https://github.com/graalvm/graalvm-ce-builds/releases/download/vm-22.3.1/graalvm-ce-java17-linux-amd64-22.3.1.tar.gz | sudo tar -xz -C /usr/local/graalvm
sudo chown -R $USER:$USER /usr/local/graalvm/


# GraalVM 환경 변수 설정
echo 'export PATH=/usr/local/graalvm/graalvm-ce-java17-22.3.1/bin:$PATH' >> ~/.bashrc
echo 'export JAVA_HOME=/usr/local/graalvm/graalvm-ce-java17-22.3.1' >> ~/.bashrc

# 변경 사항 적용
source ~/.bashrc

# Native Image 설치
gu install native-image

# btm 설치
curl -LO https://github.com/ClementTsang/bottom/releases/download/0.9.6/bottom_0.9.6_amd64.deb
sudo dpkg -i bottom_0.9.6_amd64.deb

# Quarkus CLI 설치 (jbang)
curl -Ls https://sh.jbang.dev | bash -s - trust add https://repo1.maven.org/maven2/io/quarkus/quarkus-cli/
curl -Ls https://sh.jbang.dev | bash -s - app install --fresh --force quarkus@quarkusio
source ~/.bashrc

# JGroups GossipRouter
# wget https://repo1.maven.org/maven2/org/jgroups/jgroups/4.2.27.Final/jgroups-4.2.27.Final.jar
# java -cp jgroups-4.2.27.Final.jar org.jgroups.stack.GossipRouter -port 12001