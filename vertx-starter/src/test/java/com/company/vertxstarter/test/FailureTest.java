/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.company.vertxstarter.test;

import com.company.vertxstarter.MainVerticle;
import com.company.vertxstarter.dto.Failure;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author thomasperoutka
 */
@RunWith(VertxUnitRunner.class)
public class FailureTest {

    Vertx vertx;

    @Before
    public void before(TestContext context) {
        vertx = Vertx.vertx();
        vertx.deployVerticle(MainVerticle.class.getName(), context.asyncAssertSuccess());
    }

    @After
    public void after(TestContext context) {
        vertx.close(context.asyncAssertSuccess());
    }
    
    private void assertFailure(TestContext context, Failure f, int statusCode, String message) {
        context.assertEquals(f.getCode(), statusCode);
        context.assertEquals(f.getMessage(), message);
    }

    @Test
    public void notFound(TestContext context) {
        HttpClient client = vertx.createHttpClient();
        Async async = context.async();
        HttpClientRequest req = client.get(8080, "localhost", "/notfound", resp -> {
            resp.bodyHandler(body -> {
                JsonObject res = body.toJsonObject();
                assertFailure(context, Failure.NOT_FOUND, resp.statusCode(), res.getString("message"));
                client.close();
                async.complete();
            });
        });
        req.headers().add("Accept", "application/json");
        req.end();
    }
    
    
    @Test
    public void noMediaType(TestContext context) {
        HttpClient client = vertx.createHttpClient();
        Async async = context.async();
        HttpClientRequest req = client.get(8080, "localhost", "/", resp -> {
            resp.bodyHandler(body -> {
                JsonObject res = body.toJsonObject();
                assertFailure(context, Failure.NO_MEDIA_TYPE, resp.statusCode(), res.getString("message"));
                client.close();
                async.complete();
            });
        });
        req.end();
    }
    
    @Test
    public void notSupportedMediaType(TestContext context) {
        HttpClient client = vertx.createHttpClient();
        Async async = context.async();
        HttpClientRequest req = client.get(8080, "localhost", "/", resp -> {
            resp.bodyHandler(body -> {
                JsonObject res = body.toJsonObject();
                assertFailure(context, Failure.UNSUPPORTED_MEDIA_TYPE, resp.statusCode(), res.getString("message"));
                client.close();
                async.complete();
            });
        });
        req.headers().add("Accept", "application/xml");
        req.end();
    }
}
