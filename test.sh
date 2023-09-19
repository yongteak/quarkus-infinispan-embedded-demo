#!/bin/bash

./gradlew clean quarkusBuild -Dquarkus.container-image.group=renegrob -Dquarkus.container-image.build=true -Dquarkus.container-image.labels.SHOULD_STOP=true

BACK_PID=$!
wait $BACK_PID

docker network create test

docker run -p8080:8080 --net=test -d renegrob/infinispan-embedded-demo:1.0.0-SNAPSHOT
docker run -p8081:8080 --net=test -d renegrob/infinispan-embedded-demo:1.0.0-SNAPSHOT
docker run -p8082:8080 --net=test -d renegrob/infinispan-embedded-demo:1.0.0-SNAPSHOT

sleep 10s

curl -s http://localhost:8080/cache/test1 | jq
curl -s http://localhost:8080/cache/test2 | jq
curl -s http://localhost:8080/cache/test3 | jq
curl -s http://localhost:8080/cache/test4 | jq

echo ---

curl -s http://localhost:8080/cache/testA | jq &
curl -s http://localhost:8081/cache/testA | jq &
curl -s http://localhost:8082/cache/testA | jq &

wait

echo ---

echo ---

curl -s http://localhost:8080/cache/TestB | jq &
curl -s http://localhost:8081/cache/TestB | jq &
curl -s http://localhost:8082/cache/TestB | jq &

wait

echo ---

docker stop $(docker ps --filter=label='SHOULD_STOP=true' -q)