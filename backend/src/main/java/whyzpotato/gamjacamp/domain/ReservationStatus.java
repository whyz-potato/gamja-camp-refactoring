package whyzpotato.gamjacamp.domain;

public enum ReservationStatus {
    PENDING, // 신청(대기)
    BOOKED,  // 예약확정
    CANCELED, // 취소
    COMPLETED // 이용완료
}
