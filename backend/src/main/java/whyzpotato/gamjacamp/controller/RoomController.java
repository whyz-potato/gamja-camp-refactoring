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
                                                             @RequestParam(value = "check-in", defaultValue = "#{T(java.time.LocalDateTime).now()}") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkIn,
                                                             @RequestParam(value = "check-out", defaultValue = "#{T(java.time.LocalDateTime).now().plusDays(1)}") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkOut,
                                                             @RequestParam(value = "guests", defaultValue = "2") int numGuest) {

        return ResponseEntity.ok(roomService.findCampAvailableRooms(campId, checkIn, checkOut, numGuest));
    }

//    //curl --location 'localhost:8080/rooms/:id?check-in=null&check-out=null&guests=null'
//    public RoomResponse availableRoom() {
//
//    }
}
