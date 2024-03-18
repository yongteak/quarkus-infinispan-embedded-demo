#!/bin/bash

# Initialize default values
NODE_PREFIX=0
# INFINISPAN_BIND_PORT
# INFINISPAN_INITIAL_HOSTS

# Parse command line arguments
for arg in "$@"
do
    case $arg in
        --node-prefix=*)
        NODE_PREFIX="${arg#*=}"
        ;;
        --bind-port=*)
        INFINISPAN_BIND_PORT="${arg#*=}"
        ;;
        --initial_hosts=*)
        INFINISPAN_INITIAL_HOSTS="${arg#*=}"
        ;;
        *)
        # Unknown option
        ;;
    esac
done

# Use NODE_PREFIX for other configurations as needed
ORACLIZER_HTTP_PORT=$((8080 + NODE_PREFIX))
ORACLIZER_API_PORT=$((8090 + NODE_PREFIX))
INFINISPAN_CLUSTER_NAME=cluster1
INFINISPAN_NODE_NAME="node$NODE_PREFIX"
INFINISPAN_STORE_PERSISTENCE_PATH="oraclizer/node$NODE_PREFIX/store/persistence/data"
INFINISPAN_STORE_PERSISTENT_PATH="oraclizer/node$NODE_PREFIX/store/persistent/data"
INFINISPAN_STORE_TEMPORARY_PATH="oraclizer/node$NODE_PREFIX/store/temporary/data"

# Export additional environment variables
export ORACLIZER_HTTP_PORT
export ORACLIZER_API_PORT
export INFINISPAN_CLUSTER_NAME
export INFINISPAN_NODE_NAME
export INFINISPAN_STORE_PERSISTENCE_PATH
export INFINISPAN_STORE_PERSISTENT_PATH
export INFINISPAN_STORE_TEMPORARY_PATH
export INFINISPAN_BIND_PORT
export INFINISPAN_INITIAL_HOSTS

# Echo the settings for verification
echo "NODE_PREFIX 설정: $NODE_PREFIX"
echo "INFINISPAN_BIND_PORT 설정: $INFINISPAN_BIND_PORT"
echo "INFINISPAN_INITIAL_HOSTS 설정: $INFINISPAN_INITIAL_HOSTS"
echo "ORACLIZER_HTTP_PORT 설정: $ORACLIZER_HTTP_PORT"
echo "ORACLIZER_API_PORT 설정: $ORACLIZER_API_PORT"
echo "INFINISPAN_CLUSTER_NAME 설정: $INFINISPAN_CLUSTER_NAME"
echo "INFINISPAN_NODE_NAME 설정: $INFINISPAN_NODE_NAME"

# echo "INFINISPAN_STORE_PERSISTENCE_PATH 설정: $INFINISPAN_STORE_PERSISTENCE_PATH"
# echo "INFINISPAN_STORE_PERSISTENT_PATH 설정: $INFINISPAN_STORE_PERSISTENT_PATH"
# echo "INFINISPAN_STORE_TEMPORARY_PATH 설정: $INFINISPAN_STORE_TEMPORARY_PATH"


# ___global.lck 파일의 경로를 지정합니다.
LOCK_FILE="oraclizer/node$NODE_PREFIX/store/persistent/data/___global.lck"

# 파일이 존재하는지 확인하고, 있다면 삭제합니다.
if [ -f "$LOCK_FILE" ]; then
    echo "___global.lck 파일을 삭제합니다."
    rm -f "$LOCK_FILE"
fi
# Run Quarkus in dev mode
# quarkus dev

# Run Quarkus in dev mode
./build/infinispan-embedded-demo-1.0.0-SNAPSHOT-runner
