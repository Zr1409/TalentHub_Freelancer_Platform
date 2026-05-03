package com.backend.service.intf.account.client;

import com.backend.dto.request.account.client.CompanyDTORequest;
import com.backend.dto.response.account.client.CompanyDTOResponse;
import com.backend.service.BaseService;

import java.util.Optional;

public interface CompanyService extends BaseService<CompanyDTORequest, CompanyDTOResponse,Long> {
    Optional<CompanyDTOResponse> update(Long id, CompanyDTORequest companyDTORequest);
}
