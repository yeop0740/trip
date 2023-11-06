package com.example.trip.domain.interaction.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateInteractionRequest {

    private Long postId;
    private InteractionType type;

}
