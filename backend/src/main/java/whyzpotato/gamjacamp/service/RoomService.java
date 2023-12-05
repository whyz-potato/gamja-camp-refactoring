package whyzpotato.gamjacamp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whyzpotato.gamjacamp.controller.dto.CampDto;
import whyzpotato.gamjacamp.controller.dto.PriceDto;
import whyzpotato.gamjacamp.controller.dto.RoomDto;
import whyzpotato.gamjacamp.controller.dto.RoomDto.RoomDetail;
import whyzpotato.gamjacamp.controller.dto.RoomDto.RoomSearchResponse;
import whyzpotato.gamjacamp.domain.Camp;
import whyzpotato.gamjacamp.domain.Room;
import whyzpotato.gamjacamp.exception.NotFoundException;
import whyzpotato.gamjacamp.repository.CampRepository;
import whyzpotato.gamjacamp.repository.RoomRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class RoomService {

    private final RoomRepository roomRepository;
    private final CampRepository campRepository;

    // RQ08 : 사장 객실 등록/수정
    public Room saveRoom(Camp camp, RoomDto.RoomSaveRequest saveDto) {

        Room entity = saveDto.toEntity();

        if (saveDto.getId() != null) {
            Room room = roomRepository.findById(saveDto.getId())
                    .orElseThrow(() -> new NotFoundException());
            room.update(entity);
            return room;
        }

        entity.setCamp(camp);
        return roomRepository.save(entity);
    }

    public Room findById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("잘못된 객실 정보입니다."));
    }

    public RoomSearchResponse findCampAvailableRooms(Long campId, LocalDate checkIn, LocalDate checkOut, int numGuests) {
        Camp camp = campRepository.findById(campId).orElseThrow(IllegalArgumentException::new);
        List<Room> availRooms = roomRepository.findAvailRooms(campId, checkIn, checkOut, numGuests);
        return toRoomSearchResponse(camp, availRooms, checkIn, checkOut);
    }

    private RoomSearchResponse toRoomSearchResponse(Camp camp, List<Room> rooms, LocalDate checkIn, LocalDate checkOut) {
        List<RoomDetail> roomDetails = rooms.stream()
                .map(r -> toRoomDetail(r, checkIn, checkOut))
                .collect(Collectors.toList());
        return new RoomSearchResponse(checkIn, checkOut, camp.getId(), roomDetails);
    }

    private RoomDetail toRoomDetail(Room room, LocalDate stayStarts, LocalDate stayEnds) {
        PriceDto priceDto = new PriceDto(room.getPrices(stayStarts, stayEnds));
        return new RoomDetail(room.getId(), room.getName(), room.getCapacity(), priceDto);
    }
}
