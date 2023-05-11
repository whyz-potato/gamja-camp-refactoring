package whyzpotato.gamjacamp.service.dto;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import whyzpotato.gamjacamp.domain.Room;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class RoomSave {

    private Long id;

    @NotBlank
    private String name;

    @Positive
    private int cnt;

    @Positive
    private int capacity;

    @Positive
    private int weekPrice;

    @Positive
    private int weekendPrice;

    private List<PeakPriceDto> peakPrices;

    //TODO : 대표 사진


    @Builder
    public RoomSave(Long id, String name, int cnt, int capacity, int weekPrice, int weekendPrice, List<PeakPriceDto> peakPrices) {
        this.id = id;
        this.name = name;
        this.cnt = cnt;
        this.capacity = capacity;
        this.weekPrice = weekPrice;
        this.weekendPrice = weekendPrice;
        this.peakPrices = peakPrices;
    }

    public Room toEntity() {

        return Room.builder()
                .name(name)
                .cnt(cnt)
                .capacity(capacity)
                .weekPrice(weekPrice)
                .weekendPrice(weekendPrice)
                .peakPrices(Optional.ofNullable(peakPrices)
                        .orElseGet(Collections::emptyList)
                        .stream()
                        .map(dto -> dto.toEntity()).collect(Collectors.toList()))
                .build();

    }


}
