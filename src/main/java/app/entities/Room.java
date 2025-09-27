package app.entities;

import app.dtos.RoomDTO;
import app.enums.RoomType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "room_number")
    private int number;

    @Column(name = "room_price")
    private double price;

    @Enumerated(EnumType.STRING)
    @Column(name = "room_type")
    private RoomType roomType;

    @ManyToOne
    private Hotel hotel;

    public Room(RoomDTO roomDTO){
        this.id = roomDTO.getId();
        this.number = roomDTO.getNumber();
        this.price = roomDTO.getPrice();
        this.roomType = roomDTO.getRoomType();
    }


}
