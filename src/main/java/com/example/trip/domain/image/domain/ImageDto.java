package com.example.trip.domain.image.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageDto {

    private Long imageId;

    private String imageUrl;

    public static ImageDto of(Long imageId, String imageUrl) {
        return new ImageDto(imageId, imageUrl);
    }

}
