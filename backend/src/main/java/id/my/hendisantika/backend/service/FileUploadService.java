package id.my.hendisantika.backend.service;

import id.my.hendisantika.backend.config.StorageConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Object;
import software.amazon.awssdk.services.s3.model.StorageClass;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by IntelliJ IDEA.
 * Project : cloudfare-r2-demo
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 08/12/24
 * Time: 15.29
 * To change this template use File | Settings | File Templates.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FileUploadService {
    private static final String BASE_PREFIX_IMAGE_NAME_TAG = "nakes/photos/";
    private final S3Client s3Client;
    private final S3Client s3Client;
    private final StorageConfig storageConfig;
    private final ImageService imageService;

//    public void uploadFile(String keyName, MultipartFile file) throws IOException {
//        PutObjectResult putObjectResult = s3Client.putObject(bucketName, keyName, file.getInputStream(), null);
//        log.info(putObjectResult.getMetadata());
//    }

    public String uploadFile(ByteArrayResource resource, String fileName) {
        PutObjectRequest putObjectRequest =
                PutObjectRequest.builder().bucket(storageConfig.getBucketName()).key(fileName).build();
        try (InputStream inputStream = resource.getInputStream()) {
            s3Client.putObject(
                    putObjectRequest,
                    software.amazon.awssdk.core.sync.RequestBody.fromInputStream(
                            inputStream, resource.contentLength()));
            log.info("Upload File success: {}", fileName);
        } catch (Exception e) {
            log.error("Error while uploading to s3 file {} with {}", e, fileName);
        }
        return fileName;
    }


    public S3Object getFile(String keyName) {
        try {
            return s3Client.getObject(bucketName, keyName);
        } catch (AmazonS3Exception e) {
            log.error(e);
            return null;
        }
    }

    public String uploadFile(MultipartFile multipartFile) {
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(awsProperties.getS3().getBucket())
                    .key(BASE_PREFIX_IMAGE_NAME_TAG + multipartFile.getOriginalFilename())
                    .contentLength(multipartFile.getSize())
                    .contentType(multipartFile.getContentType())
                    .storageClass(StorageClass.STANDARD)
                    .build();
            log.info("multipartFile.getSize() -> {}", multipartFile.getSize());
            s3Client.putObject(putObjectRequest,
                    RequestBody.fromBytes(multipartFile.getInputStream().readAllBytes()));
            String s3Url = String.format("https://%s.s3.%s.amazonaws.com/%s", awsProperties.getS3().getBucket(), awsProperties.getS3().getRegion(), BASE_PREFIX_IMAGE_NAME_TAG + multipartFile.getOriginalFilename());
            log.info("Upload file {} to AWS S3 Successfully Uploaded.", multipartFile.getOriginalFilename());
            log.info("s3Url -> {}", s3Url);
            return s3Url;
        } catch (IOException e) {
            return e.getMessage();
        }
    }
}
