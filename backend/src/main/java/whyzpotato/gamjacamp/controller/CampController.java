package whyzpotato.gamjacamp.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import whyzpotato.gamjacamp.controller.dto.CampDto.CampDetail;
import whyzpotato.gamjacamp.controller.dto.CampDto.CampSaveRequest;
import whyzpotato.gamjacamp.controller.dto.CampDto.CampUpdateRequest;
import whyzpotato.gamjacamp.service.AwsS3Service;
import whyzpotato.gamjacamp.service.CampService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/camps")
public class CampController {

    private final CampService campService;
    private final AwsS3Service awsS3Service;

    @PostMapping ("/new/{memberId}")
    public ResponseEntity createCamp(@PathVariable("memberId") Long memberId,
                                     @RequestPart("request") CampSaveRequest request,
                                     @RequestPart(value = "images", required = false) List<MultipartFile> multipartFiles) {
        return new ResponseEntity(new createdBodyDto(campService.register(memberId, request, awsS3Service.uploadImages(multipartFiles))), HttpStatus.CREATED);
    }

    @GetMapping("/{campId}")
    public ResponseEntity<CampDetail> detailCamp(@PathVariable("campId") Long campId) {
        return new ResponseEntity<>(campService.findCamp(campId), HttpStatus.OK);
    }

    @PutMapping("/update/{memberId}/{campId}")
    public ResponseEntity<CampDetail> updateCamp(@PathVariable("memberId") Long memberId,
                                                    @PathVariable("campId") Long campId,
                                                    @RequestBody CampUpdateRequest request) {
        return new ResponseEntity<>(campService.updateCamp(memberId, campId, request), HttpStatus.OK);
    }

    @PutMapping("/update/address/{memberId}/{campId}")
    public ResponseEntity<CampDetail> updateCampAddress(@PathVariable("memberId") Long memberId,
                                                    @PathVariable("campId") Long campId,
                                                    @RequestParam("address") String address) {
        return new ResponseEntity<>(campService.updateCampAddress(memberId, campId, address), HttpStatus.OK);
    }

    @PutMapping("/update/image/{memberId}/{campId}")
    public ResponseEntity<CampDetail> updateCampImages(@PathVariable("memberId") Long memberId,
                                                    @PathVariable("campId") Long campId,
                                                    @RequestPart(value = "images", required = false) List<MultipartFile> multipartFiles) {
        awsS3Service.removeImages(campService.findCampImages(memberId, campId));
        return new ResponseEntity<>(campService.updateCampImages(memberId, campId, awsS3Service.uploadImages(multipartFiles)), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{memberId}/{campId}")
    public ResponseEntity deleteCamp(@PathVariable("memberId") Long memberId,
                                     @PathVariable("campId") Long campId) {
        awsS3Service.removeImages(campService.findCampImages(memberId, campId));
        campService.delete(memberId, campId);
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
