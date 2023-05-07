package whyzpotato.gamjacamp.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "peak_price")
public class PeakPrice {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name="room_id")
    private Room room;

    @Column
    private Integer peakPrice;

    @Column
    private Date peakStart;

    @Column
    private Date peakEnd;

}
