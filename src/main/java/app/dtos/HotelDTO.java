package app.dtos;

import app.entities.Hotel;
import app.enums.HotelType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class HotelDTO {
    private int id;
    private String name;
    private String address;
    private HotelType hotelType;

    public HotelDTO(Hotel hotel){
        this.id = hotel.getId();
        this.name = hotel.getName();
        this.address = hotel.getAddress();
        this.hotelType = hotel.getHotelType();
    }

    // for the populator test class
    public HotelDTO(String name, String address, HotelType hotelType) {
        this.name = name;
        this.address = address;
        this.hotelType = hotelType;
    }

}
