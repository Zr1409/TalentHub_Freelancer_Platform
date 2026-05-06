package com.backend.dto.response.account.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import com.backend.enums.TypePackage;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class PackageHistoryDTOResponse {
    private Long id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Double price;
    private Long numberPost;
    private Long numberPosted;
    private Long postsUsed;
    private TypePackage packageType;
    private String packageTypeName;
    private boolean isActive;
    private String usagePeriod;
    private String status;
}