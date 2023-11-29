package com.example.trip.domain.interaction.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InteractionRequest {

    @Schema(description = "게시물 번호")
    private Long postId;

    @Schema(description = "상호작용 타입 LIKE or SCRAP")
    private InteractionType type;

}
