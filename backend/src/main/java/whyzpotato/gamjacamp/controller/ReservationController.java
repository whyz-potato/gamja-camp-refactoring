package whyzpotato.gamjacamp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import whyzpotato.gamjacamp.config.auth.LoginMember;
import whyzpotato.gamjacamp.config.auth.dto.SessionMember;
import whyzpotato.gamjacamp.controller.dto.ReservationDto.ReservationDetail;
import whyzpotato.gamjacamp.controller.dto.ReservationDto.ReservationRequest;
import whyzpotato.gamjacamp.service.ReservationService;

import javax.validation.Valid;
import java.net.URI;

@RequiredArgsConstructor
@RestController
public class ReservationController {

    private final ReservationService reservationService;


    @GetMapping("/reservations/{id}")
    public ReservationDetail reservationDetail(@LoginMember SessionMember sessionMember,
                                               @PathVariable Long id) {
        return reservationService.findByIdAndMember(id, sessionMember.getId());
    }


    @PostMapping("/reservations")
    public ResponseEntity<?> book(@LoginMember SessionMember sessionMember,
                                  @RequestParam("camp") Long campId,
                                  @RequestParam("room") Long roomId,
                                  @Valid @RequestBody ReservationRequest request) {

        Long id = reservationService.createReservation(sessionMember.getId(), campId, roomId, request);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(URI.create("/reservations/" + id));
        return new ResponseEntity<>(httpHeaders, HttpStatus.FOUND);
    }
}
