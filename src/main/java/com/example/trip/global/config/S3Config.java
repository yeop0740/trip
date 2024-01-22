package com.example.trip.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {

    @Value("${s3.bucket}")
    private String bucketName;

    @Value("${s3.region}")
    private Region region;

    @Value("${aws.accessKeyId}")
    private String accessKeyId;

    @Value("${aws.secretAccessKey}")
    private String secretAccessKey;

    @Bean
    public S3Client getClient() {
        AwsCredentials credentials = getCredentials();

        return S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .region(region)
                .build();
    }

    @Bean
    public S3AsyncClient getS3AsyncClient() {
        return S3AsyncClient.builder()
                .credentialsProvider(StaticCredentialsProvider.create(getCredentials()))
                .region(region)
                .build();
    }

    @Bean
    public String getBucketName() {
        return bucketName;
    }

    @Bean
    public Region getRegion() {
        return region;
    }

    @Bean
    public AwsCredentials getCredentials() {
        return AwsBasicCredentials.create(accessKeyId, secretAccessKey);
    }

}
