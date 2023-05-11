package whyzpotato.gamjacamp.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import whyzpotato.gamjacamp.domain.Camp;
import whyzpotato.gamjacamp.domain.Room;
import whyzpotato.gamjacamp.domain.member.Member;
import whyzpotato.gamjacamp.domain.member.Role;
import whyzpotato.gamjacamp.exception.NotFoundException;
import whyzpotato.gamjacamp.service.dto.PeakPriceDto;
import whyzpotato.gamjacamp.service.dto.RoomSave;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

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
    public void initEach() {
        member = Member.builder()
                .account("account@mail.com")
                .role(Role.ROLE_OWNER)
                .username("tester")
                .picture("img")
                .build();
        em.persist(member);

        camp = Camp.builder()
                .member(member)
                .name("tester camp")
                .address("경기도 남양주시")
                .campX((float) 1.1)
                .campY((float) 1.1)
                .build();
        em.persist(camp);

    }

    @DisplayName("객실 저장 테스트 (기본)")
    @Test
    public void saveRoom() {
        //given
        RoomSave roomSave = RoomSave.builder()
                .name("room")
                .cnt(10)
                .capacity(3)
                .weekendPrice(1000)
                .weekPrice(1200)
                .build();

        //when
        Room room = roomService.saveRoom(camp, roomSave);

        //then
        assertThat(room.getId()).isNotNull();

    }

    @DisplayName("객실 저장 실패 테스트 : 필수 정보 누락")
    @Test
    public void saveRoom_withoutRequireParam_fail() {
        //given
        RoomSave roomSave = RoomSave.builder()
                .cnt(1)
                .build();

        //when
        Room room = roomService.saveRoom(camp, roomSave);
        Set<ConstraintViolation<Room>> constraintViolations = validator.validate(room);

        //then
        assertThat(constraintViolations.size()).isNotEqualTo(0);
    }


    @DisplayName("PeakPrice 영속성 전이 테스트")
    @Test
    public void saveRoom_withPeakPrice() {
        // given
        List<PeakPriceDto> list = new ArrayList<>();
        list.add(PeakPriceDto.builder()
                .price(1)
                .peakStart(LocalDate.now())
                .peakEnd(LocalDate.now())
                .build());
        list.add(PeakPriceDto.builder()
                .price(1)
                .peakStart(LocalDate.now())
                .peakEnd(LocalDate.now())
                .build());

        RoomSave roomSave = RoomSave.builder()
                .peakPrices(list)
                .build();

        //when
        Room room = roomService.saveRoom(camp, roomSave);

        //then
        assertThat(room.getPeakPrices().get(0).getId()).isNotNull();
        assertThat(room.getPeakPrices().get(1).getId()).isNotNull();

    }

    @DisplayName("캠핑장-객실 연관관계 편의 메소드 확인")
    @Test
    public void campRoom_relation() {
        //given
        RoomSave roomSave = RoomSave.builder()
                .name("room")
                .build();

        //when
        Room room = roomService.saveRoom(camp, roomSave);

        //then
        assertThat(room.getCamp()).isEqualTo(camp);
        assertThat(camp.getRooms().get(0)).isEqualTo(room);

    }

    @DisplayName("객실 정보 수정 성공 : 영속된 객체 반환")
    @Test
    public void updateRoom(){
        //given
        RoomSave roomSave = RoomSave.builder()
                .name("room")
                .cnt(10)
                .capacity(3)
                .weekendPrice(1000)
                .weekPrice(1200)
                .build();
        Room room = roomService.saveRoom(camp, roomSave);

        //when
        RoomSave updateRoomSave = RoomSave.builder()
                .id(room.getId())
                .name("room")
                .cnt(5)
                .capacity(1)
                .weekendPrice(2000)
                .weekPrice(8000)
                .build();
        Room updatedRoom = roomService.saveRoom(camp, updateRoomSave);


        //that
        assertThat(updatedRoom).isSameAs(room);

    }

    @DisplayName("객실 정보 수정 성공 : 정보 업데이트")
    @Test
    public void updateRoom_infoUpdate(){
        //given
        RoomSave roomSave = RoomSave.builder()
                .name("room")
                .cnt(10)
                .capacity(3)
                .weekendPrice(1000)
                .weekPrice(1200)
                .build();
        Room room = roomService.saveRoom(camp, roomSave);

        //when
        RoomSave updateRoomSave = RoomSave.builder()
                .id(room.getId())
                .name("room")
                .cnt(5)
                .capacity(1)
                .weekendPrice(2000)
                .weekPrice(8000)
                .build();
        Room updatedRoom = roomService.saveRoom(camp, updateRoomSave);


        //that
        assertThat(updatedRoom.getCnt()).isEqualTo(room.getCnt());
        assertThat(updatedRoom.getCapacity()).isEqualTo(room.getCapacity());
        assertThat(updatedRoom.getWeekendPrice()).isEqualTo(room.getWeekendPrice());
        assertThat(updatedRoom.getWeekPrice()).isEqualTo(room.getWeekPrice());

    }

    @DisplayName("객실 정보 수정 실패 : 존재하지 않는 아이디")
    @Test
    public void updateRoom_wrongId_fail(){

        //given
        RoomSave updateRoomSave = RoomSave.builder()
                .id(30000L)
                .name("room")
                .cnt(5)
                .capacity(1)
                .weekendPrice(2000)
                .weekPrice(8000)
                .build();

        //then
        assertThrows(NotFoundException.class, () -> roomService.saveRoom(camp, updateRoomSave));

    }




}