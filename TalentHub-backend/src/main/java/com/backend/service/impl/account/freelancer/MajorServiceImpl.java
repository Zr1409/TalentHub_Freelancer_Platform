package com.backend.service.impl.account.freelancer;

import lombok.RequiredArgsConstructor;
import com.backend.dto.request.account.freelancer.MajorDTORequest;
import com.backend.dto.response.account.freelancer.MajorDTOResponse;
import com.backend.entity.child.account.freelancer.Major;
import com.backend.repository.MajorRepository;
import com.backend.service.intf.account.freelancer.MajorService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MajorServiceImpl implements MajorService {

    private final MajorRepository majorRepository;

    @Override
    public MajorDTOResponse create(MajorDTORequest majorDTORequest) {
        Major major = new Major();
        major.setMajorName(majorDTORequest.getMajorName());

        major = majorRepository.save(major);
        return new MajorDTOResponse(major.getId(), major.getMajorName());
    }

    @Override
    public Optional<MajorDTOResponse> getById(Long id) {
        Optional<Major> major = majorRepository.findById(id);
        return major.map(m -> new MajorDTOResponse(m.getId(), m.getMajorName()));
    }

    @Override
    public List<MajorDTOResponse> getAll() {
        List<Major> majors = majorRepository.findAll();
        return majors.stream()
                .map(m -> new MajorDTOResponse(m.getId(), m.getMajorName()))
                .collect(Collectors.toList());
    }

    @Override
    public Boolean deleteById(Long id) {
        if (majorRepository.existsById(id)) {
            majorRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

