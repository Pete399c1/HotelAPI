package app.daos;

import app.dtos.HotelDTO;
import app.entities.Hotel;
import app.entities.Room;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HotelDAO implements IDAO<Hotel,Integer> {
    private final EntityManagerFactory emf;

    public HotelDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }


    @Override
    public Hotel getById(Integer id) {
        try(EntityManager em = emf.createEntityManager()){
            return em.find(Hotel.class,id);
        }
    }

    @Override
    public List<Hotel> getAll() {
        try(EntityManager em = emf.createEntityManager()){
            TypedQuery<Hotel> query = em.createQuery("select h from Hotel h", Hotel.class);
            return query.getResultList();
        }
    }

    @Override
    public Hotel create(Hotel hotel) {
        try(EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(hotel);
            em.getTransaction().commit();
            return hotel;
        }
    }

    @Override
    public Hotel update(Hotel hotel) {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            Hotel update = em.merge(hotel);
            em.getTransaction().commit();
            return update;
        }
    }

    @Override
    public boolean delete(Integer id) {
       try(EntityManager em = emf.createEntityManager()){
           em.getTransaction().begin();
           Hotel hotel = em.find(Hotel.class,id);
           if(hotel != null){
               em.remove(hotel);
               em.getTransaction().commit();
               return true;
           }
       }
       return false;
    }


    public void addRoom(Hotel hotel, Room room) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Hotel specifikHotel = em.find(Hotel.class, hotel.getId());
            if (specifikHotel != null) {
                specifikHotel.addRoom(room);
                //update the hotel in the database
                em.merge(specifikHotel);
            }
            em.getTransaction().commit();
        }
    }

    public void removeRoom(Hotel hotel, Room room) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // find hotel and room in the database
            Hotel specifikHotel = em.find(Hotel.class, hotel.getId());
            Room specifikRoom = em.find(Room.class, room.getId());

            // check if they exist
            if (specifikHotel != null && specifikRoom != null) {
                // remove room from the hotel list
                specifikHotel.getRooms().remove(specifikRoom);

                //update the hotel in the db
                em.merge(specifikHotel);
            }
            em.getTransaction().commit();
        }
    }

    public Set<Room> getRoomsForHotel(Hotel hotel) {
        try (EntityManager em = emf.createEntityManager()) {
            // find the hotel in the db
            Hotel specifikHotel = em.find(Hotel.class, hotel.getId());

            // if the hotel exists, then return the list of rooms
            if (specifikHotel != null) {
                return specifikHotel.getRooms();
            }

            // if the hotel not exists, return an empty list
            return new HashSet<>();
        }
    }

}
