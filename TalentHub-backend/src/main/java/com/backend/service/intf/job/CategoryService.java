package com.backend.service.intf.job;

import com.backend.dto.request.job.CategoryDTORequest;
import com.backend.dto.response.job.CategoryDTOResponse;
import com.backend.service.BaseService;

public interface CategoryService extends BaseService<CategoryDTORequest, CategoryDTOResponse, Long> {
    CategoryDTOResponse update(Long id, CategoryDTORequest categoryDTORequest);
}
