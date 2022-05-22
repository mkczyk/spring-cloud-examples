package com.example.minios3example;

import io.minio.*;
import io.minio.errors.MinioException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
@Slf4j
public class MinioS3Service {

    public static final String BUCKET_NAME = "mini-s3-example";
    public static final String MINIO_SERVER_URL = "http://localhost:9000";
    public static final String MINIO_ACCESS_KEY = "minioadmin";
    public static final String MINIO_SECRET_KEY = "minioadmin";

    private MinioClient minioClient;

    @PostConstruct
    public void init() {
        try {
            initMinioClient();
            createBucket(BUCKET_NAME);
            upload("test.txt", "C://dev//test.txt", BUCKET_NAME);
            download("test.txt", BUCKET_NAME, "C://dev//test2.txt");
        } catch (MinioException e) {
            log.error("HTTP trace: {}", e.httpTrace());
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

    private void upload(String name, String filePath, String bucketName) throws MinioException {
        try {
            minioClient.uploadObject(
                    UploadObjectArgs.builder()
                            .bucket(bucketName)
                            .object(name)
                            .filename(filePath)
                            .build());
            log.info("Uploaded '{}' from '{}' to bucket '{}'", name, filePath, bucketName);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(String.format("Can't upload '%s' from '%s'", name, filePath), e);
        }
    }

    private void download(String name, String bucketName, String filePathDownload) throws MinioException {
        try {
            minioClient.downloadObject(
                    DownloadObjectArgs.builder()
                            .object(name)
                            .bucket(bucketName)
                            .filename(filePathDownload)
                            .build()
            );
            log.info("Download '{}' from bucket '{}' to '{}'", name, bucketName, filePathDownload);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(String.format("Can't download '%s' to '%s'", name, filePathDownload), e);
        }
    }

}
