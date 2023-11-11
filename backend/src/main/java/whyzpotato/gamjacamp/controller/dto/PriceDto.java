package whyzpotato.gamjacamp.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.IntStream;

@Getter
@Setter
@NoArgsConstructor
public class PriceDto {

    private int minOneNightPrice;
    private List<Integer> dailyPrices;
    private int totalPrice;

    public PriceDto(List<Integer> dailyPrices) {
        if(dailyPrices.isEmpty())
            throw new IllegalArgumentException("dailyPrices는 숙박 기간만큼의 원소를 가져야한다.");

        this.dailyPrices = dailyPrices;
        this.minOneNightPrice = dailyPrices.stream().mapToInt(Integer::intValue).min().orElseThrow(IllegalArgumentException::new);
        this.totalPrice = dailyPrices.stream().mapToInt(Integer::intValue).sum();
    }
}
