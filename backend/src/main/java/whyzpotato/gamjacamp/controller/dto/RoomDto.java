package whyzpotato.gamjacamp.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import whyzpotato.gamjacamp.domain.Room;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

public class RoomDto {


    @Getter
    @NoArgsConstructor
    public static class RoomSaveRequest {

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
        public RoomSaveRequest(Long id, String name, int cnt, int capacity, int weekPrice, int weekendPrice, List<PeakPriceDto> peakPrices) {
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


    @Getter
    @NoArgsConstructor
    public static class RoomResponse {

        private String name;
        private List<Integer> prices;
        private int minPrice;
        private int capacity;

        public RoomResponse(Room room) {
            this.name = room.getName();
            this.capacity = room.getCapacity();
        }

        public RoomResponse(Room room, LocalDate start, LocalDate end) {
            this.name = room.getName();
            this.prices = room.getPrices(start, end);
            this.minPrice = Collections.min(prices);
            this.capacity = room.getCapacity();
        }


    }

    //실제로는 path variable로 받을 예정
    @Getter
    @NoArgsConstructor
    public static class RoomSearchParam {

        private int numGuest;
        private LocalDate stayStarts;
        private LocalDate stayEnds;

        public RoomSearchParam(int numGuest, LocalDate stayStarts, LocalDate stayEnds) {
            this.numGuest = ofNullable(numGuest).orElse(1);
            this.stayStarts = ofNullable(stayStarts).orElse(LocalDate.now());
            this.stayEnds = ofNullable(stayStarts).orElse(LocalDate.now().plusDays(1));
        }

    }
}
