package whyzpotato.gamjacamp.controller.dto.camp;

import lombok.Builder;
import lombok.Getter;
import whyzpotato.gamjacamp.domain.Camp;
import whyzpotato.gamjacamp.domain.Coordinate;
import whyzpotato.gamjacamp.domain.member.Member;

import javax.validation.constraints.NotBlank;

@Getter
public class CampSaveRequestDto {
    @NotBlank
    private String name;
    @NotBlank
    private String address;
    private String phone;
    private String campIntroduction;

    @Builder
    public CampSaveRequestDto(String name, String address, String phone, String campIntroduction) {
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
