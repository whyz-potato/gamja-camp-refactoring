package whyzpotato.gamjacamp.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import whyzpotato.gamjacamp.dto.camp.CampDto;
import whyzpotato.gamjacamp.dto.camp.CampSaveRequestDto;
import whyzpotato.gamjacamp.dto.camp.CampUpdateRequestDto;
import whyzpotato.gamjacamp.service.AwsS3Service;
import whyzpotato.gamjacamp.service.CampService;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Controller
public class CampController {

    private final CampService campService;
    private final AwsS3Service awsS3Service;

    @PostMapping ("/v1/camp/new/{memberId}")
    public ResponseEntity createCamp(@PathVariable("memberId") Long memberId,
                                     @RequestPart("request") CampSaveRequestDto requestDto,
                                     @RequestPart(value = "images", required = false) List<MultipartFile> multipartFiles) {
        return new ResponseEntity(new createdBodyDto(campService.register(memberId, requestDto, awsS3Service.uploadImages(multipartFiles))), HttpStatus.CREATED);
    }

    @GetMapping("/v1/camp/{campId}")
    public ResponseEntity<CampDto> detailCamp(@PathVariable("campId") Long campId) {
        return new ResponseEntity<>(campService.findCamp(campId), HttpStatus.OK);
    }

    @PutMapping("/v1/camp/update/{memberId}/{campId}")
    public ResponseEntity<CampDto> updateCamp(@PathVariable("memberId") Long memberId,
                                                    @PathVariable("campId") Long campId,
                                                    @RequestBody CampUpdateRequestDto requestDto) {
        return new ResponseEntity<>(campService.updateCamp(memberId, campId, requestDto), HttpStatus.OK);
    }

    @PutMapping("/v1/camp/update/address/{memberId}/{campId}")
    public ResponseEntity<CampDto> updateCampAddress(@PathVariable("memberId") Long memberId,
                                                    @PathVariable("campId") Long campId,
                                                    @RequestParam("address") String address) {
        return new ResponseEntity<>(campService.updateCampAddress(memberId, campId, address), HttpStatus.OK);
    }

    @PutMapping("/v1/camp/update/image/{memberId}/{campId}")
    public ResponseEntity<CampDto> updateCampImages(@PathVariable("memberId") Long memberId,
                                                    @PathVariable("campId") Long campId,
                                                    @RequestPart(value = "images", required = false) List<MultipartFile> multipartFiles) {
        awsS3Service.removeImages(campService.findCampImages(memberId, campId));
        return new ResponseEntity<>(campService.updateCampImages(memberId, campId, awsS3Service.uploadImages(multipartFiles)), HttpStatus.OK);
    }

    @DeleteMapping("/v1/camp/delete/{memberId}/{campId}")
    public ResponseEntity deleteCamp(@PathVariable("memberId") Long memberId,
                                     @PathVariable("campId") Long campId) {
        awsS3Service.removeImages(campService.findCampImages(memberId, campId));
        campService.delete(memberId, campId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Data
    protected class createdBodyDto {
        private Long id;

        public createdBodyDto(Long id) {
            this.id = id;
        }
    }
}
