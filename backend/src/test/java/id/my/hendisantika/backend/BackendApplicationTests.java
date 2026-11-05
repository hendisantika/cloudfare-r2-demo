package id.my.hendisantika.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
        "aws.region=auto",
        "aws.access-key=test-access-key",
        "aws.access-secret=test-access-secret",
        "aws.bucket-name=test-bucket",
        "aws.endpoint-url=https://example.r2.cloudflarestorage.com"
})
class BackendApplicationTests {

    @Test
    void contextLoads() {
    }

}
