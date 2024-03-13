package whyzpotato.gamjacamp.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ReservationStatus {
    PENDING("pending", "대기"),
    BOOKED("confirm", "확정"),
    CANCELED("cancel", "취소"),
    COMPLETED("complete", "이용완료");

    private final String value;

    @JsonValue
    private final String description;

    @JsonCreator
    public static ReservationStatus from(String sub) {
        for (ReservationStatus status : ReservationStatus.values()) {
            if (status.getValue().equals(sub) || status.getDescription().equals(sub))
                return status;
        }

        throw new IllegalArgumentException();
    }

}
