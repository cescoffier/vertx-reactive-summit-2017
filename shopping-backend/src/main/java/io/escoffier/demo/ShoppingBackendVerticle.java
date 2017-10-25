package io.escoffier.demo;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.redis.RedisClient;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.types.RedisDataSource;

import java.util.HashMap;
import java.util.Map;

public class ShoppingBackendVerticle extends AbstractVerticle {

    private RedisClient redis;

    private static final String KEY = "SHOPPING";

    @Override
    public void start() {
        Router router = Router.router(vertx);
        router.get("/").handler(rc -> rc.response().end("Hello Austin"));
        router.get("/shopping").handler(this::getList);
        router.post().handler(BodyHandler.create());
        router.post("/shopping").handler(this::addToList);
        router.delete("/shopping/:name").handler(this::deleteFromList);

        ServiceDiscovery.create(vertx, discovery -> {
            RedisDataSource.getRedisClient(discovery, svc -> svc.getName().equals("redis"), ar -> {
                if (ar.failed()) {
                    System.out.println("D'oh");
                } else {
                    redis = ar.result();
                    vertx.createHttpServer()
                        .requestHandler(router::accept)
                        .listen(8080);
                }
            });
        });

    }

    private void deleteFromList(RoutingContext rc) {
        String name = rc.pathParam("name");
        redis.hdel(KEY, name, x -> {
            getList(rc);
        });
    }

    private void addToList(RoutingContext rc) {
        JsonObject json = rc.getBodyAsJson();
        String name = json.getString("name");
        Integer quantity = json.getInteger("quantity", 1);

        redis.hset(KEY, name, quantity.toString(), x -> {
            getList(rc);
        });
    }

    private void getList(RoutingContext rc) {
        redis.hgetall(KEY, res -> {
            if (res.failed()) {
                rc.fail(res.cause());
            } else {
                JsonObject json = res.result();
                rc.response()
                    .putHeader("X-POD", System.getenv("HOSTNAME"))
                    .end(json.encodePrettily());
            }
        });
    }

}
