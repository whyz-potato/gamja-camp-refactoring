package whyzpotato.gamjacamp.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import whyzpotato.gamjacamp.controller.dto.Utility.PageResult;
import whyzpotato.gamjacamp.domain.Camp;
import whyzpotato.gamjacamp.domain.Coordinate;
import whyzpotato.gamjacamp.domain.Image;
import whyzpotato.gamjacamp.domain.member.Member;
import whyzpotato.gamjacamp.repository.querydto.CampQueryDto;

import javax.validation.constraints.NotBlank;
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
    public static class CampSearchItem {

        private Long id;
        private String name;
        private String address;
        private int price;
        private double latitude;
        private double longitude;
        private String mainImage;
        private double rate;

        public CampSearchItem(CampQueryDto campQueryDto) {
            this.id = campQueryDto.getCamp_Id();
            this.name = campQueryDto.getName();
            this.address = campQueryDto.getAddress();
            this.price = campQueryDto.getMin_Price();
            this.latitude = campQueryDto.getLatitude();
            this.longitude = campQueryDto.getLongitude();
            this.mainImage = campQueryDto.getImage();
            this.rate = campQueryDto.getRate();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class CampSearchResult<T> extends PageResult<T> {
        private LocalDate checkIn;
        private LocalDate checkOut;

        public CampSearchResult(Page<T> page, LocalDate checkIn, LocalDate checkOut) {
            super(page);
            this.checkIn = checkIn;
            this.checkOut = checkOut;
        }
    }


    @Getter
    @NoArgsConstructor
    public static class  CampSaveRequest {
        @NotBlank
        private String name;
        @NotBlank
        private String address;
        private String phone;
        private String campIntroduction;

        @Builder
        public CampSaveRequest(String name, String address, String phone, String campIntroduction) {
            this.name = name;
            this.address = address;
            this.phone = phone;
            this.campIntroduction = campIntroduction;
        }

        public Camp toEntity(Member member, Coordinate coordinate) {
            return Camp.builder()
                    .member(member)
                    .name(this.name)
                    .address(this.address)
                    .phone(this.phone)
                    .campIntroduction(this.campIntroduction)
                    .longitude(coordinate.getLongitude())
                    .latitude(coordinate.getLatitude())
                    .build();
        }
    }


    @Getter
    @NoArgsConstructor
    public static class CampUpdateRequest {
        @NotBlank
        private String name;
        private String phone;
        private String campIntroduction;
        private String campOperationStart;
        private String campOperationEnd;

        @Builder
        public CampUpdateRequest(String name, String phone, String campIntroduction, String campOperationStart, String campOperationEnd) {
            this.name = name;
            this.phone = phone;
            this.campIntroduction = campIntroduction;
            this.campOperationStart = campOperationStart;
            this.campOperationEnd = campOperationEnd;
        }
    }
}
