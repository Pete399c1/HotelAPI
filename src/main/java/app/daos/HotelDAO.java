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
                em.merge(specifikHotel);
            }
            em.getTransaction().commit();
        }
    }

    public void removeRoom(Hotel hotel, Room room) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Hotel specifikHotel = em.find(Hotel.class, hotel.getId());
            Room specifikRoom = em.find(Room.class, room.getId());
            if (specifikHotel != null && specifikRoom != null) {
                specifikHotel.getRooms().remove(specifikRoom);
                specifikRoom.setHotel(null);
                em.merge(specifikHotel);
            }
            em.getTransaction().commit();
        }
    }

    public Set<Room> getRoomsForHotel(Hotel hotel) {
        try (EntityManager em = emf.createEntityManager()) {
            Hotel specifikHotel = em.find(Hotel.class, hotel.getId());
            if (specifikHotel != null) {
                return specifikHotel.getRooms();
            }
            return new HashSet<>();
        }
    }
}
