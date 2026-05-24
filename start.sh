#!/bin/sh
set -e

if ! docker network inspect fintech-network >/dev/null 2>&1; then
    echo "Creating network fintech-network..."
    docker network create fintech-network
else
    echo "Network fintech-network already exists."
fi

echo "Starting docker containers..."
docker-compose up -d

if[ -z "$1" ]; then
    echo "Usage: $0 host:port [command]"
    exit 1
fi

HOST=$(echo "$1" | cut -d: -f1)
PORT=$(echo "$1" | cut -d: -f2)

if [ "$HOST" = "$PORT" ]; then
    PORT=5432
fi

echo "Waiting for $HOST:$PORT..."
while ! nc -z "$HOST" "$PORT" 2>/dev/null; do
    echo "[$(date)] Still waiting..."
    sleep 1
done

echo "$HOST:$PORT is ready. You can start your Java app now!"

if [ -n "$2" ]; then
    shift
    exec "$@"
fi