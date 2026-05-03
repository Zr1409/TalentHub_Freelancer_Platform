package com.backend.service.impl.account.freelancer;

import lombok.RequiredArgsConstructor;
import com.backend.dto.request.account.freelancer.CVDTORequest;
import com.backend.dto.response.account.freelancer.CVDTOResponse;
import com.backend.service.intf.account.freelancer.CVService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CVServiceImpl implements CVService {

    @Override
    public CVDTOResponse create(CVDTORequest cvdtoRequest) {
        return null;
    }

    @Override
    public Optional<CVDTOResponse> getById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public List<CVDTOResponse> getAll() {
        return List.of();
    }

    @Override
    public Boolean deleteById(Long aLong) {
        return null;
    }
}
