package com.example.clouds3example;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Service
@Slf4j
@RequiredArgsConstructor
@DependsOn("awsS3Configuration")
public class CloudS3Service {

    private final AmazonS3 s3Client;

    public static final String BUCKET_NAME = "cloud-s3-example";

    @PostConstruct
    public void init() {
        try {
            createBucket(BUCKET_NAME);
            File file = ResourceUtils.getFile("classpath:test.txt");
            upload(BUCKET_NAME, "test.txt", new FileInputStream(file), file.length());
            download(BUCKET_NAME, "test.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void createBucket(String bucketName) {
        if (!s3Client.doesBucketExistV2(bucketName)) {
            s3Client.createBucket(bucketName);
            log.info("Created bucket {}", bucketName);
        } else {
            log.info("Bucket {} already exists", bucketName);
        }
    }

    private void upload(String bucketName, String name, InputStream inputStream, long contentLength) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(contentLength);
        s3Client.putObject(new PutObjectRequest(bucketName, name, inputStream, objectMetadata));
        log.info("Uploaded '{}' to bucket '{}'", name, bucketName);
    }

    private void download(String bucketName, String name) {
        try (InputStream inputStream = s3Client.getObject(new GetObjectRequest(bucketName, name)).getObjectContent()) {
            String content = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            log.info("Downloaded '{}' from bucket '{}': {}", name, bucketName, content);
        } catch (IOException e) {
            throw new RuntimeException(String.format("Can't download '%s'", name), e);
        }
    }

}
