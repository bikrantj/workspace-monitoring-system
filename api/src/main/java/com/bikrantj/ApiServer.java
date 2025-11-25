package com.bikrantj;

import com.bikrantj.config.Routes;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.javalin.Javalin;

public class ApiServer {
    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        // Remove this line in production if you want smaller responses
        mapper.enable(SerializationFeature.INDENT_OUTPUT);


        Javalin app = Javalin.create(config -> {
            config.http.defaultContentType = "application/json";
//            config.jsonMapper(new JavalinJackson(mapper));
        }).start(8000);

        // Configure all routes
        Routes.configure(app);

        // Demo route (optional - can remove)
        app.get("/", ctx -> ctx.result("{\"status\":\"Workspace Monitor API is running\"}"));

        System.out.println("Server started on http://localhost:8000");
    }
}