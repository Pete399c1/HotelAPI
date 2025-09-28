package app.daos;

import app.dtos.HotelDTO;
import app.entities.Hotel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;

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
}
