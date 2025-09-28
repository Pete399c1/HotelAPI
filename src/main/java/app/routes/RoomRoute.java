package app.routes;

import app.config.HibernateConfig;
import app.controllers.HotelController;
import app.controllers.RoomController;
import io.javalin.Javalin;
import jakarta.persistence.EntityManagerFactory;

public class RoomRoute {
    public static  void addRoutes(Javalin app){
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        RoomController roomController = new RoomController(emf);

        app.get("/rooms", roomController::getAll);
        app.get("/rooms/{id}", roomController::getById);
        app.post("/rooms", roomController::create);
        app.put("/rooms/{id}", roomController::update);
        app.delete("/rooms/{id}", roomController::delete);
    }
}
