package app.routes;

import app.daos.HotelDAO;
import app.dtos.HotelDTO;
import app.enums.HotelType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;



public class Populator {
    private final HotelDAO hotelDao;  // DAO to make hotels
    private final EntityManagerFactory emf;  // EntityManagerFactory to clean up

    // Constructor to retrieve DAO and EMF
    public Populator(HotelDAO hotelDao, EntityManagerFactory emf) {
        this.hotelDao = hotelDao;
        this.emf = emf;
    }


    public List<HotelDTO> populate3Hotels() {
        HotelDTO h1 = hotelDao.add(new HotelDTO("HotelInn1", "adress1", HotelType.STANDARD));
        HotelDTO h2 = hotelDao.add(new HotelDTO("HotelInn2 ", "adress2", HotelType.LUXURY));
        HotelDTO h3 = hotelDao.add(new HotelDTO("HotelInn3", "address3", HotelType.BUDGET));
        return List.of(h1, h2, h3);
    }

    public void cleanUpHotels() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Hotel").executeUpdate();
            em.createNativeQuery("ALTER SEQUENCE hotel_id_seq RESTART WITH 1").executeUpdate();
            em.getTransaction().commit();
        }
    }

}
