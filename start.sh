#!/bin/bash

# Check if an argument is provided
if [ $# -ne 1 ]; then
    echo "Usage: $0 <number>"
    exit 1
fi

# Check if the argument is a non-negative integer
if ! [[ $1 =~ ^[0-9]+$ ]]; then
    echo "Error: Argument must be a non-negative integer."
    exit 1
fi

# Increment ports and node name based on the argument
ORACLIZER_HTTP_PORT=$((8080 + $1))
ORACLIZER_API_PORT=$((8090 + $1))
INFINISPAN_CLUSTER_NAME=cluster1
INFINISPAN_NODE_NAME="node$1"
INFINISPAN_STORE_PERSISTENCE_PATH="oraclizer/node$1/store/persistence/data"
INFINISPAN_STORE_PERSISTENT_PATH="oraclizer/node$1/store/persistent/data"
INFINISPAN_STORE_TEMPORARY_PATH="oraclizer/node$1/store/temporary/data"
# Export the environment variables
export ORACLIZER_HTTP_PORT
export ORACLIZER_API_PORT
export INFINISPAN_CLUSTER_NAME
export INFINISPAN_NODE_NAME
export INFINISPAN_STORE_PERSISTENCE_PATH;
export INFINISPAN_STORE_PERSISTENT_PATH;
export INFINISPAN_STORE_TEMPORARY_PATH;
# infinispan.store.persistence.path

echo "ORACLIZER_HTTP_PORT 설정: $ORACLIZER_HTTP_PORT"
echo "ORACLIZER_API_PORT 설정: $ORACLIZER_API_PORT"
echo "INFINISPAN_CLUSTER_NAME 설정: $INFINISPAN_CLUSTER_NAME"
echo "INFINISPAN_NODE_NAME 설정: $INFINISPAN_NODE_NAME"
# echo "INFINISPAN_STORE_PERSISTENCE_PATH 설정: $INFINISPAN_STORE_PERSISTENCE_PATH"
# echo "INFINISPAN_STORE_PERSISTENT_PATH 설정: $INFINISPAN_STORE_PERSISTENT_PATH"
# echo "INFINISPAN_STORE_TEMPORARY_PATH 설정: $INFINISPAN_STORE_TEMPORARY_PATH"


# ___global.lck 파일의 경로를 지정합니다.
LOCK_FILE="oraclizer/node$1/store/persistent/data/___global.lck"

# 파일이 존재하는지 확인하고, 있다면 삭제합니다.
if [ -f "$LOCK_FILE" ]; then
    echo "___global.lck 파일을 삭제합니다."
    rm -f "$LOCK_FILE"
fi
# Run Quarkus in dev mode
quarkus dev
