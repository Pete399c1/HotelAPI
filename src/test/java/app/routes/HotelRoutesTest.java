package app.routes;

import app.config.ApplicationConfig;
import app.config.HibernateConfig;
import app.daos.HotelDAO;
import app.dtos.HotelDTO;
import app.dtos.RoomDTO;
import app.enums.HotelType;
import io.javalin.Javalin;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContainingInAnyOrder;
import static org.hamcrest.Matchers.equalTo;

import static org.junit.jupiter.api.Assertions.*;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HotelRoutesTest {

    private static Javalin app;
    private static EntityManagerFactory emf;
    private static HotelDAO hotelDao;
    private static Populator populator;

    private static List<HotelDTO> hotels;
    private static HotelDTO h1, h2, h3;

    @BeforeAll
    void init() {
        HibernateConfig.setTest(true); // use test-db
        emf = HibernateConfig.getEntityManagerFactoryForTest();

        hotelDao = new HotelDAO(emf);
        populator = new Populator(hotelDao, emf);

        app = ApplicationConfig.startServer(7070, emf); // start server
    }

    @BeforeEach
    void setUp() {
        hotels = populator.populate3Hotels(); // make 3 test-hotels
        h1 = hotels.get(0);
        h2 = hotels.get(1);
        h3 = hotels.get(2);
    }

    @AfterEach
    void tearDown() {
        populator.cleanUpHotels();
    }

    @AfterAll
    void closeDown() {
        ApplicationConfig.stopServer(app); // stop server
    }

    @Test
    void GetAllHotels() {
        HotelDTO[] hotels =
                given()
                        .when()
                        .get("http://localhost:7070/api/v1/hotels")
                        .then()
                        .statusCode(200)
                        .extract()
                        .as(HotelDTO[].class);

        assertEquals(3, hotels.length);
        assertThat(hotels, arrayContainingInAnyOrder(h1, h2, h3));
    }

    @Test
    void GetHotelById() {
        HotelDTO hotel =
                given()
                        .when()
                        .get("http://localhost:7070/api/v1/hotels/" + h1.getId())
                        .then()
                        .statusCode(200)
                        .extract()
                        .as(HotelDTO.class);

        assertThat(hotel, equalTo(h1));
    }

    /*
    @Test
    void GetRoomsForHotel(){
        RoomDTO[] rooms =
                given()
                        .when()
                        .get("http://localhost:7070/api/v1/hotels/" + h1.getId() + "/rooms")
                        .then()
                        .statusCode(200)
                        // Extract data from the response
                        .extract()
                        // Convert the JSON response into an array of HotelDTO
                        .as(RoomDTO[].class);
        assertNotNull(rooms);
    }
     */

    @Test
    void createHotel(){
        HotelDTO newHotel = new HotelDTO("radisson", "Copenhagen", HotelType.STANDARD);

        HotelDTO createdHotel =
                given()
                        // Tell the server we are sending JSON
                        .contentType("application/json")
                        // Send newHotel as the request body
                        .body(newHotel)
                        .when()
                        .post("http://localhost:7070/api/v1/hotels")
                        .then()
                        .statusCode(201) // POST -> created
                        .extract()
                        // Convert the response to a HotelDTO
                        .as(HotelDTO.class);

        assertNotNull(createdHotel.getId());
        assertEquals("radisson", createdHotel.getName());
        assertEquals("Copenhagen", createdHotel.getAddress());
        assertEquals(HotelType.STANDARD, createdHotel.getHotelType());

    }

    @Test
    void updateHotel(){
        h1.setName("HotelParis");
        h1.setAddress("Paris");
        h1.setHotelType(HotelType.LUXURY);

        HotelDTO updatedHotel =
                given()
                        .contentType("application/json")
                        .body(h1)
                        .when()
                        .put("http://localhost:7070/api/v1/hotels/" + h1.getId())
                        .then()
                        .statusCode(200)
                        .extract()
                        .as(HotelDTO.class);

        assertEquals("HotelParis", updatedHotel.getName());
        assertEquals("Paris", updatedHotel.getAddress());
        assertEquals(HotelType.LUXURY, updatedHotel.getHotelType());
    }

    @Test
    void deleteHotel(){
        given()
                // "when()" describes the actual action: the HTTP request itself
                .when()
                .delete("http://localhost:7070/api/v1/hotels/" + h3.getId())
                // "then()" validates the response from the server
                .then()
                // Check that the server returned HTTP 204 OK
                .statusCode(204); // delete -> no content

        // check that the hotel no longer exists
        given()
                .when()
                .get("http://localhost:7070/api/v1/hotels/" + h3.getId())
                .then()
                .statusCode(404);

    }

}