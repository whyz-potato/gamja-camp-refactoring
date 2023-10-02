package whyzpotato.gamjacamp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whyzpotato.gamjacamp.controller.dto.ReservationDto.ReservationDetail;
import whyzpotato.gamjacamp.controller.dto.ReservationDto.ReservationInfo;
import whyzpotato.gamjacamp.controller.dto.ReservationDto.ReservationRequest;
import whyzpotato.gamjacamp.domain.Camp;
import whyzpotato.gamjacamp.domain.Reservation;
import whyzpotato.gamjacamp.domain.ReservationStatus;
import whyzpotato.gamjacamp.domain.Room;
import whyzpotato.gamjacamp.domain.member.Member;
import whyzpotato.gamjacamp.exception.NotFoundException;
import whyzpotato.gamjacamp.repository.CampRepository;
import whyzpotato.gamjacamp.repository.ReservationRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final MemberService memberService;
    private final RoomService roomService;
    private final CampRepository campRepository;

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


    @Transactional
    public void updateStatus(Long memberId, ReservationStatus status, List<Long> reservationId) {

        Member member = memberService.findById(memberId);
        List<Reservation> reservations = reservationId.stream()
                .map(id -> reservationRepository.findById(id).orElseThrow(NotFoundException::new))
                .collect(Collectors.toList());

        if (status.equals(ReservationStatus.BOOKED)) {
            for (Reservation reservation : reservations) {
                reservation.confirm(member);
            }
        } else if (status.equals(ReservationStatus.CANCELED)) {
            for (Reservation reservation : reservations) {
                reservation.cancel(member);
            }
        }
    }

    @Transactional
    public void cancel(Long memberId, Long reservationId) {

        Member member = memberService.findById(memberId);
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(NotFoundException::new);
        reservation.cancel(member);

    }


    public Page<ReservationInfo> findReservations(Long memberId, Pageable pageable) {
        Member member = memberService.findById(memberId);
        return reservationRepository.findByMemberOrderByStayStartsDesc(member, pageable)
                .map(ReservationInfo::new);
    }

    public Page<ReservationInfo> findReservations(Long ownerId, ReservationStatus status, Pageable pageable) {
        Camp camp = campRepository.findByMember(memberService.findById(ownerId)).orElseThrow(NotFoundException::new);
        return reservationRepository.findByCampAndStatusOrderByStayStartsDesc(camp, status, pageable)
                .map(ReservationInfo::new);
    }

}
