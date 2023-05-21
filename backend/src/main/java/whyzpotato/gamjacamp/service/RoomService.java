package whyzpotato.gamjacamp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whyzpotato.gamjacamp.controller.dto.RoomDto;
import whyzpotato.gamjacamp.domain.Camp;
import whyzpotato.gamjacamp.domain.Room;
import whyzpotato.gamjacamp.exception.NotFoundException;
import whyzpotato.gamjacamp.repository.RoomRepository;

@RequiredArgsConstructor
@Service
@Transactional
public class RoomService {

    private final RoomRepository roomRepository;

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

    public Room findRoom(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("잘못된 객실 정보입니다."));
    }


//    // RQ28 : 고객 특정 캠핑장의 예약 가능한 객실들 조회
//    public List<Room> saveRoom(Camp camp, RoomSearchParam param){
//        return roomRepository.findAllAvailableRooms(camp, param.getNumGuest(), param.getStayStarts(), param.getStayEnds());
//    }


}
