#!/usr/bin/env bash

mvn compile assembly:single > /dev/null && java -jar target/remindev-1.0-SNAPSHOT-jar-with-dependencies.jar