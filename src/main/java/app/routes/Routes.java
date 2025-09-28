package app.routes;

import app.config.HibernateConfig;
import app.controllers.HotelController;
import io.javalin.Javalin;
import jakarta.persistence.EntityManagerFactory;

public class Routes {
    public static void addRoutes(Javalin app){
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        HotelController hotelController = new HotelController(emf);

        app.get("/hotels", hotelController::getAll);
        app.get("/hotels/{id}", hotelController::getById);
        app.get("/hotels/{id}/rooms", hotelController::getRoomsForHotel);
        app.post("/hotels", hotelController::create);
        app.put("/hotels/{id}", hotelController::update);
        app.delete("/hotels/{id}", hotelController::delete);
    }
}
