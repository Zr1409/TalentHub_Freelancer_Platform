package com.backend.service.intf;

import com.backend.dto.request.BannerDTORequest;
import com.backend.dto.response.BannerDTOResponse;
import com.backend.dto.response.LogoDTOResponse;
import com.backend.service.BaseService;

import java.util.List;

public interface BannerService extends BaseService<BannerDTORequest, BannerDTOResponse, Long> {
    BannerDTOResponse update(Long id, BannerDTORequest bannerDTORequest);

    List<LogoDTOResponse> getAllLogo();
}
