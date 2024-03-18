package whyzpotato.gamjacamp.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import whyzpotato.gamjacamp.config.auth.LoginMember;
import whyzpotato.gamjacamp.config.auth.dto.SessionMember;
import whyzpotato.gamjacamp.controller.dto.ReservationDto.*;
import whyzpotato.gamjacamp.controller.dto.Utility.PageResult;
import whyzpotato.gamjacamp.domain.ReservationStatus;
import whyzpotato.gamjacamp.service.ReservationService;

import javax.validation.Valid;
import java.net.URI;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ReservationController {

    private final ReservationService reservationService;


    @GetMapping("/customer/reservations/{id}")
    public ReservationDetail reservationDetail(@LoginMember SessionMember sessionMember,
                                               @PathVariable Long id) {
        return reservationService.findReservation(id, sessionMember.getId());
    }


    @PostMapping("/customer/reservations")
    public ResponseEntity<?> book(@LoginMember SessionMember sessionMember,
                                  @Valid @RequestBody ReservationRequest request) {


        Long id = reservationService.createReservation(sessionMember.getId(), request);
        URI uri = ServletUriComponentsBuilder.fromUriString("/customer/reservations")
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
        return ResponseEntity.created(uri).build();
    }


    @DeleteMapping("/customer/reservations/{id}")
    public ResponseEntity<Void> cancelReservation(@LoginMember SessionMember sessionMember,
                                            @PathVariable Long id) {

        reservationService.cancel(sessionMember.getId(), id);
        return ResponseEntity.noContent().build();

    }

    @GetMapping("/customer/reservations/my")
    public ResponseEntity<PageResult<ReservationListItem>> reservationList(@LoginMember SessionMember sessionMember,
                                             @PageableDefault(size = 10) Pageable pageable) {

        Page<ReservationListItem> customerReservations = reservationService.findCustomerReservations(sessionMember.getId(), pageable);
        return ResponseEntity.ok(new PageResult<>(customerReservations));

    }

    @GetMapping("/owner/reservations")
    public ResponseEntity<PageResult<ReservationInfo>> campReservationList(@LoginMember SessionMember sessionMember,
                                                 @RequestParam(value = "status") ReservationStatus status,
                                                 @PageableDefault(size = 10) Pageable pageable) {

        Page<ReservationInfo> campReservations = reservationService.findCampReservations(sessionMember.getId(), status, pageable);
        return ResponseEntity.ok(new PageResult<>(campReservations));
    }

    @GetMapping("/owner/reservations/{id}")
    public ResponseEntity<?> campReservationDetail(@LoginMember SessionMember sessionMember,
                                                   @PathVariable Long id) {

        return ResponseEntity.ok(reservationService.findCampReservation(id, sessionMember.getId()));
    }

    @PostMapping("/owner/reservations/status")
    public ResponseEntity<Void> changeReservationStatus(@LoginMember SessionMember sessionMember,
                                                  @Valid @RequestBody StatusMultipleRequest request) {

        reservationService.updateStatus(sessionMember.getId(), request.getStatus(), request.getReservations());
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create("/owner/reservations")).build();
    }

}
