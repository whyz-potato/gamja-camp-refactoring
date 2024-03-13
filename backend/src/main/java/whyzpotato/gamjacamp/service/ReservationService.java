package whyzpotato.gamjacamp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whyzpotato.gamjacamp.controller.dto.ReservationDto;
import whyzpotato.gamjacamp.controller.dto.ReservationDto.ReservationDetail;
import whyzpotato.gamjacamp.controller.dto.ReservationDto.ReservationInfo;
import whyzpotato.gamjacamp.controller.dto.ReservationDto.ReservationListItem;
import whyzpotato.gamjacamp.controller.dto.ReservationDto.ReservationRequest;
import whyzpotato.gamjacamp.domain.Camp;
import whyzpotato.gamjacamp.domain.Reservation;
import whyzpotato.gamjacamp.domain.ReservationStatus;
import whyzpotato.gamjacamp.domain.Room;
import whyzpotato.gamjacamp.domain.member.Member;
import whyzpotato.gamjacamp.exception.NotFoundException;
import whyzpotato.gamjacamp.repository.CampRepository;
import whyzpotato.gamjacamp.repository.ReservationRepository;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
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
        Reservation reservation = Reservation.builder()
                .member(member).camp(room.getCamp()).room(room)
                .numGuest(request.getReservation().getNumGuest())
                .stayStarts(request.getCheckIn())
                .stayEnds(request.getCheckOut())
                .prices(request.getReservation().getDailyPrice())
                .build();

        Long count = reservationRepository.countByRoomAndStayEndsGreaterThanAndStayStartsLessThan(room, request.getCheckIn(), request.getCheckOut());
        if(room.getCnt() <= count){
            throw new IllegalStateException("변경된 예약정보가 있습니다.");
        }

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


    public Page<ReservationListItem> findCustomerReservations(Long memberId, Pageable pageable) {
        Member member = memberService.findById(memberId);
        return reservationRepository.findByMemberOrderByStayStartsDesc(member, pageable)
                .map(ReservationListItem::new);
    }

    public Page<ReservationInfo> findCampReservations(Long ownerId, ReservationStatus status, Pageable pageable) {
        Camp camp = campRepository.findByMember(memberService.findById(ownerId)).orElseThrow(NotFoundException::new);
        return reservationRepository.findByCampAndStatusOrderByStayStartsDesc(camp, status, pageable)
                .map(ReservationInfo::new);
    }

    public ReservationDetail findCampReservation(Long reservationId, Long ownerId){

        //값이 있는 경우에는 -> 해당 예약을 조회할 수 있는 경우에만 값을 통과
        Reservation reservation = reservationRepository.findById(reservationId)
                .filter(r -> r.getCamp().getMember().getId() == ownerId)
                .orElseThrow(NotFoundException::new);

        return new ReservationDetail(reservation);
    }
}
