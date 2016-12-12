/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.company.vertxstarter;

import com.company.vertxstarter.api.PersonResource;
import com.company.vertxstarter.dto.Failure;
import com.google.inject.Guice;
import com.google.inject.Injector;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.CorsHandler;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author thomasperoutka
 */
public class MainVerticle extends AbstractVerticle {

    @Override
    public void start(Future<Void> fut) {
        // Create a router object.
        Router router = Router.router(vertx);
        //CORS handler
        router.route().handler(CorsHandler.create("*")
                .allowedMethod(HttpMethod.GET)
                .allowedMethod(HttpMethod.POST)
                .allowedMethod(HttpMethod.OPTIONS)
                .allowedHeader("Content-Type")
                .allowedHeader("Accept"));

        //default headers
        router.route().handler(ctx -> {
            ctx.response()
                    .putHeader("Cache-Control", "no-store, no-cache")
                    .putHeader("Content-Type", "application/json");

            if (StringUtils.isEmpty(ctx.request().getHeader("Accept"))) {
                ctx.fail(Failure.NO_MEDIA_TYPE);
                return;
            } else if (!"application/json".equalsIgnoreCase(ctx.request().getHeader("Accept"))) {
                ctx.fail(Failure.UNSUPPORTED_MEDIA_TYPE);
                return;
            }
            ctx.next();
        });

        //error handling
        router.route().failureHandler(ctx -> {
            HttpServerResponse response = ctx.response();
            final JsonObject error = new JsonObject();
            Failure ex;

            if (ctx.failure() instanceof Failure) { //specific error
                ex = (Failure) ctx.failure();
            } else { //general error
                ctx.failure().printStackTrace();
                ex = Failure.INTERNAL_ERROR;
            }
            error.put("message", ex.getMessage());
            response.setStatusCode(ex.getCode()).end(error.encode());
        });
        //default 404 handling
        router.route().last().handler(ctx -> {
            HttpServerResponse response = ctx.response();
            final JsonObject error = new JsonObject();
            error.put("message", Failure.NOT_FOUND.getMessage());
            response.setStatusCode(404).end(error.encode());
        });

        //routes
        Injector injector = Guice.createInjector(new AppInjector());
        router.route(HttpMethod.GET, "/people")
                .handler(injector.getInstance(PersonResource.class)::get);

        // Create the HTTP server and pass the "accept" method to the request handler.
        HttpServerOptions serverOptions = new HttpServerOptions();
        serverOptions.setCompressionSupported(true);
        vertx
                .createHttpServer(serverOptions)
                .requestHandler(router::accept)
                .listen(
                        config().getInteger("http.port", 8080),
                        result -> {
                            if (result.succeeded()) {
                                fut.complete();
                            } else {
                                fut.fail(result.cause());
                            }
                        }
                );
    }

}
