package com.backend.service.intf.account.client;

import com.backend.dto.request.account.client.SoldPackageDTORequest;
import com.backend.dto.response.account.client.CurrentPackageDTOResponse;
import com.backend.dto.response.account.client.PackageHistoryDTOResponse;
import com.backend.dto.response.account.client.SoldPackageDTOResponse;
import com.backend.service.BaseService;

import java.util.List;
import java.util.Optional;

public interface SoldPackageService extends BaseService<SoldPackageDTORequest, SoldPackageDTOResponse, Long> {
    Optional<CurrentPackageDTOResponse> getCurrentPackage(Long clientId);

    List<PackageHistoryDTOResponse> getPackageHistory(Long clientId);
}
