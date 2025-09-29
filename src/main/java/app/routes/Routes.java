package app.routes;

import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;


import static io.javalin.apibuilder.ApiBuilder.*;

public class Routes {
    private final HotelRoute hotelRoute;
    //private final RoomRoute roomRoute;
    public Routes(EntityManagerFactory emf) {
        this.hotelRoute = new HotelRoute(emf);
    }
    public EndpointGroup getRoutes(){
        return () ->{
            path("/hotels", hotelRoute.getRoutes());
        };
    }
}
