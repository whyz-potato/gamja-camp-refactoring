package whyzpotato.gamjacamp.service;

import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whyzpotato.gamjacamp.controller.dto.CampDto.CampSearchItem;
import whyzpotato.gamjacamp.controller.dto.CampDto.CampSearchResult;
import whyzpotato.gamjacamp.controller.dto.Utility.PageResult;
import whyzpotato.gamjacamp.domain.Camp;
import whyzpotato.gamjacamp.domain.Coordinate;
import whyzpotato.gamjacamp.domain.Image;
import whyzpotato.gamjacamp.domain.member.Member;
import whyzpotato.gamjacamp.exception.NotFoundException;
import whyzpotato.gamjacamp.controller.dto.CampDto.CampDetail;
import whyzpotato.gamjacamp.controller.dto.CampDto.CampSaveRequest;
import whyzpotato.gamjacamp.controller.dto.CampDto.CampUpdateRequest;
import whyzpotato.gamjacamp.repository.CampRepository;
import whyzpotato.gamjacamp.repository.ImageRepository;
import whyzpotato.gamjacamp.repository.MemberRepository;
import whyzpotato.gamjacamp.repository.RoomRepository;
import whyzpotato.gamjacamp.repository.querydto.CampQueryDto;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static whyzpotato.gamjacamp.service.Authority.KAKAOAK;

@Service
@RequiredArgsConstructor
public class CampService {

    private final MemberRepository memberRepository;
    private final CampRepository campRepository;
    private final RoomRepository roomRepository;
    private final ImageRepository imageRepository;

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
            return new Coordinate(Double.parseDouble(jsonObject.getString("x")), Double.parseDouble(jsonObject.getString("y")));


            //TODO 올바르지 않은 주소 처리

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 캠핑장 등록
     */
    public Long register(Long memberId, CampSaveRequest request, List<String> fileNameList) {
        Member member = memberRepository.findById(memberId).get();
        Coordinate coordinate = findCoordinate(request.getAddress());
        Camp camp = campRepository.save(request.toEntity(member, coordinate));
        if (fileNameList == null)
            return camp.getId();
        for(String fileName : fileNameList) {
            String fileUrl = "https://gamja-camp.s3.ap-northeast-2.amazonaws.com/" + fileName;
            imageRepository.save(Image.builder().camp(camp).fileName(fileName).path(fileUrl).build());
        }
        return camp.getId();
    }

    /**
     * 캠핑장 정보 불러오기
     */
    @Transactional(readOnly = true)
    public CampDetail findCamp(Long campId) {
        Optional<Camp> optionalCamp = campRepository.findById(campId);
        Camp camp = optionalCamp.orElseThrow(() -> new NoSuchElementException("등록된 캠핑장이 없습니다."));
        return new CampDetail(camp);
    }

    /**
     * 캠핑장 정보 수정
     * 이름, 연락처, 설명, 운영시간
     */
    public CampDetail updateCamp(Long memberId, Long campId, CampUpdateRequest request) {
        Camp camp = campRepository.findById(campId).get();
        Member member = memberRepository.findById(memberId).get();
        if (camp.getMember().equals(member)) {
            camp.update(request);
            campRepository.save(updateOperatingHour(camp,request.getCampOperationStart(), request.getCampOperationEnd()));
            return new CampDetail(camp);
        }
        throw new NoSuchElementException();
    }

    /**
     * 캠핑장 주소 변경
     */
    public CampDetail updateCampAddress(Long memberId, Long campId, String address) {
        Camp camp = campRepository.findById(campId).get();
        Member member = memberRepository.findById(memberId).get();
        if (camp.getMember().equals(member)) {
            Coordinate coordinate = findCoordinate(address);
            campRepository.save(camp.updateAddress(address, coordinate));
            return new CampDetail(camp);
        }
        throw new NoSuchElementException();
    }

