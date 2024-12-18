package id.my.hendisantika.backend.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import id.my.hendisantika.backend.config.StorageConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

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
    private final AmazonS3 s3ClientV1;
    private final S3Client s3Client;
    private final StorageConfig storageConfig;

    public void uploadFileV2(String keyName, InputStream inputStream, long contentLength) {
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

    public InputStream downloadFileV2(String keyName) {
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

    public void uploadFile(String keyName, MultipartFile file) throws IOException {
        PutObjectResult putObjectResult = s3ClientV1.putObject(storageConfig.getBucketName(), keyName, file.getInputStream(), null);
        log.info(String.valueOf(putObjectResult.getMetadata()));
    }

    public S3Object getFile(String keyName) {
        try {
            return s3ClientV1.getObject(storageConfig.getBucketName(), keyName);
        } catch (AmazonS3Exception e) {
            log.error("AmazonS3Exception: {}", e.getMessage());
            return null;
        }
    }
}
