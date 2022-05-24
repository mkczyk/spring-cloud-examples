package com.example.clouds3example;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.WritableResource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@Service
@Slf4j
@RequiredArgsConstructor
@DependsOn("awsS3Configuration")
public class CloudS3ResourceService {

    private final AmazonS3 s3Client;
    private final ResourceLoader resourceLoader;

    public static final String BUCKET_NAME = "cloud-s3-resource-example";

    @PostConstruct
    public void init() {
        try {
            createBucket(BUCKET_NAME);
            File file = ResourceUtils.getFile("classpath:test.txt");
            upload(BUCKET_NAME, "test.txt", file);
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

    private void upload(String bucketName, String name, File file) {
        Resource resource = this.resourceLoader.getResource(String.format("s3://%s/%s", bucketName, name));
        WritableResource writableResource = (WritableResource) resource;
        try (OutputStream outputStream = writableResource.getOutputStream()) {
            Files.copy(file.toPath(), outputStream);
        } catch (IOException e) {
            throw new RuntimeException(String.format("Can't copy stream while upload '%s'", name), e);
        }
        log.info("Uploaded '{}' to bucket '{}'", name, bucketName);
    }

    private void download(String bucketName, String name) {
        Resource resource = this.resourceLoader.getResource(String.format("s3://%s/%s", bucketName, name));
        try (InputStream inputStream = resource.getInputStream()) {
            String content = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            log.info("Downloaded '{}' from bucket '{}': {}", name, bucketName, content);
        } catch (IOException e) {
            throw new RuntimeException(String.format("Can't download '%s'", name), e);
        }
    }
}
