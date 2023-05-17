package whyzpotato.gamjacamp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whyzpotato.gamjacamp.repository.ReservationRepository;
import whyzpotato.gamjacamp.service.dto.ReserveResponse;

@RequiredArgsConstructor
@Service
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;

    // RQ06 : 고객 예약
//    public ReserveResponse reserve(){
//
//    }


}
