#!/usr/bin/env bash

RED='\033[0;31m'
NC='\033[0m' # No Color
YELLOW='\033[0;33m'
BLUE='\033[0;34m'

PROJECT=reactive-summit

echo -e "${BLUE}Creating project ${PROJECT} ${NC}"
oc new-project ${PROJECT} 
echo -e "${BLUE}Adding permissions to ${PROJECT} ${NC}"
oc policy add-role-to-user view admin -n ${PROJECT}
oc policy add-role-to-user view -n ${PROJECT} -z default
oc policy add-role-to-group view system:serviceaccounts -n ${PROJECT}

echo -e "${BLUE}Deploying Redis ${NC}"
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
echo $DIR
oc create -f ${DIR}/templates/redis/redis.yaml
oc create -f ${DIR}/templates/redis/redis-rc.yaml
oc create -f ${DIR}/templates/redis/redis-service.yaml


DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
echo "Pricer service: ${DIR}/../pricer-service"
cd ${DIR}/../pricer-service
echo -e "${BLUE}Deploying the pricer service${NC}"
mvn clean fabric8:deploy

echo -e "${BLUE}Done ! ${NC}"