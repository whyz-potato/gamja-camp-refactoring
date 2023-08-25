package whyzpotato.gamjacamp.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import whyzpotato.gamjacamp.controller.dto.CampDto.CampInfo;
import whyzpotato.gamjacamp.controller.dto.MemberDto.MemberSimple;
import whyzpotato.gamjacamp.controller.dto.RoomDto.RoomReserved;
import whyzpotato.gamjacamp.domain.Reservation;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
public class ReservationDto {


    @Getter
    @NoArgsConstructor
    public static class ReservationInfo {

        private Long id;
        private LocalDate reservationDate;
        private int numGuest;
        private String status;
        private LocalDate stayStarts;
        private LocalDate stayEnds;
        private int price;

        public ReservationInfo(Reservation reservation) {
            this.id = reservation.getId();
            this.reservationDate = reservation.getCreatedTime().toLocalDate();
            this.numGuest = reservation.getNumGuest();
            this.status = reservation.getStatus().toString();
            this.stayStarts = reservation.getStayStarts();
            this.stayEnds = reservation.getStayEnds();
            this.price = reservation.getPrice();
        }

    }

    @Getter
    @NoArgsConstructor
    public static class ReservationDetail {
        private LocalDate checkIn;
        private LocalDate checkOut;
        private MemberSimple guest;
        private CampInfo camp;
        private RoomReserved room;
        private ReservationInfo reservation;

        public ReservationDetail(Reservation reservation) {
            this.checkIn = reservation.getStayStarts();
            this.checkOut = reservation.getStayEnds();
            this.guest = new MemberSimple(reservation.getMember());
            this.camp = new CampInfo(reservation.getCamp());
            this.room = new RoomReserved(reservation.getRoom());
            this.reservation = new ReservationInfo(reservation);

        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReservationSimple {
        @NotNull
        private int numGuest;
        @NotNull
        private List<Integer> dailyPrice;
    }


    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReservationRequest {

        @NotNull
        private Long campId;

        @NotNull
        private Long roomId;

        @NotNull
        private LocalDate checkIn;

        @NotNull
        private LocalDate checkOut;

        @NotNull
        private MemberSimple guest;

        @NotNull
        private ReservationSimple reservation;

    }


}
