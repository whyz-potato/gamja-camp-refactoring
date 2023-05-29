package whyzpotato.gamjacamp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import whyzpotato.gamjacamp.controller.dto.ReservationDto.ReservationDetail;
import whyzpotato.gamjacamp.service.ReservationService;

@RequiredArgsConstructor
@RestController
public class ReservationController {

    private final ReservationService reservationService;


    //TODO 예약 내역
//    @GetMapping("/reservations")

    @GetMapping("/reservations/{id}")
    public ReservationDetail reservationDetail(@PathVariable Long id) {
        return new ReservationDetail(reservationService.findById(id));
    }


}
