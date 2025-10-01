package app.daos;

import app.config.HibernateConfig;
import app.entities.Hotel;
import app.enums.HotelType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.security.PrivateKey;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HotelDAOTest {

    private HotelDAO hotelDAO;
    private static EntityManagerFactory emf;
    private EntityManager em;

    @BeforeAll
    public static void beforeAll() {
        // Sæt HibernateConfig til test og hent test EMF
        HibernateConfig.setTest(true);
        emf = HibernateConfig.getEntityManagerFactoryForTest();
    }

    @BeforeEach
    public void setUp() {
        // Making EntityManager and starts transaction
        em = emf.createEntityManager();
        em.getTransaction().begin();
        // initialized
        hotelDAO = new HotelDAO(emf);
    }

    @AfterEach
    public void tearDown() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback(); // rolls back after every test
        }
        if (em.isOpen()) {
            em.close();
        }
    }


    @Test
    void getById() {
        Hotel hotel = new Hotel();
        hotel.setName("ShittyHotel");
        hotel.setAddress("ShittyCity");
        hotel.setHotelType(HotelType.STANDARD);

        // Use the DAO to persist and save it
        Hotel savedHotel = hotelDAO.create(hotel);

        //Retrieve the hotel using getById
        Hotel getHotelById = hotelDAO.getById(savedHotel.getId());

        // making sure that something was returned
        assertNotNull(getHotelById);

        assertEquals(savedHotel.getId(), getHotelById.getId());
        assertEquals("ShittyHotel", getHotelById.getName());
        assertEquals("ShittyCity", getHotelById.getAddress());
        assertEquals(HotelType.STANDARD, getHotelById.getHotelType());
    }

    @Test
    void getAll() {
        //create and persist a few hotels
        Hotel hotel1 = new Hotel();
        hotel1.setName("HotelOne");
        hotel1.setAddress("AddressOne");
        hotel1.setHotelType(HotelType.STANDARD);
        hotelDAO.create(hotel1);

        Hotel hotel2 = new Hotel();
        hotel2.setName("HotelTwo");
        hotel2.setAddress("AddressTwo");
        hotel2.setHotelType(HotelType.LUXURY);
        hotelDAO.create(hotel2);

        Hotel hotel3 = new Hotel();
        hotel3.setName("HotelThree");
        hotel3.setAddress("AddressThree");
        hotel3.setHotelType(HotelType.BUDGET);
        hotelDAO.create(hotel3);

        // retrieve all the hotels
        List<Hotel> hotels = hotelDAO.getAll();

        //make sure all created hotels are present
        assertNotNull(hotels);
        assertEquals(3, hotels.size());
        assertTrue(hotels.contains(hotel1));
        assertTrue(hotels.contains(hotel2));
        assertTrue(hotels.contains(hotel3));

    }

    @Test
    void create() {
        // Arrange: make a new hotel
        Hotel hotel = new Hotel();
        hotel.setName("Hotel");
        hotel.setAddress("City");
        hotel.setHotelType(HotelType.STANDARD);

        // Act: persist it with DAO
        Hotel createdHotel = hotelDAO.create(hotel);

        //check that it was saved correctly
        assertNotNull(createdHotel);              // DAO must return a hotel
        assertTrue(createdHotel.getId() > 0);     // ID should be generated
        assertEquals("Hotel", createdHotel.getName());
        assertEquals("City", createdHotel.getAddress());
        assertEquals(HotelType.STANDARD, createdHotel.getHotelType());

        // Double-check: fetch it from DB again
        Hotel fromDb = hotelDAO.getById(createdHotel.getId());
        assertNotNull(fromDb);
        assertEquals(createdHotel, fromDb); // equals() is based on id
    }

    @Test
    void update() {
        //create and save a hotel
        Hotel hotel = new Hotel();
        hotel.setName("whatEver");
        hotel.setAddress("WhatEverCity");
        hotel.setHotelType(HotelType.STANDARD);

        Hotel savedHotel = hotelDAO.create(hotel);

        //change some fields and update in DB
        savedHotel.setName("WhatSoEver");
        savedHotel.setAddress("WhatCity");
        savedHotel.setHotelType(HotelType.LUXURY);

        Hotel updatedHotel = hotelDAO.update(savedHotel);

        //check values are updated
        assertNotNull(updatedHotel);
        assertEquals("WhatSoEver", updatedHotel.getName());
        assertEquals("WhatCity", updatedHotel.getAddress());
        assertEquals(HotelType.LUXURY, updatedHotel.getHotelType());

        //fetch fresh from DB
        Hotel fromDb = hotelDAO.getById(updatedHotel.getId());
        assertEquals("WhatSoEver", fromDb.getName());
        assertEquals("WhatCity", fromDb.getAddress());
        assertEquals(HotelType.LUXURY, fromDb.getHotelType());
    }

    @Test
    void delete() {
       // create and save a hotel
        Hotel hotel = new Hotel();
        hotel.setName("vMotel");
        hotel.setAddress("germany");
        hotel.setHotelType(HotelType.BUDGET);

        Hotel savedHotel = hotelDAO.create(hotel);
        assertNotNull(savedHotel.getId()); // make sure it got an ID

        //delete the hotel
        boolean deleted = hotelDAO.delete(savedHotel.getId());

        //deletion worked
        assertTrue(deleted);

        // And the hotel should not exist anymore
        Hotel fromDb = hotelDAO.getById(savedHotel.getId());
        assertNull(fromDb);
    }

    @Test
    void addRoom() {
    }

    @Test
    void removeRoom() {
    }

    @Test
    void getRoomsForHotel() {
    }
}