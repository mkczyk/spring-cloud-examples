package com.example.minios3example;

import io.minio.*;
import io.minio.errors.MinioException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
@Slf4j
public class MinioS3Service {

    public static final String BUCKET_NAME = "minio-s3-example";
    public static final String MINIO_SERVER_URL = "http://localhost:9000";
    public static final String MINIO_ACCESS_KEY = "minioadmin";
    public static final String MINIO_SECRET_KEY = "minioadmin";

    private MinioClient minioClient;

    @PostConstruct
    public void init() {
        try {
            initMinioClient();
            createBucket(BUCKET_NAME);
            File file = ResourceUtils.getFile("classpath:test.txt");
            upload(BUCKET_NAME, "test.txt", new FileInputStream(file), file.length());
            download(BUCKET_NAME, "test.txt");
        } catch (MinioException e) {
            log.error("HTTP trace: {}", e.httpTrace());
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void initMinioClient() {
        minioClient = MinioClient.builder()
            .endpoint(MINIO_SERVER_URL)
            .credentials(MINIO_ACCESS_KEY, MINIO_SECRET_KEY)
            .build();
    }

    private void createBucket(String bucketName) throws MinioException {
        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                log.info("Created bucket {}", bucketName);
            } else {
                log.info("Bucket {} already exists", bucketName);
            }
        } catch (IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(String.format("Can't create bucket '%s'", bucketName), e);
        }
    }

    private void upload(String bucketName, String name, InputStream inputStream, long contentLength) throws MinioException {
        try {
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(name)
                    .stream(inputStream, contentLength, -1)
                    .build());
            log.info("Uploaded '{}' to bucket '{}'", name, bucketName);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(String.format("Can't upload '%s'", name), e);
        }
    }

    private void download(String bucketName, String name) throws MinioException {
        try (InputStream inputStream =
                 minioClient.getObject(
                     GetObjectArgs.builder()
                         .bucket(bucketName)
                         .object(name)
                         .build()
                 )) {
            String content = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            log.info("Downloaded '{}' from bucket '{}': {}", name, bucketName, content);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(String.format("Can't download '%s'", name), e);
        }
    }

}
