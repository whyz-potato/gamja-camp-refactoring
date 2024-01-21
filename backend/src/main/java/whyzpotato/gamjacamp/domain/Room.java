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
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "room")
public class Room {

    @Id
    @GeneratedValue
    @Column(name = "room_id")
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

    @Builder
    public Room(Camp camp, String name, int cnt, int capacity, int weekPrice, int weekendPrice) {
        this.camp = camp;
        this.name = name;
        this.cnt = cnt;
        this.capacity = capacity;
        this.weekPrice = weekPrice;
        this.weekendPrice = weekendPrice;
    }

    public void setCamp(Camp camp) {
        this.camp = camp;
        if (!camp.getRooms().contains(this))
            camp.getRooms().add(this);
    }

    // 해당 기간 동안의 방 가격 리스트 반환
    public List<Integer> getPrices(LocalDate stayStarts, LocalDate stayEnds) {
        return stayStarts.datesUntil(stayEnds)
                .map(date -> dateToPrice(date))
                .collect(Collectors.toList());
    }

    private int dateToPrice(LocalDate date) {
        if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            return this.weekendPrice;
        }
        return this.weekPrice;
    }


    public void update(Room room) {
        this.name = room.getName();
        this.cnt = room.getCnt();
        this.capacity = room.getCapacity();
        this.weekPrice = room.getWeekPrice();
        this.weekendPrice = room.getWeekendPrice();
    }

    public void setWeekPrice(int weekPrice) {
        this.weekPrice = weekPrice;
    }

    public void setWeekendPrice(int weekendPrice) {
        this.weekendPrice = weekendPrice;
    }
}
