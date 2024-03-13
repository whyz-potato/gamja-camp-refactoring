package whyzpotato.gamjacamp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import whyzpotato.gamjacamp.service.AwsS3Service;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/s3")
public class AwsS3Controller {

    private final AwsS3Service awsS3Service;

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("image") MultipartFile file) throws IOException {
        return ResponseEntity.ok(awsS3Service.upload(file));
    }

    @DeleteMapping("/remove/{id}")
    public void remove(@PathVariable("id") Long imageId) {
        awsS3Service.remove(imageId);
    }
}