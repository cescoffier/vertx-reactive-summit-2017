#!/usr/bin/env bash

# Params

RED='\033[0;31m'
NC='\033[0m' # No Color
YELLOW='\033[0;33m'
BLUE='\033[0;34m'

echo -e "${YELLOW}Starting minishift with Openshift ${VERSION} ${NC}"
minishift start --cpus=2 --memory=8G
echo -e "${YELLOW}Opening Openshift Console, connect as 'developer/developer' ${NC}"
minishift console
echo -e "${YELLOW}Connecting oc as 'developer' ${NC}"
oc login -u developer -p developer
echo -e "${YELLOW}Done !${NC}"

