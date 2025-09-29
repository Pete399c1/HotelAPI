package app.routes;

import app.config.HibernateConfig;
import app.controllers.HotelController;
import app.controllers.RoomController;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;
import static io.javalin.apibuilder.ApiBuilder.*;

public class RoomRoute {

    private final RoomController roomController;

    public RoomRoute(EntityManagerFactory emf) {
        this.roomController = new RoomController(emf);
    }

    protected EndpointGroup getRoutes() {
        return () ->{
            get("/", roomController::getAll);
            get("/{id}", roomController::getById);
            post("/", roomController::create);
            put("/{id}", roomController::update);
            delete("/{id}", roomController::delete);
        };
    }

}
