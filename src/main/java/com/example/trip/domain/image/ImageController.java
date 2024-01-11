package com.example.trip.domain.image;

import com.example.trip.domain.image.domain.Image;
import com.example.trip.domain.member.domain.Member;
import com.example.trip.global.BaseResponse;
import com.example.trip.global.annotation.Login;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api")
@Tag(name = "Image", description = "이미지 관련 API")
public class ImageController {

    private final ImageManager imageManager;

    private final ImageService imageService;

    @Operation(summary = "이미지 등록", description = "1개 이상의 이미지를 S3에 저장한 뒤 데이터베이스에 정보를 저장합니다.")
    @PostMapping(value = "/v1/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResponse<List<Long>> createImage(@Parameter(hidden = true) @Login Member member, @RequestPart MultipartFile[] images) throws IOException {
        List<String> imageKeys = imageManager.uploadImages(List.of(images), UUID.randomUUID());
        List<Long> imageIds = imageService.createImages(imageKeys, member);
        return new BaseResponse<>(imageIds);
    }

    @Operation(summary = "이미지 등록", description = "1개 이상의 이미지를 S3에 저장한 뒤 데이터베이스에 정보를 저장합니다.")
    @PostMapping(value = "/v2/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResponse<List<Long>> createImageParallel(@Parameter(hidden = true) @Login Member member, @RequestPart MultipartFile[] images) throws IOException {
        List<String> imageKeys = imageManager.uploadImagesParallel(List.of(images), UUID.randomUUID());
        List<Long> imageIds = imageService.createImagesParallel(imageKeys, member);
        return new BaseResponse<>(imageIds);
    }

    @Scheduled(cron = "0 0 0 1 * ?")
    public void deleteImages() {
        List<Image> imageList = imageService.findDeleteImageList();
        imageManager.deleteImages(imageList);
        imageService.deleteImages(imageList);
    }

}
