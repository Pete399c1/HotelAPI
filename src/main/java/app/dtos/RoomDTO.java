package app.dtos;

import app.entities.Room;
import app.enums.RoomType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoomDTO {
    private int id;
    private int number;
    private double price;
    private RoomType roomType;

    public RoomDTO(Room room){
        this.id = room.getId();
        this.number = room.getNumber();
        this.price = room.getPrice();
        this.roomType = room.getRoomType();
    }

}
