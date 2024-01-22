package com.example.trip.domain.image;

import com.example.trip.domain.image.domain.Image;
import com.example.trip.domain.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Slf4j
@Component
@RequiredArgsConstructor
public class ImageManager {

    private final S3Client s3;

    private final S3AsyncClient s3AsyncClient;

    private final Region region;

    private final String bucketName;

    private final AwsCredentials awsCredentials;

    private final ExecutorService executorService;

    public String uploadImage(MultipartFile multipartFile, UUID uuid) throws S3Exception, RuntimeException {
        String fileName = multipartFile.getOriginalFilename();
        String key = createKey(fileName, uuid);
        byte[] bytes;
        try {
            bytes = multipartFile.getBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        PutObjectRequest putOb = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(multipartFile.getContentType())
                .build();

        s3.putObject(putOb, RequestBody.fromBytes(bytes));
        return key;
    }

    public CompletableFuture<String> uploadImageAsync(MultipartFile file, UUID uuid) {
        byte[] bytes;
        try {
            bytes = file.getBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        PutObjectRequest putOb = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(createKey(file.getOriginalFilename(), uuid))
                .contentType(file.getContentType())
                .build();

        return s3AsyncClient.putObject(putOb, AsyncRequestBody.fromBytes(bytes))
                .thenApply(response -> {
                    if (response != null) {
                        return createKey(file.getOriginalFilename(), uuid);
                    }
                    return null;
                });
    }

    public List<String> uploadImages(List<MultipartFile> multipartFiles, UUID uuid) throws IOException {
        List<String> imageKeys = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            if (multipartFile.isEmpty()) {
                continue;
            }
            long start = System.nanoTime();
            String imageKey = uploadImage(multipartFile, uuid);
            long duration = (System.nanoTime() - start) / 1_000_000;
            log.info("Done in " + duration + " msecs");
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

    public List<Image> uploadImagesAsync(List<MultipartFile> multipartFiles, UUID uuid, Member member) {
        List<CompletableFuture<Image>> futureImages = multipartFiles.stream()
                .map(file ->
                        CompletableFuture
                                .supplyAsync(() -> uploadImage(file, uuid), executorService)
                                .thenCombine(CompletableFuture.supplyAsync(() -> new Image(null, member)),
                                        (key, image) -> {
                                            if (key != null) {
                                                image.changeImageKey(key);
                                                return image;
                                            }
                                            return null;
                                        })
                                .exceptionally(io -> null))
                .toList();

        return futureImages.stream()
                .map(CompletableFuture::join)
                .toList();
    }

    public List<Image> uploadImagesAsyncV2(List<MultipartFile> multipartFiles, UUID uuid, Member member) {
        List<CompletableFuture<Image>> futureImages = multipartFiles.stream()
                .map(file ->
                        CompletableFuture
                                .supplyAsync(() -> uploadImage(file, uuid))
                                .thenApply(key -> {
                                    if (key != null) {
                                        return new Image(key, member);
                                    }
                                    return null;
                                })
                                .exceptionally(io -> null))
                .toList();

        return futureImages.stream()
                .map(CompletableFuture::join)
                .toList();
    }

    public List<String> uploadImagesAsyncV3(List<MultipartFile> multipartFiles, UUID uuid) {
        List<CompletableFuture<String>> futureKeys = multipartFiles.stream()
                .map(file -> CompletableFuture.supplyAsync(() -> uploadImage(file, uuid), executorService))
                .toList();

        return futureKeys.stream()
                .map(CompletableFuture::join)
                .toList();
    }

    public List<String> uploadImagesAsyncV5(List<MultipartFile> multipartFiles, UUID uuid) {
        List<CompletableFuture<String>> futureKeys = multipartFiles.stream()
                .map(file -> uploadImageAsync(file, uuid))
                .toList();

        return futureKeys.stream()
                .map(CompletableFuture::join)
                .toList();
    }

    public List<String> uploadImagesAsyncV4(List<MultipartFile> multipartFiles, UUID uuid) {
        return multipartFiles.stream()
                .map(file -> uploadImageAsync(file, uuid).join())
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
