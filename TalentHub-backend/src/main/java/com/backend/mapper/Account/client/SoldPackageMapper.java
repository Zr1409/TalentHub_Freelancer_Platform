package com.backend.mapper.Account.client;

import com.backend.dto.request.account.client.SoldPackageDTORequest;
import com.backend.dto.response.account.client.SoldPackageDTOResponse;
import com.backend.entity.child.account.client.SoldPackage;
import com.backend.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SoldPackageMapper extends BaseMapper<SoldPackage, SoldPackageDTORequest, SoldPackageDTOResponse> {
    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "voucherPackage.id", target = "voucherPackageId")
    SoldPackageDTOResponse toResponseDto(SoldPackage soldPackage);

    @Mapping(source = "clientId", target = "client.id")
//    @Mapping(source = "voucherId", target = "voucherPackage.id")
    SoldPackage toEntity(SoldPackageDTORequest soldPackageDTORequest);
}
