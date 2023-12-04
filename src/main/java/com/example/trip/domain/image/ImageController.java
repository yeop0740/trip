package com.example.trip.domain.image;

import com.example.trip.domain.image.domain.CreateImageRequest;
import com.example.trip.domain.member.domain.Member;
import com.example.trip.global.BaseResponse;
import com.example.trip.global.annotation.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/image")
public class ImageController {

    private final ImageManager imageManager;

    private final ImageService imageService;

    @PostMapping
    public BaseResponse<List<Long>> createImage(@Login Member member, @ModelAttribute CreateImageRequest request) throws IOException {
        Map<String, String> imageInfos = imageManager.uploadImages(request.getImages(), UUID.randomUUID());
        List<Long> imageIds = imageService.createImages(imageInfos);
        return new BaseResponse<>(imageIds);
    }

}
