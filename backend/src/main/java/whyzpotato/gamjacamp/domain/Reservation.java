package whyzpotato.gamjacamp.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Reservation extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "reservation_id")
    private Long id;

//    @ManyToOne
//    @JoinColumn(name = "camp_id")
//    private Camp camp;

//    @OneToOne
//    @JoinColumn(name = "room_id")
//    private Room room;

    private int numGuest;

    private LocalDateTime stayStarts;

    private LocalDateTime stayEnds;

    private int price;

}
