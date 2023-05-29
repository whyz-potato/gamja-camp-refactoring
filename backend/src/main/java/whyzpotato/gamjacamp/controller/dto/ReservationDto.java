package whyzpotato.gamjacamp.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import whyzpotato.gamjacamp.controller.dto.CampDto.CampInfo;
import whyzpotato.gamjacamp.controller.dto.MemberDto.MemberSimple;
import whyzpotato.gamjacamp.controller.dto.RoomDto.RoomReserved;
import whyzpotato.gamjacamp.domain.Reservation;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class ReservationDto {

    @Getter
    @NoArgsConstructor
    public static class ReservationSimple{

        private Long id;
        private LocalDate reservationDate;
        private int numGuest;
        private String status;
        private LocalDate stayStarts;
        private LocalDate stayEnds;
        private int price;

        public ReservationSimple(Reservation reservation){
            this.id = reservation.getId();
            this.reservationDate = reservation.getCreatedTime().toLocalDate();
            this.numGuest = reservation.getNumGuest();
            this.status = reservation.getStatus().toString();
            this.stayStarts = reservation.getStayStarts();
            this.stayEnds = reservation.getStayEnds();
            this.price = reservation.getPrice();
        }

    }

    public static class ReservationDetail{
        private LocalDate checkIn;
        private LocalDate checkOut;
        private MemberSimple guest;
        private CampInfo camp;
        private RoomReserved room;
        private ReservationSimple reservation;

        public ReservationDetail(Reservation reservation){
            this.checkIn = reservation.getStayStarts();
            this.checkOut = reservation.getStayEnds();
            this.guest = new MemberSimple(reservation.getMember());
            this.camp = new CampInfo(reservation.getCamp());
            this.room = new RoomReserved(reservation.getRoom());
            this.reservation = new ReservationSimple(reservation);

        }
    }




}
