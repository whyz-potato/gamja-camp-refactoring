package whyzpotato.gamjacamp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import whyzpotato.gamjacamp.controller.dto.RoomDto.RoomSearchResponse;
import whyzpotato.gamjacamp.service.RoomService;

import java.time.LocalDate;

@RequiredArgsConstructor
@RestController
public class RoomController {
    private final RoomService roomService;

    @GetMapping("/rooms")
    public ResponseEntity<RoomSearchResponse> availableRooms(@RequestParam("camp") Long campId,
                                                             @RequestParam("check-in") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkIn,
                                                             @RequestParam("check-out") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkOut,
                                                             @RequestParam("guests") int numGuest) {

        return ResponseEntity.ok(roomService.findCampAvailableRooms(campId, checkIn, checkOut, numGuest));
    }


//    //curl --location 'localhost:8080/rooms/:id?check-in=null&check-out=null&guests=null'
//    public RoomResponse availableRoom() {
//
//    }
}
