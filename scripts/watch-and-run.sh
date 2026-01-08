#!/bin/bash

APP_PID=""

WATCH_DIRS=("src/main/java")

function start_app {
    mvn spring-boot:run -Dspring-boot.run.fork=true &
    APP_PID=$!
}

function stop_app {
    if [ ! -z "$APP_PID" ] && ps -p $APP_PID > /dev/null; then
        kill $APP_PID
        wait $APP_PID 2>/dev/null
    fi
}

function watch_changes {
    inotifywait -q -e modify,create,delete -r "${WATCH_DIRS[@]}"
}

start_app

while true; do
    watch_changes

    echo "Detected changes. Compiling..."
    mvn compile -DskipTests

    sleep 1

    stop_app
    start_app
done
