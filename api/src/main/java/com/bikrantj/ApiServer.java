package com.bikrantj;

import com.bikrantj.config.Routes;
import io.javalin.Javalin;

public class ApiServer {
    public static void main(String[] args) {
        Javalin app = Javalin.create(config -> {
            config.http.defaultContentType = "application/json";
        }).start(8000);

        // Configure all routes
        Routes.configure(app);

        // Demo route (optional - can remove)
        app.get("/", ctx -> ctx.result("{\"status\":\"Workspace Monitor API is running\"}"));

        System.out.println("Server started on http://localhost:8000");
    }
}