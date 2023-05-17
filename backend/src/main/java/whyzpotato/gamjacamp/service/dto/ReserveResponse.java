package whyzpotato.gamjacamp.service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import whyzpotato.gamjacamp.domain.Camp;
import whyzpotato.gamjacamp.domain.Reservation;
import whyzpotato.gamjacamp.service.dto.RoomDto.RoomResponse;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class ReserveResponse {

    private CampDto camp;
    private RoomResponse room;
    private ReservationDetail reservation;

    public ReserveResponse(Reservation reservation){
        this.camp = new CampDto(reservation.getCamp());
        this.room = new RoomResponse(reservation.getRoom());
        this.reservation = new ReservationDetail(reservation);
    }


    //TODO : 서영님의 CampDto로 나중에 교체
    @Getter
    @NoArgsConstructor
    private class CampDto{
        private String name;
        private String address;
        private String info;

        public CampDto(Camp camp){
            this.name = camp.getName();
            this.address = camp.getAddress();
            this.info = camp.getCampIntroduction();
        }
    }

    @Getter
    @NoArgsConstructor
    public class ReservationDetail{
        private Long id;
        private String status;
        private int numGuest;
        private LocalDate stayStarts;
        private LocalDate stayEnds;

        public ReservationDetail(Reservation reservation){
            this.id = reservation.getId();;
            this.status = reservation.getStatus().toString();
            this.numGuest = reservation.getNumGuest();
            this.stayStarts = reservation.getStayStarts();
            this.stayEnds = reservation.getStayEnds();
        }

    }



}
