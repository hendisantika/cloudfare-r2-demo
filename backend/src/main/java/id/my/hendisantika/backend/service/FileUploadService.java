package id.my.hendisantika.backend.service;

import id.my.hendisantika.backend.config.StorageConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

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
    private final S3Client s3Client;
    private final StorageConfig storageConfig;

//    public void uploadFile(String keyName, MultipartFile file) throws IOException {
//        PutObjectResult putObjectResult = s3Client.putObject(bucketName, keyName, file.getInputStream(), null);
//        log.info(putObjectResult.getMetadata());
//    }

    public void uploadFile(String keyName, InputStream inputStream, long contentLength) {
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(storageConfig.getBucketName())
                    .key(keyName)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, contentLength));
        } catch (S3Exception e) {
            throw new RuntimeException("Error uploading file to S3: " + e.getMessage(), e);
        }
    }

    public InputStream downloadFile(String keyName) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(storageConfig.getBucketName())
                    .key(keyName)
                    .build();

            return s3Client.getObject(getObjectRequest);
        } catch (S3Exception e) {
            throw new RuntimeException("Error downloading file from S3: " + e.getMessage(), e);
        }
    }

//    public String uploadFile(MultipartFile multipartFile) {
//        try {
//            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
//                    .bucket(awsProperties.getS3().getBucket())
//                    .key(BASE_PREFIX_IMAGE_NAME_TAG + multipartFile.getOriginalFilename())
//                    .contentLength(multipartFile.getSize())
//                    .contentType(multipartFile.getContentType())
//                    .storageClass(StorageClass.STANDARD)
//                    .build();
//            log.info("multipartFile.getSize() -> {}", multipartFile.getSize());
//            s3Client.putObject(putObjectRequest,
//                    RequestBody.fromBytes(multipartFile.getInputStream().readAllBytes()));
//            String s3Url = String.format("https://%s.s3.%s.amazonaws.com/%s", awsProperties.getS3().getBucket(), awsProperties.getS3().getRegion(), BASE_PREFIX_IMAGE_NAME_TAG + multipartFile.getOriginalFilename());
//            log.info("Upload file {} to AWS S3 Successfully Uploaded.", multipartFile.getOriginalFilename());
//            log.info("s3Url -> {}", s3Url);
//            return s3Url;
//        } catch (IOException e) {
//            return e.getMessage();
//        }
//    }
}
