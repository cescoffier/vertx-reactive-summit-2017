#!/usr/bin/env bash

RED='\033[0;31m'
NC='\033[0m' # No Color
YELLOW='\033[0;33m'
BLUE='\033[0;34m'

DIR="$(dirname "$0")"
NAME=${PWD##*/}

echo -e "${BLUE}Creating binary build $NAME ${NC}"
oc new-build --binary --name=$NAME -l app=$NAME

echo -e "${BLUE}Build application $NAME ${NC}"
mvn package

echo -e "${BLUE}Sending bits to Openshift and starting the Docker build for $NAME ${NC}"
oc start-build $NAME --from-dir=. --follow

echo -e "${BLUE}Creating new application $NAME (Deployment -> Service & Pod) - Selector is 'app=$NAME' ${NC}"
oc new-app $NAME -l app=$NAME

echo -e "${BLUE}Exposing service $NAME publicly (creating route $NAME) ${NC}"
oc expose service $NAME

echo -e "${BLUE}Done ! ${NC}"