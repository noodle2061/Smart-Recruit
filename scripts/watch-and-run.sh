#!/bin/bash

APP_PID=""

function start_app {
    mvn spring-boot:run -Dspring-boot.run.fork=false &
    APP_PID=$!
}

function stop_app {
    if [ ! -z "$APP_PID" ] && ps -p $APP_PID > /dev/null; then
        kill $APP_PID
        wait $APP_PID 2>/dev/null
    fi
}

start_app

while true; do
    inotifywait -e modify,create,delete -r /app/src /app/src/main/resources
    mvn compile

    stop_app
    start_app
done
