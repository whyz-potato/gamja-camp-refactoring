package whyzpotato.gamjacamp.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import whyzpotato.gamjacamp.config.auth.LoginMember;
import whyzpotato.gamjacamp.config.auth.dto.SessionMember;
import whyzpotato.gamjacamp.controller.dto.Utility.PageResult;
import whyzpotato.gamjacamp.controller.dto.CampDto.CampDetail;
import whyzpotato.gamjacamp.controller.dto.CampDto.CampSaveRequest;
import whyzpotato.gamjacamp.controller.dto.CampDto.CampUpdateRequest;
import whyzpotato.gamjacamp.controller.dto.CampDto.CampSearchItem;
import whyzpotato.gamjacamp.controller.dto.CampDto.CampSearchResult;
import whyzpotato.gamjacamp.service.CampService;
import whyzpotato.gamjacamp.service.AwsS3Service;

import java.util.List;
import java.time.LocalDate;

@RequiredArgsConstructor
@RestController
@RequestMapping("/camps")
public class CampController {

    private final CampService campService;
    private final AwsS3Service awsS3Service;

    @GetMapping("/search")
    public CampSearchResult<CampSearchItem> search(@RequestParam(defaultValue = "") String query,
                                                   @RequestParam("ne-lat") Double neLatitude,
                                                   @RequestParam("sw-lat") Double swLatitude,
                                                   @RequestParam("ne-lng") Double neLongitude,
                                                   @RequestParam("sw-lng") Double swLongitude,
                                                   @RequestParam(value = "check-in", defaultValue = "#{T(java.time.LocalDate).now()}") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkIn,
                                                   @RequestParam(value = "check-out", defaultValue = "#{T(java.time.LocalDate).now().plusDays(1)}") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkOut,
                                                   @RequestParam(value = "guests", defaultValue = "2") int numGuests,
                                                   Pageable pageable) {

        return campService.search(query, neLatitude, swLatitude, neLongitude, swLongitude, checkIn, checkOut, numGuests, pageable);
    }

    @PostMapping
    public ResponseEntity createCamp(@LoginMember SessionMember member,
                                     @RequestPart("request") CampSaveRequest request,
                                     @RequestPart(value = "images", required = false) List<MultipartFile> multipartFiles) {
        return new ResponseEntity(new createdBodyDto(campService.register(member.getId(), request, awsS3Service.uploadImages(multipartFiles))), HttpStatus.CREATED);
    }

    @GetMapping("/{campId}")
    public ResponseEntity<CampDetail> detailCamp(@PathVariable("campId") Long campId) {
        return new ResponseEntity<>(campService.findCamp(campId), HttpStatus.OK);
    }

    @PutMapping("/{campId}")
    public ResponseEntity<CampDetail> updateCamp(@LoginMember SessionMember member,
                                                 @PathVariable("campId") Long campId,
                                                 @RequestBody CampUpdateRequest request) {
        return new ResponseEntity<>(campService.updateCamp(member.getId(), campId, request), HttpStatus.OK);
    }

    @PutMapping("/update/address/{campId}")
    public ResponseEntity<CampDetail> updateCampAddress(@LoginMember SessionMember member,
                                                        @PathVariable("campId") Long campId,
                                                        @RequestParam("address") String address) {
        return new ResponseEntity<>(campService.updateCampAddress(member.getId(), campId, address), HttpStatus.OK);
    }

    @PutMapping("/update/image/{memberId}/{campId}")
    public ResponseEntity<CampDetail> updateCampImages(@LoginMember SessionMember member,
                                                       @PathVariable("campId") Long campId,
                                                       @RequestPart(value = "images", required = false) List<MultipartFile> multipartFiles) {
        awsS3Service.removeImages(campService.findCampImages(member.getId(), campId));
        return new ResponseEntity<>(campService.updateCampImages(member.getId(), campId, awsS3Service.uploadImages(multipartFiles)), HttpStatus.OK);
    }

    @DeleteMapping("/{campId}")
    public ResponseEntity deleteCamp(@LoginMember SessionMember member,
                                     @PathVariable("campId") Long campId) {
        awsS3Service.removeImages(campService.findCampImages(member.getId(), campId));
        campService.delete(member.getId(), campId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Data
    protected static class createdBodyDto {
        private Long id;

        public createdBodyDto(Long id) {
            this.id = id;
        }
    }
}
