package com.example.trip.domain.image;

import com.example.trip.domain.image.domain.UploadImageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ImageManager {

    private final S3Client s3;

    private final String bucketName;

    public UploadImageDTO uploadImage(MultipartFile multipartFile, UUID uuid) throws IOException, S3Exception {
        String fileName = multipartFile.getOriginalFilename();
        String key = createKey(fileName, uuid);
        byte[] bytes = multipartFile.getBytes();
        PutObjectRequest putOb = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        s3.putObject(putOb, RequestBody.fromBytes(bytes));
        return new UploadImageDTO(key, getUrl(key));
    }

    public Map<String, String> uploadImages(List<MultipartFile> multipartFiles, UUID uuid) throws IOException {
        Map<String, String> keyAndUrl = new HashMap<>();

        for (MultipartFile multipartFile : multipartFiles) {
            if (multipartFile.isEmpty()) {
                continue;
            }
            UploadImageDTO imageDto = uploadImage(multipartFile, uuid);
            keyAndUrl.put(imageDto.getKey(), imageDto.getUrl());
        }

        return keyAndUrl;
    }

    private String getUrl(String key) throws S3Exception {
        GetUrlRequest request = GetUrlRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        URL url = s3.utilities().getUrl(request);
        return url.toString();
    }

    public String createKey(String fileName, UUID uuid) {
        return new StringBuffer()
                .append(uuid.toString())
                .append("/")
                .append(fileName)
                .toString();
    }

    public void delete(String key) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        s3.deleteObject(deleteObjectRequest);
    }

}
