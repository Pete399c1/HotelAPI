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



}
