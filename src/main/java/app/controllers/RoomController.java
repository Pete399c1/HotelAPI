package app.controllers;

import app.daos.RoomDAO;
import app.dtos.RoomDTO;
import app.entities.Room;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;

import java.util.ArrayList;
import java.util.List;

public class RoomController {
    private RoomDAO roomDAO;

    // keys to the room and database
    public RoomController(EntityManagerFactory emf) {
        this.roomDAO = new RoomDAO(emf);
    }

    public void getAll(Context ctx) {
        // getting a list of all rooms from db
        List<Room> rooms = roomDAO.getAll();

        // initializing a new empty list of RoomDto object
        List<RoomDTO> roomDTOs = new ArrayList<>();

        // running through the list of rooms and converting room to the dto object
        for (Room room : rooms) {
            RoomDTO dto = new RoomDTO(room);
            // add to the empty list
            roomDTOs.add(dto);
        }

        // return the list as JSON
        // so when I make the http request get a response
        ctx.json(roomDTOs);
    }

    public void getById(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        Room room = roomDAO.getById(id);

        if (room != null) {
            RoomDTO dto = new RoomDTO(room);
            ctx.json(dto);
        } else {
            ctx.status(404).result("Room not found");
        }
    }

    public void create(Context ctx) {
        RoomDTO roomDTO = ctx.bodyAsClass(RoomDTO.class);
        Room room = new Room(roomDTO);

        roomDAO.create(room);

        RoomDTO dto = new RoomDTO(room);
        ctx.status(201).json(dto);
    }

    public void update(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        RoomDTO roomDTO = ctx.bodyAsClass(RoomDTO.class);

        Room room = new Room(roomDTO);
        room.setId(id);

        Room updated = roomDAO.update(room);
        RoomDTO dto = new RoomDTO(updated);

        ctx.json(dto);
    }

    public void delete(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        boolean deleted = roomDAO.delete(id);

        if (deleted) {
            ctx.status(204);
        } else {
            ctx.status(404).result("Room not found");
        }
    }
}