    /**
     * 캠핑장 이미지 변경
     */
    public CampDetail updateCampImages(Long memberId, Long campId, List<String> fileNameList) {
        Camp camp = campRepository.findById(campId).get();
        Member member = memberRepository.findById(memberId).get();
        if (camp.getMember().equals(member)) {
            for(String fileName : fileNameList) {
                String fileUrl = "https://gamja-camp.s3.ap-northeast-2.amazonaws.com/" + fileName;
                imageRepository.save(Image.builder().camp(camp).fileName(fileName).path(fileUrl).build());
            }
            return new CampDetail(campRepository.findById(campId).get());
        }
        throw new NoSuchElementException();
    }

    /**
     * 캠핑장 검색
     */
    //TODO 화면 내 검색 활성화
    //TODO refactor (파라미터 줄이기)
    public CampSearchResult<CampSearchItem> search(String query, Double neLatitude, Double swLatitude, Double neLongitude, Double swLongitude, LocalDate checkIn, LocalDate checkOut, int numGuests, Pageable pageable) {
        Page<CampSearchItem> result = campRepository.searchAvailCamp(query, checkIn, checkOut, numGuests, pageable)
                .map(CampSearchItem::new);
        return new CampSearchResult<>(result, checkIn, checkOut);
    }

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

    protected Camp updateOperatingHour(Camp camp, String start, String end) {
        if (start != null && end != null) {
            LocalTime startTime = LocalTime.of(Integer.parseInt(start.substring(0, start.indexOf(":"))), Integer.parseInt(start.substring(start.indexOf(":")+1)));
            LocalTime endTime = LocalTime.of(Integer.parseInt(end.substring(0, end.indexOf(":"))), Integer.parseInt(end.substring(end.indexOf(":")+1)));
            camp.updateOperatingHours(startTime, endTime);
        } else {
            camp.updateOperatingHours(null, null);
        }
        return camp;
    }

    public List<Image> findCampImages(Long memberId, Long campId) {
        Camp camp = campRepository.findById(campId).get();
        Member member = memberRepository.findById(memberId).get();
        if (camp.getMember().equals(member)) {
            return imageRepository.findAllByCamp(camp);
        }
        throw new NoSuchElementException();
    }

//    /**
//     * 캠핑장 정보 수정
//     * 이름, 연락처, 설명
//     */
//    public CampDto updateCamp(Long memberId, Long campId, CampUpdateRequestDto campUpdateRequestDto) {
//        Camp camp = campRepository.findById(campId).get();
//        Member member = memberRepository.findById(memberId).get();
//        if (camp.getMember().equals(member)) {
//            campRepository.save(camp.update(campUpdateRequestDto));
//            return new CampDto(camp);
//        }
//        throw new NoSuchElementException();
//    }

//    /**
//     * 캠핑장 운영시간 변경
//     */
//    public CampDto updateOperatingHours(Long memberId, Long campId, int startHours, int startMinutes, int endHours, int endMinutes) {
//        Camp camp = campRepository.findById(campId).get();
//        Member member = memberRepository.findById(memberId).get();
//        if (camp.getMember().equals(member)) {
//            LocalTime startTime = LocalTime.of(startHours, startMinutes);
//            LocalTime endTime = LocalTime.of(endHours, endMinutes);
//            campRepository.save(camp.updateOperatingHours(startTime, endTime));
//            return new CampDto(camp);
//        }
//        throw new NoSuchElementException();
//    }

//    /**
//     * 캠핑장 운영시간 삭제
//     */
//    public void deleteOperatingHours(Long memberId, Long campId) {
//        Camp camp = campRepository.findById(campId).get();
//        Member member = memberRepository.findById(memberId).get();
//        if (camp.getMember().equals(member)) {
//            campRepository.save(camp.updateOperatingHours(null, null));
//            return;
//        }
//        throw new NoSuchElementException();
//    }

    /**
     * @param campId
     * @return camp
     */
    public Camp findById(Long campId) {
        return campRepository.findById(campId)
                .orElseThrow(() -> new NotFoundException());

    }
}
