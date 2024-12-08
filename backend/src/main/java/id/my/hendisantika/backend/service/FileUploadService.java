package id.my.hendisantika.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Object;

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

    @Value("${aws.s3.bucket}")
    private String bucketName;

//    public void uploadFile(String keyName, MultipartFile file) throws IOException {
//        PutObjectResult putObjectResult = s3Client.putObject(bucketName, keyName, file.getInputStream(), null);
//        log.info(putObjectResult.getMetadata());
//    }

    public String uploadFile(ByteArrayResource resource, String fileName) {
        PutObjectRequest putObjectRequest =
                PutObjectRequest.builder().bucket(bucketName).key(fileName).build();
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
}
