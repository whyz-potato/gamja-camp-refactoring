package whyzpotato.gamjacamp.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import whyzpotato.gamjacamp.domain.Image;
import whyzpotato.gamjacamp.repository.ImageRepository;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AwsS3Service {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3Client amazonS3Client;
    private final ImageRepository imageRepository;

    /**
     *  S3에 업로드
     */
    public String upload(MultipartFile file) throws IOException {
        UUID uuid = UUID.randomUUID();
        String fileName = uuid.toString() + "_" + file.getOriginalFilename();
        //https://gamja-camp.s3.ap-northeast-2.amazonaws.com/60377354-e75f-4209-af57-d8afc4ac6455_gamja1.png
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        amazonS3Client.putObject(bucket, fileName, file.getInputStream(), metadata);
        String fileUrl = amazonS3Client.getUrl(bucket, fileName).toString();

        imageRepository.save(Image.builder().filename(fileName).path(fileUrl).build());

        return fileUrl;
    }

    /**
     * S3의 객체 삭제
     */
    public void remove(Long imageId) {
        Image image = imageRepository.findById(imageId).get();
        if (!amazonS3Client.doesObjectExist(bucket, image.getFilename())) {
            // TODO does not exist exception
            return;
        }
        amazonS3Client.deleteObject(bucket, image.getFilename());
        imageRepository.delete(image);
    }
}