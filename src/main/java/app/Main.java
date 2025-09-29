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

        // event listener and close every db connection
        app.events(event -> {
            event.serverStopping(() -> {
                emf.close();
                System.out.println(" EntityManagerFactory closed");
            });
        });
    }
}
