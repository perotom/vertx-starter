/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.company.vertxstarter.api;

import com.company.vertxstarter.api.util.FilterExpression;
import com.company.vertxstarter.api.util.QueryParam;
import com.company.vertxstarter.api.util.SortExpression;
import com.company.vertxstarter.dto.Person;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/*
 * @author thomasperoutka
 */
public class PersonResource {

    public void get(RoutingContext e) {
        QueryParam params = new QueryParam(e.request().query());
        if (!params.isValid()) {
            e.fail(params.error());
            return;
        }

        Set<SortExpression> sortExp = params.getSortExpressions();
        Set<FilterExpression> filterExp = params.getFilterExpressions();
        
        List<Person> people = new ArrayList<>();
        people.add(new Person("Thomas", "Peroutka"));
        people.add(new Person("Lisa", "Gärtner"));
        people.add(new Person("Felix", "Wagner"));
        people.add(new Person("Kathi", "Schrank"));
        people.add(new Person("Omar", "Arrafeh"));
        people.add(new Person("Tim", "Schwarz"));
        people.add(new Person("Tom", "Schwarz"));
        people.add(new Person("Adriana", "Krug"));
        people.add(new Person("Lisa", "Moller"));
        people.add(new Person("Stefan", "Moller"));
        people.add(new Person("Anna", "Gschaider"));
       
        HttpServerResponse response = e.response();
        response.end(Json.encode(people));

        /*
        JsonObject postgreSQLClientConfig = new JsonObject();
        postgreSQLClientConfig.put("host", "localhost");
        postgreSQLClientConfig.put("port", 5432);
        postgreSQLClientConfig.put("username", "postgres");
        postgreSQLClientConfig.put("database", "postgres");
        AsyncSQLClient client = PostgreSQLClient.createShared(e.vertx(), postgreSQLClientConfig);

        client.getConnection(res -> {
            if (res.succeeded()) {

                SQLConnection connection = res.result();
                
                connection.close();
                client.close();
                
                HttpServerResponse response = e.response();
                response.end("{\"message\":\"success\"}");
            } else {
                HttpServerResponse response = e.response();
                response.end("{\"message\":\"failed\"}");
            }
        });
        */
        
    }

}
