package whyzpotato.gamjacamp.controller.dto.camp;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class CampUpdateRequestDto {
    @NotBlank
    private String name;
    private String phone;
    private String campIntroduction;

    @Builder
    public CampUpdateRequestDto(String name, String phone, String campIntroduction) {
        this.name = name;
        this.phone = phone;
        this.campIntroduction = campIntroduction;
    }
}
