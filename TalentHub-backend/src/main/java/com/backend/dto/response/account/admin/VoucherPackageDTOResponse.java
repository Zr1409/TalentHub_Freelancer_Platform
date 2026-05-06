package com.backend.dto.response.account.admin;

import lombok.Data;
import com.backend.enums.TypePackage;

@Data
public class VoucherPackageDTOResponse {
    private Long id;
    private String name;
    private Double price;
    private Long duration;
    private TypePackage typePackage;
    private Long numberPost;
    private boolean status;
    private String createdAt;
    private String updatedAt;
    private Long accountId;
    private boolean myPackage;
}