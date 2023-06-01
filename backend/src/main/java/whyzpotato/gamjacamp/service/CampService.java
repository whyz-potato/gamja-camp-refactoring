package whyzpotato.gamjacamp.service;

import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whyzpotato.gamjacamp.domain.Camp;
import whyzpotato.gamjacamp.domain.Coordinate;
import whyzpotato.gamjacamp.domain.member.Member;
import whyzpotato.gamjacamp.dto.camp.CampDto;
import whyzpotato.gamjacamp.dto.camp.CampSaveRequestDto;
import whyzpotato.gamjacamp.dto.camp.CampUpdateRequestDto;
import whyzpotato.gamjacamp.repository.CampRepository;
import whyzpotato.gamjacamp.repository.MemberRepository;
import whyzpotato.gamjacamp.repository.RoomRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.time.LocalTime;
import java.util.NoSuchElementException;
import java.util.Optional;

import static whyzpotato.gamjacamp.service.Authority.KAKAOAK;

@Service
@RequiredArgsConstructor
public class CampService {

    private final MemberRepository memberRepository;
    private final CampRepository campRepository;
    private final RoomRepository roomRepository;

    /**
     * 캠핑장 주소 좌표 찾기
     */
    public Coordinate findCoordinate(String address) {
        // 주소 -> 좌표
        String GEOCODE_URL = "http://dapi.kakao.com/v2/local/search/address.json?query=";
        String GEOCODE_USER_INFO = "KakaoAK " + KAKAOAK.getValue();

        try {
            String text_context = URLEncoder.encode(address, "utf-8");
            URL url = new URL(GEOCODE_URL + text_context);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", GEOCODE_USER_INFO);
            con.setRequestProperty("content-type", "application/json");
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.setDefaultUseCaches(false);

            Charset charset = Charset.forName("utf-8");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), charset));

            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            JSONObject jsonObject = new JSONObject(response.toString()).getJSONArray("documents").getJSONObject(0);
            return new Coordinate(Float.parseFloat(jsonObject.getString("x")), Float.parseFloat(jsonObject.getString("y")));

            //TODO 올바르지 않은 주소 처리

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 캠핑장 등록
     */
    public Long register(Long memberId, CampSaveRequestDto campSaveRequestDto) {
        Member member = memberRepository.findById(memberId).get();
        Coordinate coordinate = findCoordinate(campSaveRequestDto.getAddress());
        return campRepository.save(campSaveRequestDto.toEntity(member, coordinate)).getId();
    }

    /**
     * 캠핑장 정보 불러오기
     */
    @Transactional(readOnly = true)
    public CampDto findCamp(Long memberId) {
        Optional<Camp> optionalCamp = campRepository.findByMember(memberRepository.findById(memberId).get());
        Camp camp = optionalCamp.orElseThrow(() -> new NoSuchElementException("등록된 캠핑장이 없습니다."));
        return new CampDto(camp);
    }

    /**
     * 캠핑장 정보 수정
     * 이름, 연락처, 설명
     */
    public CampDto updateCamp(Long memberId, Long campId, CampUpdateRequestDto campUpdateRequestDto) {
        Camp camp = campRepository.findById(campId).get();
        Member member = memberRepository.findById(memberId).get();
        if (camp.getMember().equals(member)) {
            campRepository.save(camp.update(campUpdateRequestDto));
            return new CampDto(camp);
        }
        throw new NoSuchElementException();
    }

    /**
     * 캠핑장 주소 변경
     */
    public CampDto updateCampAddress(Long memberId, Long campId, String address) {
        Camp camp = campRepository.findById(campId).get();
        Member member = memberRepository.findById(memberId).get();
        if (camp.getMember().equals(member)) {
            Coordinate coordinate = findCoordinate(address);
            campRepository.save(camp.updateAddress(address, coordinate));
            return new CampDto(camp);
        }
        throw new NoSuchElementException();
    }

    /**
     * 캠핑장 운영시간 변경
     */
    public CampDto updateOperatingHours(Long memberId, Long campId, int startHours, int startMinutes, int endHours, int endMinutes) {
        Camp camp = campRepository.findById(campId).get();
        Member member = memberRepository.findById(memberId).get();
        if (camp.getMember().equals(member)) {
            LocalTime startTime = LocalTime.of(startHours, startMinutes);
            LocalTime endTime = LocalTime.of(endHours, endMinutes);
            campRepository.save(camp.updateOperatingHours(startTime, endTime));
            return new CampDto(camp);
        }
        throw new NoSuchElementException();
    }

    /**
     * 캠핑장 검색
     */

    /**
     * 캠핑장 삭제
     */
    public void delete(Long memberId, Long campId) {
        Camp camp = campRepository.findById(campId).get();
        Member member = memberRepository.findById(memberId).get();
        if (camp.getMember().equals(member)) {
            campRepository.delete(camp);
            return;
        }
        throw new NoSuchElementException();
    }

    /**
     * 캠핑장 운영시간 삭제
     */
    public void deleteOperatingHours(Long memberId, Long campId) {
        Camp camp = campRepository.findById(campId).get();
        Member member = memberRepository.findById(memberId).get();
        if (camp.getMember().equals(member)) {
            campRepository.save(camp.updateOperatingHours(null, null));
            return;
        }
        throw new NoSuchElementException();
    }
}
