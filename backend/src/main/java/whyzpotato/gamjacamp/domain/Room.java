package whyzpotato.gamjacamp.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
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

    @NotNull
    private String name;

    @Min(1)
    private int cnt;

    @Min(1)
    private int capacity;

    // images

    @Min(1)
    private int weekPrice;

    @Min(1)
    private int weekendPrice;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<PeakPrice> peakPrices;


    @Builder
    public Room(Camp camp, String name, int cnt, int capacity, int weekPrice, int weekendPrice, List<PeakPrice> peakPrices) {
        this.camp = camp;
        this.name = name;
        this.cnt = cnt;
        this.capacity = capacity;
        this.weekPrice = weekPrice;
        this.weekendPrice = weekendPrice;
        this.peakPrices = peakPrices;
    }

    public void setCamp(Camp camp) {
        this.camp = camp;
        if (!camp.getRooms().contains(this))
            camp.getRooms().add(this);
    }

    // 해당 기간 동안의 방 가격 리스트 반환
    public List<Integer> getPrices(LocalDate stayStarts, LocalDate stayEnds) {
        List<Integer> prices = new ArrayList<>();

        for (LocalDate date = stayStarts; date.isBefore(stayEnds); date = date.plusDays(1)) {
            boolean isPeak = false;
            if (peakPrices != null) {
                for (PeakPrice peakPrice : peakPrices) {
                    if (peakPrice.isPeakDate(date)) {
                        isPeak = true;
                        prices.add(peakPrice.getPeakPrice());
                        break;
                    }
                }
            }
            if (!isPeak) {
                if ((date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY)) {
                    prices.add(weekendPrice);
                } else {
                    prices.add(weekPrice);
                }
            }
        }
        return prices;
    }


    public void update(Room room) {
        this.name = room.getName();
        this.cnt = room.getCnt();
        this.capacity = room.getCapacity();
        this.weekPrice = room.getWeekPrice();
        this.weekendPrice = room.getWeekendPrice();
        this.peakPrices = room.getPeakPrices();
    }

    public void setWeekPrice(int weekPrice) {
        this.weekPrice = weekPrice;
    }

    public void setWeekendPrice(int weekendPrice) {
        this.weekendPrice = weekendPrice;
    }
}
