package app.routes;


import app.config.HibernateConfig;
import app.controllers.HotelController;
import app.daos.HotelDAO;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;

import static io.javalin.apibuilder.ApiBuilder.*;
public class HotelRoute {
    private final HotelController hotelController;

    // dependency injections
    public HotelRoute(EntityManagerFactory emf) {
        this.hotelController = new HotelController(emf);
    }

    protected EndpointGroup getRoutes() {
        return () ->{
            get("/", hotelController::getAll);
            get("/{id}", hotelController::getById);
            get("/{id}/rooms", hotelController::getRoomsForHotel);
            post("/", hotelController::create);
            put("/{id}", hotelController::update);
            delete("/{id}", hotelController::delete);
        };
    }
}
