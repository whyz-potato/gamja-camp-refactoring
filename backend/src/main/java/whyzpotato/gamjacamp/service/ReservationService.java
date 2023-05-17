package whyzpotato.gamjacamp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whyzpotato.gamjacamp.domain.Camp;
import whyzpotato.gamjacamp.domain.Reservation;
import whyzpotato.gamjacamp.domain.Room;
import whyzpotato.gamjacamp.domain.member.Member;
import whyzpotato.gamjacamp.repository.ReservationRepository;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;

    //RQ06 : 고객 예약
    @Transactional
    public Reservation reserve(Member member, Camp camp, Room room, int numGuest, LocalDate stayStarts, LocalDate stayEnds, List<Integer> prices) {

        if (!room.getPrices(stayStarts, stayEnds).containsAll(prices))
            throw new IllegalStateException("변경된 정보가 있습니다.");
        if (room.getCapacity() < numGuest)
            throw new IllegalArgumentException("잘못된 접근입니다.");

        Reservation reservation = Reservation.builder()
                .member(member).camp(camp).room(room)
                .numGuest(numGuest)
                .stayStarts(stayStarts)
                .stayEnds(stayEnds)
                .build();
        reservationRepository.save(reservation);

        return reservation;

    }


}
