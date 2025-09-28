package app.controllers;


import app.daos.HotelDAO;
import app.dtos.HotelDTO;
import app.entities.Hotel;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.List;

public class HotelController {

    private HotelDAO hotelDAO;

    public HotelController(EntityManagerFactory emf) {
        this.hotelDAO = new HotelDAO(emf);
    }

    public void getAll(Context ctx) {
        // get all the hotels from the database
        List<Hotel> hotels = hotelDAO.getAll();

        // initialize a new empty list of hotelDto objects
        List<HotelDTO> hotelDTOs = new ArrayList<>();

        // foreach that goes through all the hotels
        for (Hotel hotel : hotels) {
            // converting hotel to hotelDto
            HotelDTO hotelDTO = new HotelDTO(hotel);
            // adding it to the list of empty hotelDto objects
            hotelDTOs.add(hotelDTO);
        }
        // return the DtoList as JSON to the client
        ctx.json(hotelDTOs);
    }

    public void getById(Context ctx) {
        // reading the id from url and Converting
        int id = Integer.parseInt(ctx.pathParam("id"));

        // find the hotel in the database by id
        Hotel hotel = hotelDAO.getById(id);

        // if the hotel exists
        if (hotel != null) {
            // convert the hotel to a hotelDTO object
            HotelDTO hotelDTO = new HotelDTO(hotel);
            // return dto as JSON
            ctx.json(hotelDTO);
        } else {
            // else if the hotel is not found, return 404
            ctx.status(404).result("Hotel not found");
        }
    }

    public void create(Context ctx) {
        // reading the request body convert to dto
        HotelDTO hotelDTO = ctx.bodyAsClass(HotelDTO.class);

        // converts HotelDto to hotel entity
        Hotel hotel = new Hotel(hotelDTO);

        // save the hotel in the database
        hotelDAO.create(hotel);

        // making HotelDto object for the saved hotel
        HotelDTO dto = new HotelDTO(hotel);

        // returns a status success and returning the dto as JSON
        // created 201
        ctx.status(201).json(dto);
    }

    public void update(Context ctx) {

        // read id for the url and convert to int
        int id = Integer.parseInt(ctx.pathParam("id"));

        // read the request body and change to a dto
        HotelDTO hotelDTO = ctx.bodyAsClass(HotelDTO.class);

        // convert hotelDto to a hotel object
        Hotel hotel = new Hotel(hotelDTO);

        // set the id from the url so that it will update the right hotel
        hotel.setId(id);

        // update the hotel in the database
        Hotel updated = hotelDAO.update(hotel);

        // make a hotelDto from the updated hotel
        HotelDTO dto = new HotelDTO(updated);

        // return dto as JSON
        ctx.json(dto);
    }

    public void delete(Context ctx) {

        // read id from url and convert to int
        int id = Integer.parseInt(ctx.pathParam("id"));

        // try and delete a hotel by id
        boolean deleted = hotelDAO.delete(id);


        if (deleted) {
            // success no content found
            ctx.status(204);
        } else {
            ctx.status(404).result("Hotel not found");
        }
    }

}
