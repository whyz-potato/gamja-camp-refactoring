package whyzpotato.gamjacamp.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import whyzpotato.gamjacamp.controller.dto.RoomDto;
import whyzpotato.gamjacamp.controller.dto.RoomDto.RoomSearchResponse;
import whyzpotato.gamjacamp.domain.Camp;
import whyzpotato.gamjacamp.domain.Room;
import whyzpotato.gamjacamp.domain.member.Member;
import whyzpotato.gamjacamp.domain.member.Role;
import whyzpotato.gamjacamp.exception.NotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RoomServiceTest {
    private static Validator validator;

    @Autowired
    private RoomService roomService;

    @PersistenceContext
    private EntityManager em;

    private Member member;

    private Camp camp;

    @BeforeAll
    public static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeEach
    public void setUp() {
        member = Member.builder().account("account@mail.com").role(Role.OWNER).username("tester").picture("img").build();
        em.persist(member);
        camp = Camp.builder().member(member).name("tester camp").address("경기도 남양주시").longitude(1.1).latitude(1.1).build();
        em.persist(camp);

    }

    @DisplayName("객실 저장 성공(기본)")
    @Test
    public void saveRoom() {
        //given
        RoomDto.RoomSaveRequest roomSaveRequest = RoomDto.RoomSaveRequest.builder()
                .name("room")
                .cnt(10)
                .capacity(3)
                .weekendPrice(1000)
                .weekPrice(1200)
                .build();

        //when
        Long roomId = roomService.saveRoom(camp.getId(), roomSaveRequest);

        //then
        assertThat(roomId).isNotNull();

    }

    @DisplayName("객실 정보 수정 성공")
    @Test
    public void updateRoom() {
        //given
        RoomDto.RoomSaveRequest roomSaveRequest = RoomDto.RoomSaveRequest.builder()
                .name("room")
                .cnt(10)
                .capacity(3)
                .weekendPrice(1000)
                .weekPrice(1200)
                .build();
        Long saveId = roomService.saveRoom(camp.getId(), roomSaveRequest);

        //when
        RoomDto.RoomSaveRequest updateRoomSaveRequest = RoomDto.RoomSaveRequest.builder()
                .id(saveId)
                .name("room")
                .cnt(5)
                .capacity(1)
                .weekendPrice(2000)
                .weekPrice(8000)
                .build();
        Long updateId = roomService.saveRoom(camp.getId(), updateRoomSaveRequest);


        //that
        assertThat(saveId).isEqualTo(updateId);

    }

    @DisplayName("객실 정보 수정 실패 : 존재하지 않는 아이디")
    @Test
    public void updateRoom_wrongId_fail() {

        //given
        RoomDto.RoomSaveRequest updateRoomSaveRequest = RoomDto.RoomSaveRequest.builder()
                .id(30000L)
                .name("room")
                .cnt(5)
                .capacity(1)
                .weekendPrice(2000)
                .weekPrice(8000)
                .build();

        //then
        assertThrows(NotFoundException.class, () -> roomService.saveRoom(camp.getId(), updateRoomSaveRequest));

    }

    @DisplayName("예약가능한 객실 검색 성공")
    @Test
    public void findAvailableRooms() {
        em.persist(Room.builder().name("room1").camp(camp).capacity(2).cnt(2).weekPrice(7_000).weekendPrice(10_000)
                .build());
        em.persist(Room.builder().name("room2").camp(camp).capacity(2).cnt(2).weekPrice(8_000).weekendPrice(12_000)
                .build());
        em.persist(Room.builder().name("room3").camp(camp).capacity(4).cnt(1).weekPrice(10_000).weekendPrice(15_000)
                .build());
        em.persist(Room.builder().name("room4").camp(camp).capacity(6).cnt(1).weekPrice(15_000).weekendPrice(20_000)
                .build());

        RoomSearchResponse result = roomService.findCampAvailableRooms(camp.getId(), LocalDate.now(), LocalDate.now().plusDays(1), 2);

        assertThat(result.getRooms().size()).isEqualTo(4);
        assertThat(result.getRooms()).extracting("name").containsOnly("room1", "room2", "room3", "room4");
    }
}
