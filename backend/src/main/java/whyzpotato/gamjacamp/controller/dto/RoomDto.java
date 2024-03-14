package whyzpotato.gamjacamp.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import whyzpotato.gamjacamp.domain.Room;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.List;

public class RoomDto {

    @Getter
    @NoArgsConstructor
    public static class RoomSimple {
        private Long id;
        private String name;

        public RoomSimple(Room room) {
            this.id = room.getId();
            this.name = room.getName();
        }
    }

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

        //TODO : 대표 사진

        @Builder
        public RoomSaveRequest(Long id, String name, int cnt, int capacity, int weekPrice, int weekendPrice) {
            this.id = id;
            this.name = name;
            this.cnt = cnt;
            this.capacity = capacity;
            this.weekPrice = weekPrice;
            this.weekendPrice = weekendPrice;
        }

        public RoomSaveRequest(Room room){
            this.id = room.getId();
            this.name = room.getName();
            this.cnt = room.getCnt();
            this.capacity = room.getCapacity();
            this.weekPrice = room.getWeekPrice();
            this.weekendPrice = room.getWeekendPrice();
        }

        public Room toEntity() {
            return Room.builder()
                    .name(name)
                    .cnt(cnt)
                    .capacity(capacity)
                    .weekPrice(weekPrice)
                    .weekendPrice(weekendPrice)
                    .build();
        }
    }


    @Getter
    @NoArgsConstructor
    public static class RoomReserved {
        private Long id;
        private String name;
        private int capacity;
//        private List<String> images;

        public RoomReserved(Room room) {
            this.id = room.getId();
            this.name = room.getName();
            this.capacity = room.getCapacity();
            //this.images = room.getImages(); //TODO 이미지
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RoomDetail {
        private Long id;
        private String name;
        private int capacity;
//        private List<String> images;
        private PriceDto price;

        public RoomDetail(Room room, LocalDate stayStarts, LocalDate stayEnds) {
            this.id = room.getId();
            this.name = room.getName();
            this.capacity = room.getCapacity();
//            this.images = room.getImages(); //TODO 이미지
            this.price = new PriceDto(room.getPrices(stayStarts, stayEnds));
        }
    }

    @Getter
    @NoArgsConstructor
    public static class RoomSearchResponse {
        private LocalDate checkIn;
        private LocalDate checkOut;
        private Long campId;
        private List<RoomDetail> rooms;

        public RoomSearchResponse(LocalDate checkIn, LocalDate checkOut, Long campId, List<RoomDetail> rooms) {
            this.checkIn = checkIn;
            this.checkOut = checkOut;
            this.campId = campId;
            this.rooms = rooms;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class RemainRoom {
        private LocalDate checkIn;
        private LocalDate checkOut;
        private int availCnt;
        private RoomDetail room;

        public RemainRoom(LocalDate checkIn, LocalDate checkOut, int availCnt, RoomDetail room) {
            this.checkIn = checkIn;
            this.checkOut = checkOut;
            this.availCnt = availCnt;
            this.room = room;
        }
    }
}
