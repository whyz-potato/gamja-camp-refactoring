package whyzpotato.gamjacamp.dto.camp;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class CampUpdateRequestDto {
    @NotBlank
    private String name;
    private String phone;
    private String campIntroduction;
    private String campOperationStart;
    private String campOperationEnd;

    @Builder
    public CampUpdateRequestDto(String name, String phone, String campIntroduction, String campOperationStart, String campOperationEnd) {
        this.name = name;
        this.phone = phone;
        this.campIntroduction = campIntroduction;
        this.campOperationStart = campOperationStart;
        this.campOperationEnd = campOperationEnd;
    }
}
