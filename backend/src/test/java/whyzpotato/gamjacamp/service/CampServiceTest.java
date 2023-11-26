package whyzpotato.gamjacamp.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import whyzpotato.gamjacamp.domain.Camp;
import whyzpotato.gamjacamp.domain.member.Member;
import whyzpotato.gamjacamp.domain.member.Role;
import whyzpotato.gamjacamp.controller.dto.CampDto.CampDetail;
import whyzpotato.gamjacamp.controller.dto.CampDto.CampSaveRequest;
import whyzpotato.gamjacamp.controller.dto.CampDto.CampUpdateRequest;
import whyzpotato.gamjacamp.repository.CampRepository;
import whyzpotato.gamjacamp.repository.MemberRepository;

import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class CampServiceTest {

    @Autowired
    private CampService campService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private CampRepository campRepository;

    @Test
    public void 캠핑장등_등록() {
        Member member = memberRepository.save(Member.builder().account("hsy3130@test.com").username("hsy").picture(null).role(Role.ROLE_OWNER).build());
        CampSaveRequest request = CampSaveRequest.builder().name("감자캠핑").address("서울특별시 광진구 동일로40길 25-1").build();

        Long campId = campService.register(member.getId(), request, null);

        Optional<Camp> camp = campRepository.findById(campId);
        assertThat(camp.get().getMember().getId()).isEqualTo(member.getId());
        assertThat(camp.get().getName()).isEqualTo(request.getName());
        assertThat(camp.get().getAddress()).isEqualTo(request.getAddress());
    }

    @Test void 캠핑장_정보조회() {
        Member member = memberRepository.save(Member.builder().account("hsy3130@test.com").username("hsy").picture(null).role(Role.ROLE_OWNER).build());
        Camp camp = campRepository.save(Camp.builder().member(member).name("감자캠핑").address("서울특별시 광진구 동일로40길 25-1").longitude(126.1332152f).latitude(92.1234455f).build());

        CampDetail campDetail = campService.findCamp(camp.getId());

        assertThat(campDetail.getId()).isEqualTo(camp.getId());
        assertThat(campDetail.getMemberId()).isEqualTo(member.getId());
        assertThat(campDetail.getName()).isEqualTo("감자캠핑");
        assertThat(campDetail.getAddress()).isEqualTo("서울특별시 광진구 동일로40길 25-1");
        assertThat(campDetail.getLongitude()).isEqualTo(126.1332152f);
        assertThat(campDetail.getLatitude()).isEqualTo(92.1234455f);
    }

    @Test
    public void 캠핑장_정보변경() {
        Member member = memberRepository.save(Member.builder().account("hsy3130@test.com").username("hsy").picture(null).role(Role.ROLE_OWNER).build());
        Camp camp = campRepository.save(Camp.builder().member(member).name("감자캠핑").address("서울특별시 광진구 동일로40길 25-1").longitude(126.1332152f).latitude(92.1234455f).build());

        campService.updateCamp(member.getId(), camp.getId(), CampUpdateRequest.builder().name("이름변경").phone("010-1234-1234").campIntroduction("캠프소개").build());

        Optional<Camp> updateCamp = campRepository.findById(camp.getId());
        assertThat(updateCamp.get().getName()).isEqualTo("이름변경");
        assertThat(updateCamp.get().getPhone()).isEqualTo("010-1234-1234");
        assertThat(updateCamp.get().getCampIntroduction()).isEqualTo("캠프소개");
    }

    @Test
    public void 캠핑장_정보변경_연락처소개_삭제() {
        Member member = memberRepository.save(Member.builder().account("hsy3130@test.com").username("hsy").picture(null).role(Role.ROLE_OWNER).build());
        Camp camp = campRepository.save(Camp.builder().member(member).name("감자캠핑").address("서울특별시 광진구 동일로40길 25-1").phone("010-1234-1234").campIntroduction("캠프소개").longitude(126.1332152f).latitude(92.1234455f).build());

        campService.updateCamp(member.getId(), camp.getId(), CampUpdateRequest.builder().name("감자캠핑").build());

        Optional<Camp> updateCamp = campRepository.findById(camp.getId());
        assertThat(updateCamp.get().getName()).isEqualTo("감자캠핑");
        assertThat(updateCamp.get().getPhone()).isNull();
        assertThat(updateCamp.get().getCampIntroduction()).isNull();
    }

    @Test
    public void 캠핑장_주소변경() {
        Member member = memberRepository.save(Member.builder().account("hsy3130@test.com").username("hsy").picture(null).role(Role.ROLE_OWNER).build());
        Camp camp = campRepository.save(Camp.builder().member(member).name("감자캠핑").address("서울특별시 광진구 군자동 218").longitude(126.1332152f).latitude(92.1234455f).build());

        campService.updateCampAddress(member.getId(), camp.getId(), "서울특별시 광진구 동일로40길 25-1");

        Optional<Camp> updateCamp = campRepository.findById(camp.getId());
        assertThat(updateCamp.get().getAddress()).isEqualTo("서울특별시 광진구 동일로40길 25-1");
        System.out.println(updateCamp.get().getLongitude());
        System.out.println(updateCamp.get().getLatitude());
    }

//    @Test
//    public void 캠핑장_운영시간변경() {
//        Member member = memberRepository.save(Member.builder().account("hsy3130@test.com").username("hsy").picture(null).role(Role.ROLE_OWNER).build());
//        Camp camp = campRepository.save(Camp.builder().member(member).name("감자캠핑").address("서울특별시 광진구 동일로40길 25-1").campX(126.1332152f).campY(92.1234455f).build());
//
//        campService.updateOperatingHours(member.getId(),  camp.getId(), 11, 0, 22, 30);
//
//        Optional<Camp> updateCamp = campRepository.findById(camp.getId());
//        assertThat(updateCamp.get().getCampOperationStart()).isEqualTo(LocalTime.of(11,0));
//        assertThat(updateCamp.get().getCampOperationEnd()).isEqualTo(LocalTime.of(22,30));
//    }

    @Test
    public void 캠핑장_삭제() {
        Member member = memberRepository.save(Member.builder().account("hsy3130@test.com").username("hsy").picture(null).role(Role.ROLE_OWNER).build());
        Camp camp = campRepository.save(Camp.builder().member(member).name("감자캠핑").address("서울특별시 광진구 동일로40길 25-1").longitude(126.1332152f).latitude(92.1234455f).build());

        campService.delete(member.getId(), camp.getId());

        Optional<Camp> deleteCamp = campRepository.findById(camp.getId());
        assertThat(deleteCamp.isPresent()).isFalse();
    }

//    @Test
//    public void 캠핑장_운영시간삭제() {
//        Member member = memberRepository.save(Member.builder().account("hsy3130@test.com").username("hsy").picture(null).role(Role.ROLE_OWNER).build());
//        Camp camp = campRepository.save(Camp.builder().member(member).name("감자캠핑").address("서울특별시 광진구 동일로40길 25-1").campX(126.1332152f).campY(92.1234455f).build());
//        campService.updateOperatingHours(member.getId(),  camp.getId(), 11, 0, 22, 30);
//
//        campService.deleteOperatingHours(member.getId(), camp.getId());
//
//        Optional<Camp> optionalCamp = campRepository.findById(camp.getId());
//        assertThat(optionalCamp.get().getCampOperationStart()).isNull();
//        assertThat(optionalCamp.get().getCampOperationEnd()).isNull();
//    }

    @Test
    public void 캠핑장_운영시간() {
        Member member = memberRepository.save(Member.builder().account("hsy3130@test.com").username("hsy").picture(null).role(Role.ROLE_OWNER).build());
        Camp camp = campRepository.save(Camp.builder().member(member).name("감자캠핑").address("서울특별시 광진구 동일로40길 25-1").longitude(126.1332152f).latitude(92.1234455f).build());

        campRepository.save(campService.updateOperatingHour(camp, "18:00", "11:30"));

        Camp updateCamp = campRepository.findById(camp.getId()).get();
        assertThat(updateCamp.getCampOperationStart()).isEqualTo(LocalTime.of(18,0));
        assertThat(updateCamp.getCampOperationEnd()).isEqualTo(LocalTime.of(11,30));
    }
}
