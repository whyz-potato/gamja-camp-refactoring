package whyzpotato.gamjacamp.controller.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import whyzpotato.gamjacamp.domain.Camp;
import whyzpotato.gamjacamp.domain.Coordinate;
import whyzpotato.gamjacamp.domain.Image;
import whyzpotato.gamjacamp.domain.member.Member;

import javax.validation.constraints.NotBlank;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
public class CampDto {

    // CampSimple
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

    // CampInfo
    @Getter
    @NoArgsConstructor
    public static class CampInfo {
        private Long id;
        private String name;
        private String address;
        private String phone;
        private String campIntroduction;
        private LocalTime campOperationStart;
        private LocalTime campOperationEnd;

        public CampInfo(Camp camp) {
            this.id = camp.getId();
            this.name = camp.getName();
            this.address = camp.getAddress();
            this.phone = camp.getPhone();
            this.campIntroduction = camp.getCampIntroduction();
            this.campOperationStart = camp.getCampOperationStart();
            this.campOperationEnd = camp.getCampOperationEnd();
        }
    }

    // CampDetail
    @Getter
    @NoArgsConstructor
    public static class CampDetail {
        private Long id;
        private Long memberId;
        private String name;
        private String address;
        private String phone;
        private String campIntroduction;
        private double longitude;
        private double latitude;
        private LocalTime campOperationStart;
        private LocalTime campOperationEnd;
        private List<Image> images;

        public CampDetail(Camp camp) {
            this.id = camp.getId();
            this.memberId = camp.getMember().getId();
            this.name = camp.getName();
            this.address = camp.getAddress();
            this.phone = camp.getPhone();
            this.campIntroduction = camp.getCampIntroduction();
            this.longitude = camp.getLongitude();
            this.latitude = camp.getLatitude();
            this.campOperationStart = camp.getCampOperationStart();
            this.campOperationEnd = camp.getCampOperationEnd();
            this.images = camp.getImages();
        }
    }


    // CampSaveRequest
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
                    .longitude(coordinate.getCampX())
                    .latitude(coordinate.getCampY())
                    .build();
        }
    }

    // CampUpdatedRequest
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
