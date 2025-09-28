package app;

import app.config.HibernateConfig;
import app.routes.Routes;
import io.javalin.Javalin;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class Main {
    public static  void  main(String[]args) {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        Javalin app = Javalin.create().start(7070);

        Routes.addRoutes(app);

        // Part 3 Error handling
        app.exception(IllegalStateException.class, (e, ctx) -> {
            ctx.status(400).result("Invalid request: " + e.getMessage());
        });

        app.exception(Exception.class, (e, ctx) -> {
            ctx.status(500).result("Something went wrong: " + e.getMessage());
        });

        app.events(event -> {
            event.serverStopping(() -> {
                emf.close();
                System.out.println(" EntityManagerFactory closed");
            });
        });
    }
}
