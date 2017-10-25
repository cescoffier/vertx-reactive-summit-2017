package me.escoffier.demo.pricer;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class PricerVerticle extends AbstractVerticle {

    Map<String, Double> prices = new HashMap<>();
    Random random = new Random();
    private boolean slow;

    @Override
    public void start() throws Exception {
        Router router = Router.router(vertx);

        router.get("/prices/:name").handler(rc -> {
            String name = rc.pathParam("name");
            Double price = prices.computeIfAbsent(name, k -> (double) random.nextInt(50));
            rc.response().end(new JsonObject().put("name", name).put("price", price).encodePrettily());
        });

        router.route().handler(BodyHandler.create());
        router.post("/prices").handler(rc -> {
            if (slow) {
                vertx.setTimer(5000, l -> computeResponse(rc));
            } else {
                computeResponse(rc);
            }
        });

        router.get("/health").handler(rc -> rc.response().end("OK"));

        router.get("/toggle").handler(rc -> {
            slow = ! slow;
            rc.response().end((slow ? "Slow" : "Normal") + " mode enabled");
        });

        router.get("/assets*").handler(StaticHandler.create());

        vertx.createHttpServer()
            .requestHandler(router::accept)
            .listen(8080);
    }

    private void computeResponse(RoutingContext rc) {
        JsonObject json = rc.getBodyAsJson();
        String name = json.getString("name");
        Double price = prices.computeIfAbsent(name, k -> (double) random.nextInt(50));
        Integer q = Integer.parseInt(json.getString("quantity", "1"));
        rc.response().end(new JsonObject()
            .put("name", name)
            .put("price", price)
            .put("quantity", json.getString("quantity", "1"))
            .put("total", "" + (price * q))
            .encodePrettily());
    }
}
