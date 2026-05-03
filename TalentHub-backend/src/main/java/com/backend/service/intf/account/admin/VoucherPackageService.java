package com.backend.service.intf.account.admin;

import com.backend.dto.request.account.admin.VoucherPackageDTORequest;
import com.backend.dto.response.account.admin.VoucherPackageDTOResponse;
import com.backend.enums.TypePackage;
import com.backend.service.BaseService;

import java.util.List;

public interface VoucherPackageService extends BaseService<VoucherPackageDTORequest, VoucherPackageDTOResponse, Long> {
    public VoucherPackageDTOResponse update(TypePackage typePackage, VoucherPackageDTORequest request);

    public String updateByName(String name, VoucherPackageDTORequest request);

    VoucherPackageDTOResponse getDetailByTypePackage(TypePackage typePackage);

    List<VoucherPackageDTOResponse> findLatestVoucherPackagesByType();
    List<VoucherPackageDTOResponse> findLatestVoucherPackagesByTypeOrdered();

    List<VoucherPackageDTOResponse> findLatestVoucherPackagesByTypeByClientId(Long clientId);
}
