package com.example.clouds3example;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import io.awspring.cloud.autoconfigure.context.properties.AwsCredentialsProperties;
import io.awspring.cloud.autoconfigure.context.properties.AwsRegionProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@RequiredArgsConstructor
public class AwsS3Configuration {

    private final AwsCredentialsProperties awsCredentialsProperties;
    private final AwsRegionProperties awsRegionProperties;
    private final Environment environment;

    @Bean
    public AmazonS3 amazonS3() {
        return AmazonS3Client.builder()
            .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(this.environment.getProperty("cloud" +
                ".aws.s3.endpoint"),
                awsRegionProperties.getStatic()))
            .withPathStyleAccessEnabled(true)
            .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(awsCredentialsProperties.getAccessKey(), awsCredentialsProperties.getSecretKey())))
            .build();
    }
}