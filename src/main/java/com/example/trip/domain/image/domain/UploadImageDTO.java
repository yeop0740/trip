package com.example.trip.domain.image.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UploadImageDTO {

    private String key;

    private String url;

}
