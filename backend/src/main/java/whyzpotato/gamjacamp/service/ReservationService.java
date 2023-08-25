package whyzpotato.gamjacamp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whyzpotato.gamjacamp.controller.dto.ReservationDto.ReservationDetail;
import whyzpotato.gamjacamp.controller.dto.ReservationDto.ReservationRequest;
import whyzpotato.gamjacamp.domain.Camp;
import whyzpotato.gamjacamp.domain.Reservation;
import whyzpotato.gamjacamp.domain.Room;
import whyzpotato.gamjacamp.domain.member.Member;
import whyzpotato.gamjacamp.exception.NotFoundException;
import whyzpotato.gamjacamp.repository.ReservationRepository;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final MemberService memberService;
    private final RoomService roomService;

    //RQ31 : 예약 상세 정보
    public ReservationDetail findReservation(Long reservationId, Long memberId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(NotFoundException::new);
        if (!reservation.getMember().getId().equals(memberId))
            throw new NotFoundException();

        return new ReservationDetail(reservation);
    }

    //RQ06 : 고객 예약
    @Transactional
    public Reservation createReservation(Member member, Camp camp, Room room, int numGuest, LocalDate stayStarts, LocalDate stayEnds, List<Integer> prices) {

        Reservation reservation = Reservation.builder()
                .member(member).camp(camp).room(room)
                .numGuest(numGuest)
                .stayStarts(stayStarts)
                .stayEnds(stayEnds)
                .prices(prices)
                .build();

        reservationRepository.save(reservation);

        return reservation;

    }

    //RQ06 : 고객 예약
    @Transactional
    public Long createReservation(Long memberId, ReservationRequest request) {
        Member member = memberService.findById(memberId);
        Room room = roomService.findById(request.getRoomId());
        Camp camp = room.getCamp();

        if (!camp.getId().equals(request.getCampId()))
            throw new IllegalArgumentException("잘못된 접근입니다.");

        Reservation reservation = Reservation.builder()
                .member(member).camp(camp).room(room)
                .numGuest(request.getReservation().getNumGuest())
                .stayStarts(request.getCheckIn())
                .stayEnds(request.getCheckOut())
                .prices(request.getReservation().getDailyPrice())
                .build();

        reservationRepository.save(reservation);
        return reservation.getId();
    }


}
