package whyzpotato.gamjacamp.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import whyzpotato.gamjacamp.controller.dto.Utility.PageResult;
import whyzpotato.gamjacamp.domain.Camp;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class CampDto {

    @Getter
    @NoArgsConstructor
    public static class CampSimple {

        private Long id;
        private String name;

        public CampSimple(Camp camp) {
            this.id = camp.getId();
            this.name = camp.getName();
        }

    }


    @Getter
    @NoArgsConstructor
    public static class CampSearch {

        private Long id;
        private String name;
        private double rate;
        private List<String> images;
        private double latitude;
        private double longitude;

        public CampSearch(Camp camp) {
            this.id = camp.getId();
            this.name = camp.getName();
            this.rate = camp.getRate();
            this.images = List.of("https://picsum.photos/300", "https://picsum.photos/300", "https://picsum.photos/300"); //TODO dummy -> image
            this.latitude = camp.getLatitude();
            this.longitude = camp.getLongitude();
        }

    }


    @Getter
    @NoArgsConstructor
    public static class CampInfo {

        private Long id;
        private String name;
        private List<String> images;
        private String address;
        private String contact;
        private LocalTime checkInTime;
        private LocalTime checkOutTime;

        public CampInfo(Camp camp) {
            this.id = camp.getId();
            this.name = camp.getName();
            //this.images = camp.getImages(); //TODO image
            this.address = camp.getAddress();
            this.contact = camp.getPhone();
            this.checkInTime = camp.getCampOperationStart();
            this.checkOutTime = camp.getCampOperationEnd();
        }

    }


    @Getter
    @NoArgsConstructor
    public static class CampDetail {

        private Long id;
        private String name;
        private double rate;
        private List<String> images;
        private double latitude;
        private double longitude;
        private String address;
        private String contact;
        private LocalTime checkInTime;
        private LocalTime checkOutTime;
        private String introduction;

        public CampDetail(Camp camp) {
            this.id = camp.getId();
            this.name = camp.getName();
            this.rate = camp.getRate();
            //this.images = camp.getImages(); //TODO image
            this.latitude = camp.getLatitude();
            this.longitude = camp.getLongitude();
            this.address = camp.getAddress();
            this.contact = camp.getPhone();
            this.checkInTime = camp.getCampOperationStart();
            this.checkOutTime = camp.getCampOperationEnd();
            this.introduction = camp.getCampIntroduction();

        }

    }

    @Getter
    @NoArgsConstructor
    public static class SearchItem{
        private CampSearch camp;
        private PriceDto price;

        public SearchItem(Camp camp, List<Integer> dailyPrice) {
            this.camp = new CampSearch(camp);
            this.price = new PriceDto(dailyPrice);
        }
    }

    @Getter
    @NoArgsConstructor
    public static class SearchResult<T> extends PageResult<T>{
        private LocalDate checkIn;
        private LocalDate checkOut;

        public SearchResult(Page<T> page, LocalDate checkIn, LocalDate checkOut) {
            super(page);
            this.checkIn = checkIn;
            this.checkOut = checkOut;
        }
    }

}
