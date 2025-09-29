package app;

import app.config.ApplicationConfig;
import app.config.HibernateConfig;
import io.javalin.Javalin;
import jakarta.persistence.EntityManagerFactory;

public class Main {
    public static  void  main(String[]args) {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory("hotel");

            Javalin app = ApplicationConfig.startServer(7070,emf);

        // event listener and close every db connection
        app.events(event -> {
            event.serverStopping(() -> {
                emf.close();
                System.out.println(" EntityManagerFactory closed");
            });
        });
    }
}
