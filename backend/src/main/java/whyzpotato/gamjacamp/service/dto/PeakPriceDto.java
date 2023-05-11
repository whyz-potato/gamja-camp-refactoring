package whyzpotato.gamjacamp.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import whyzpotato.gamjacamp.domain.PeakPrice;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class PeakPriceDto {

    @Positive
    private int price;

    @NotNull
    private LocalDate peakStart;

    @NotNull
    private LocalDate peakEnd;

    @Builder
    public PeakPriceDto(int price, LocalDate peakStart, LocalDate peakEnd) {
        this.price = price;
        this.peakStart = peakStart;
        this.peakEnd = peakEnd;
    }

    public PeakPrice toEntity(){
        return PeakPrice.builder()
                .peakPrice(price)
                .peakStart(peakStart)
                .peakEnd(peakEnd)
                .build();
    }

}
