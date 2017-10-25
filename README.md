# Slideless reactive microservices

Code developed on stage during the "slideless reactive microservices" session presented at the Reactive Summit 2017.

## Prerequisites

1. You need OpenShift (Minishift, OpenShift dedicated or OpenShift Online) running.
2. You need the `oc` command line tool and have connected it with your OpenShift instance

## Preparation

Run `scripts/prepare-project.sh` from the source code root

## Shopping Backend

```bash
cd shopping-bakend
../scripts/build-with-docker.sh
```

## Shopping List

```bash
cd shopping-list-service
mvn fabric8:deploy
```  



 