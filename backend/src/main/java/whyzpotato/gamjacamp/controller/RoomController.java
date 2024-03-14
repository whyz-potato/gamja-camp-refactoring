package whyzpotato.gamjacamp.controller;

import java.net.URI;
import java.time.LocalDate;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import whyzpotato.gamjacamp.controller.dto.RoomDto.RemainRoom;
import whyzpotato.gamjacamp.controller.dto.RoomDto.RoomSaveRequest;
import whyzpotato.gamjacamp.controller.dto.RoomDto.RoomSearchResponse;
import whyzpotato.gamjacamp.service.RoomService;

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

    @GetMapping("rooms/{roomId}")
    public ResponseEntity<RemainRoom> roomWithAvailCount(@PathVariable Long roomId,
                                                         @RequestParam(value = "check-in", defaultValue = "#{T(java.time.LocalDateTime).now()}") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkIn,
                                                         @RequestParam(value = "check-out", defaultValue = "#{T(java.time.LocalDateTime).now().plusDays(1)}") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkOut,
                                                         @RequestParam(value = "guests", defaultValue = "2") int numGuest) {

        return ResponseEntity.ok(roomService.availRoomDetail(roomId, checkIn, checkOut, numGuest));
    }

    @GetMapping("/owner/rooms/{roomId}")
    public ResponseEntity<RoomSaveRequest> ownerRoom(@PathVariable Long roomId) {
        return ResponseEntity.ok(roomService.find(roomId));
    }

    @PostMapping("/owner/rooms/{campId}")
    public ResponseEntity<Void> createRoom(@PathVariable Long campId,
                                           @Valid @RequestBody RoomSaveRequest request) {
        URI uri = ServletUriComponentsBuilder.fromUriString("/owner/rooms")
                .path("/{id}")
                .buildAndExpand(roomService.saveRoom(campId, request))
                .toUri();
        return ResponseEntity.created(uri).build();
    }
}
