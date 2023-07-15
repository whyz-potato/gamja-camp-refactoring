package whyzpotato.gamjacamp.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import whyzpotato.gamjacamp.domain.Image;
import whyzpotato.gamjacamp.repository.ImageRepository;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AwsS3Service {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3Client amazonS3Client;
    private final ImageRepository imageRepository;

    /**
     * S3에 객체 업로드
     */
    public String upload(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID() + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        //https://gamja-camp.s3.ap-northeast-2.amazonaws.com/602f30fc-38e3-428d-8d6c-229e4eb33fff.jpg
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        amazonS3Client.putObject(bucket, fileName, file.getInputStream(), metadata);
        return fileName;
    }

    /**
     * S3에 객체 여러개 업로드
     */
    public List<String> uploadImages(List<MultipartFile> files) {
        if (files == null) {
            return null;
        }

        List<String> fileNameList = new ArrayList<>();

        files.forEach(file -> {
            String fileName = UUID.randomUUID() + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            //https://gamja-camp.s3.ap-northeast-2.amazonaws.com/602f30fc-38e3-428d-8d6c-229e4eb33fff.jpg
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            try (InputStream inputStream = file.getInputStream()) {
                amazonS3Client.putObject(bucket, fileName, inputStream, metadata);
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드 실패");
            }
            fileNameList.add(fileName);
        });

        return fileNameList;
    }

    /**
     * S3의 객체 삭제
     */
    public void remove(Long imageId) {
        Image image = imageRepository.findById(imageId).get();
        if (!amazonS3Client.doesObjectExist(bucket, image.getFileName())) {
            // TODO does not exist exception
            return;
        }
        amazonS3Client.deleteObject(bucket, image.getFileName());
        imageRepository.delete(image);
    }

    /**
     * S3의 객체 여러개 삭제
     */
    public void removeImages(List<Image> images){
        if (images == null) {
            return;
        }
        images.forEach(image -> {
            if (!amazonS3Client.doesObjectExist(bucket, image.getFileName())) {
                // TODO does not exist exception
                return;
            }
            amazonS3Client.deleteObject(bucket, image.getFileName());
            imageRepository.delete(image);
        });
    }
}