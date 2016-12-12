/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.company.vertxstarter;

import io.vertx.core.Vertx;

/**
 *
 * @author thomasperoutka
 */
public class Bootstrap {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new MainVerticle());

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                vertx.close();
            }
        });
    }

}
