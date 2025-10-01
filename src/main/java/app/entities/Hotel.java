package app.entities;

import app.dtos.HotelDTO;
import app.enums.HotelType;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "hotel_name")
    private String name;

    @Column(name = "hotel_address")
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "hotel_type")
    private HotelType hotelType;

    // lazy get the relation when I use it Lazy / Eager get relation right away do also get all rooms from db
    @OneToMany(mappedBy = "hotel",fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Room> rooms = new HashSet<>();

    // mapping
    public Hotel(HotelDTO hotelDTO){
        this.id = hotelDTO.getId();
        this.name = hotelDTO.getName();
        this.address = hotelDTO.getAddress();
        this.hotelType = hotelDTO.getHotelType();
    }

    public void addRoom(Room room) {
        if (room != null) {
            rooms.add(room);
            room.setHotel(this);
        }
    }
}
