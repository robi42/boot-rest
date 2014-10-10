#!/bin/sh

exec java -server -jar `dirname "$0"`/boot-rest.jar
