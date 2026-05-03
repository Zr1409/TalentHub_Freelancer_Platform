package com.backend.service.impl.account.freelancer;

import lombok.RequiredArgsConstructor;
import com.backend.dto.request.account.freelancer.DegreeDTORequest;
import com.backend.dto.response.account.freelancer.DegreeDTOResponse;
import com.backend.entity.child.account.freelancer.Degree;
import com.backend.repository.DegreeRepository;
import com.backend.repository.EducationRepository;
import com.backend.service.intf.account.freelancer.DegreeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DegreeServiceImpl implements DegreeService {

    private final DegreeRepository degreeRepository;
    private final EducationRepository educationRepository;

    @Override
    public DegreeDTOResponse update(Long id, DegreeDTORequest degreeDTORequest) {
        Optional<Degree> optionalDegree = degreeRepository.findById(id);
        if (!optionalDegree.isPresent()) {
            return null;
        }

        Degree degree = optionalDegree.get();
        degree.setDegreeTitle(degreeDTORequest.getDegreeTitle());

        degree = degreeRepository.save(degree);

        Long quantityEducation = educationRepository.countByDegreeId(degree.getId());

        return new DegreeDTOResponse(degree.getId(), degree.getDegreeTitle(), quantityEducation);
    }

    @Override
    public DegreeDTOResponse create(DegreeDTORequest degreeDTORequest) {
        Degree degree = new Degree();
        degree.setDegreeTitle(degreeDTORequest.getDegreeTitle());
        degree = degreeRepository.save(degree);

        Long quantityEducation = educationRepository.countByDegreeId(degree.getId());

        return new DegreeDTOResponse(degree.getId(), degree.getDegreeTitle(), quantityEducation);
    }

    @Override
    public Optional<DegreeDTOResponse> getById(Long id) {
        Optional<Degree> degree = degreeRepository.findById(id);
        if (degree.isPresent()) {
            Long quantityEducation = educationRepository.countByDegreeId(degree.get().getId());
            return Optional.of(new DegreeDTOResponse(degree.get().getId(), degree.get().getDegreeTitle(), quantityEducation));
        }
        return Optional.empty();
    }

    @Override
    public List<DegreeDTOResponse> getAll() {
        List<Degree> degrees = degreeRepository.findAll();
        return degrees.stream()
                .map(d -> {
                    Long quantityEducation = educationRepository.countByDegreeId(d.getId());
                    return new DegreeDTOResponse(d.getId(), d.getDegreeTitle(), quantityEducation);
                })
                .collect(Collectors.toList());
    }

    @Override
    public Boolean deleteById(Long id) {
        if (degreeRepository.existsById(id)) {
            degreeRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
