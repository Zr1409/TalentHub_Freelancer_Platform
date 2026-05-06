package com.backend.dto.response.job;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class CategoryDTOResponse {
    private Long id;
    private String categoryTitle;
    private Long quantityFreelancer;
    private Long quantityJob;
}