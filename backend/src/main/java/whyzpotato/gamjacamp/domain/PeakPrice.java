package whyzpotato.gamjacamp.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import whyzpotato.gamjacamp.utils.Utils;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "peak_price")
public class PeakPrice {

    @Id
    @GeneratedValue
    @Column(name = "peak_price_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @Min(1)
    private int peakPrice;

    @NotNull
    private LocalDate peakStart;

    @NotNull
    private LocalDate peakEnd;

    @Builder
    public PeakPrice(Room room, Integer peakPrice, LocalDate peakStart, LocalDate peakEnd) {
        this.room = room;
        this.peakPrice = peakPrice;
        this.peakStart = peakStart;
        this.peakEnd = peakEnd;
    }

    public boolean isPeakDate(LocalDate date) {
        return Utils.isBetween(date, peakStart, peakEnd);
    }


}
