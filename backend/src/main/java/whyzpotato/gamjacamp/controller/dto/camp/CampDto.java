package whyzpotato.gamjacamp.controller.dto.camp;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import whyzpotato.gamjacamp.domain.Camp;

import java.time.LocalTime;

@Data
@NoArgsConstructor
public class CampDto {

    private Long id;
    private Long memberId;
    private String name;
    private String address;
    private String phone;
    private String campIntroduction;
    private Float campX;
    private Float campY;
    private LocalTime campOperationStart;
    private LocalTime campOperationEnd;

    @Builder
    public CampDto(Long id, Long memberId, String name, String address, String phone, String campIntroduction, Float campX, Float campY, LocalTime campOperationStart, LocalTime campOperationEnd) {
        this.id = id;
        this.memberId = memberId;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.campIntroduction = campIntroduction;
        this.campX = campX;
        this.campY = campY;
        this.campOperationStart = campOperationStart;
        this.campOperationEnd = campOperationEnd;
    }

    public CampDto(Camp camp) {
        this.id = camp.getId();
        this.memberId = camp.getMember().getId();
        this.name = camp.getName();
        this.address = camp.getAddress();
        this.phone = camp.getPhone();
        this.campIntroduction = camp.getCampIntroduction();
        this.campX = camp.getCampX();
        this.campY = camp.getCampY();
        this.campOperationStart = camp.getCampOperationStart();
        this.campOperationEnd = camp.getCampOperationEnd();
    }

}
