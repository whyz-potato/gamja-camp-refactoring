package whyzpotato.gamjacamp.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "room")
public class Room {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "camp_id")
    private Camp camp;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer cnt;

    @Column
    private Integer capacity;

    // images


    private int weekPrice;
    private int weekendPrice;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<PeakPrice> peakPrices = new ArrayList<PeakPrice>();

    @Builder
    public Room(Camp camp, String name, Integer cnt, Integer capacity) {
        this.camp = camp;
        this.name = name;
        this.cnt = cnt;
        this.capacity = capacity;
    }

    public List<Integer> getPrices(LocalDate start, LocalDate end) {
        List<Integer> prices = new ArrayList<>();
        int length;
        for (LocalDate date = start; date.isBefore(end); date = date.plusDays(1)) {
            length = prices.size();
            for (PeakPrice peakPrice : peakPrices) {
                if (peakPrice.getPeakStart().compareTo(date) <= 0 && peakPrice.getPeakStart().compareTo(date) >= 0) {
                    prices.add(peakPrice.getPeakPrice());
                    break;
                }
            }
            if (length < prices.size()) {
                DayOfWeek dayOfWeek = date.getDayOfWeek();
                if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY)
                    prices.add(weekPrice);
                else
                    prices.add(weekendPrice);
            }
        }
        return prices;
    }


}
