package app.daos;

import app.entities.Hotel;
import app.entities.Room;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RoomDAO implements IDAO<Room,Integer>{
    private EntityManagerFactory emf;

    public RoomDAO(EntityManagerFactory emf){
        this.emf = emf;
    }

    @Override
    public Room getById(Integer id) {
       try(EntityManager em = emf.createEntityManager()){
            return em.find(Room.class, id);
       }
    }

    @Override
    public List<Room> getAll() {
        try(EntityManager em = emf.createEntityManager()){
            TypedQuery<Room> query = em.createQuery("select r from Room r", Room.class);
            return query.getResultList();
        }
    }

    @Override
    public Room create(Room room) {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            em.persist(room);
            em.getTransaction().commit();
            return room;
        }
    }

    @Override
    public Room update(Room room) {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            Room update = em.merge(room);
            em.getTransaction().commit();
            return update;
        }
    }

    @Override
    public boolean delete(Integer id) {
        try(EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Room room = em.find(Room.class,id);
            if(room != null){
                em.remove(room);
                em.getTransaction().commit();
                return true;
            }
        }
        return  false;
    }

    public List<Room> getRoomsByHotelId(int hotelId) {
        try(EntityManager em = emf.createEntityManager()) {
            List<Room> rooms = em.createQuery("SELECT r FROM Room r WHERE r.hotel.id = :hotelId", Room.class)
                    .setParameter("hotelId", hotelId)
                    .getResultList();

            return rooms;
        }
    }
}
