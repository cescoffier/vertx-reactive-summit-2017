## Flow

1. Develop a set of reactive microservices
2. I've mentioned reactive but what does it mean? (>> Picture))
4. Reactive mainly means 3 things: system / streams / programming
5. System -> Distributed systems done right, async, resilient, elastic, responsive
6. Programming -> API to compose event-based application based on the concept of data streams
7. Streams -> Async Non-blocking back pressure protocol 
8. (>> picture) I also mentioned microservices, and with microservices it become unmanageable very quickly. You need a platform managing your deployment, updates, discovery, administration... To do that I'm going to use Kubernetes / OpenShift.
9. (>> Picture) Kubernetes is a container platform on which you deploy your microservices or application packaged inside containers (generally Docker). OpenShift extends Kubernetes with a set of features such as build, _deployments support_ and provide a set of services (database services, ci/cd tooling, distributed logging, metrics...)
10. Your microservice runs inside a container... (>> picture) )containers is about sharing! and what you application get as CPU and memory is a slice of a slice. You need to make an efficient usage of these resources. Reduce the number of threads, the GC pause, footprint... That's why we are going to build our microservices with Eclipse Vert.x - a toolkit to build reactive and distributed system, container and kubernetes native.
11. Ok enough talking, let's see some code.
12. Let's start with a first microservices - the shopping backend microservice.

mvn io.fabric8:vertx-maven-plugin:1.0.9:setup \
 -Ddependencies=redis,web,service-discovery,service-discovery-kubernetes,org.slf4j:slf4j-api:1.7.25 \
 -Dverticle=io.escoffier.demo.ShoppingBackendVerticle \
 -DprojectGroupId=escoffier.me.demo \
 -DprojectArtifactId=shopping-backend 




  