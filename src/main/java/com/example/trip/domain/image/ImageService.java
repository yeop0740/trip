package com.example.trip.domain.image;

import com.example.trip.domain.image.domain.Image;
import com.example.trip.domain.image.domain.UploadImageDTO;
import com.example.trip.domain.member.MemberRepository;
import com.example.trip.domain.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public List<Long> createImages(List<String> imageKeys, Member member) {
        List<Image> images = imageKeys.stream()
                .map(imageKey -> new Image(imageKey, member))
                .toList();
        imageRepository.saveAll(images);
        return images.stream()
                .map(Image::getId)
                .toList();
    }

    public List<Long> createImagesParallel(List<String> imageKeys, Member member) {
        List<Image> images = imageKeys.parallelStream()
                .map(imageKey -> new Image(imageKey, member))
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
