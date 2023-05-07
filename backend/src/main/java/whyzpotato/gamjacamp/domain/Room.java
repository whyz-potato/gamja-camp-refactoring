package whyzpotato.gamjacamp.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name="room")
public class Room {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name="camp_id")
    private Camp camp;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer cnt;

    @Column
    private Integer capacity;

    // images

    @OneToOne
    @JoinColumn(name="ordinary_price_id")
    private OrdinaryPrice ordinaryPrice;

    @OneToMany(mappedBy="room")
    private List<PeakPrice> peakPrices = new ArrayList<PeakPrice>();

    @Builder
    public Room(Camp camp, String name, Integer cnt, Integer capacity) {
        this.camp = camp;
        this.name = name;
        this.cnt = cnt;
        this.capacity = capacity;
    }

}
