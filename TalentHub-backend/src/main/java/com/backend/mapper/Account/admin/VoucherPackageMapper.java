package com.backend.mapper.Account.admin;

import com.backend.dto.request.account.admin.VoucherPackageDTORequest;
import com.backend.dto.response.account.admin.VoucherPackageDTOResponse;
import com.backend.entity.child.admin.VoucherPackage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VoucherPackageMapper {

    @Mapping(source = "account.id", target = "accountId")
    VoucherPackageDTOResponse toDTO(VoucherPackage voucherPackage);

    @Mapping(source = "accountId", target = "account.id")
    VoucherPackage toEntity(VoucherPackageDTORequest voucherPackageDTORequest);
}
