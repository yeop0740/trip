package com.example.trip.domain.image;

import com.example.trip.domain.image.domain.Image;
import com.example.trip.domain.image.domain.UploadImageDTO;
import com.example.trip.domain.member.MemberRepository;
import com.example.trip.domain.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;

    private final MemberRepository memberRepository;

    public Long createImage(UploadImageDTO imageInfo) {
        Image image = new Image(imageInfo);
        imageRepository.save(image);
        return image.getId();
    }

    public List<Long> createImages(Map<String, String> imageInfos, Member member) {
        List<Image> images = imageInfos.keySet().stream()
                .map(imageKey -> new Image(imageInfos.get(imageKey), imageKey, member))
                .toList();
        imageRepository.saveAll(images);
        return images.stream()
                .map(Image::getId)
                .toList();
    }

    public List<String> readImageKeys(List<Long> ids) {
        List<Image> imageList = imageRepository.findAllById(ids);
        return imageList.stream()
                .map(Image::getImageKey)
                .toList();
    }

    public List<Image> findDeleteImageList() {
        return imageRepository.findAllByPostIsNull();
    }

    public void deleteImages(List<Image> imageList) {
        imageRepository.deleteAll(imageList);
    }

}
