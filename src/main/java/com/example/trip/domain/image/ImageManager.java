package com.example.trip.domain.image;

import com.example.trip.domain.image.domain.Image;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ImageManager {

    private final S3Client s3;

    private final Region region;

    private final String bucketName;

    private final AwsCredentials awsCredentials;

    public String uploadImage(MultipartFile multipartFile, UUID uuid) throws IOException, S3Exception {
        String fileName = multipartFile.getOriginalFilename();
        String key = createKey(fileName, uuid);
        byte[] bytes = multipartFile.getBytes();
        PutObjectRequest putOb = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(multipartFile.getContentType())
                .build();

        s3.putObject(putOb, RequestBody.fromBytes(bytes));
        return key;
    }

    public List<String> uploadImages(List<MultipartFile> multipartFiles, UUID uuid) throws IOException {
        List<String> imageKeys = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            if (multipartFile.isEmpty()) {
                continue;
            }
            String imageKey = uploadImage(multipartFile, uuid);
            imageKeys.add(imageKey);
        }

        return imageKeys;
    }

    public List<String> uploadImagesParallel(List<MultipartFile> multipartFiles, UUID uuid) {
        return multipartFiles.parallelStream()
                .map(file -> {
                    try {
                        return uploadImage(file, uuid);
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();
    }

    public String createKey(String fileName, UUID uuid) {
        return new StringBuffer()
                .append(uuid.toString())
                .append("_")
                .append(fileName)
                .toString();
    }

    public List<String> createSignedUrls(List<String> imageKeys) {
        return imageKeys.stream()
                .map(this::createSignedUrlForString)
                .toList();
    }

    public String createSignedUrlForString(String keyName) {
        try (S3Presigner preSigner = S3Presigner.builder()
                .region(region)
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .build()) {

            GetObjectRequest objectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(keyName)
                    .build();

            GetObjectPresignRequest preSignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(60))
                    .getObjectRequest(objectRequest)
                    .build();

            PresignedGetObjectRequest preSignedRequest = preSigner.presignGetObject(preSignRequest);

            return preSignedRequest.url().toString();
        }
    }

    public void delete(String key) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        s3.deleteObject(deleteObjectRequest);
    }

    public void deleteImages(List<Image> imageList) {
        imageList.forEach(image -> delete(image.getImageKey()));
    }

}
