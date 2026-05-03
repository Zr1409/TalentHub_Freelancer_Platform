package com.backend.service.impl.account.freelancer;

import com.backend.repository.*;
import lombok.RequiredArgsConstructor;
import com.backend.dto.request.account.freelancer.EducationDTORequest;
import com.backend.dto.response.account.freelancer.EducationDTOResponse;
import com.backend.entity.child.account.freelancer.Education;
import com.backend.service.intf.account.freelancer.EducationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EducationServiceImpl implements EducationService {

    private final EducationRepository educationRepository;
    private final SchoolRepository schoolRepository;
    private final DegreeRepository degreeRepository;
    private final MajorRepository majorRepository;
    private final FreelancerRepository freelancerRepository;

    @Override
    public EducationDTOResponse create(EducationDTORequest educationDTORequest) {
        Education education = new Education();
        education.setStartDate(educationDTORequest.getStartDate());
        education.setEndDate(educationDTORequest.getEndDate());
        education.setDescription(educationDTORequest.getDescription());

        education.setSchool(schoolRepository.findById(educationDTORequest.getSchoolId()).orElse(null));
        education.setDegree(degreeRepository.findById(educationDTORequest.getDegreeId()).orElse(null));
        education.setMajor(majorRepository.findById(educationDTORequest.getMajorId()).orElse(null));
        education.setFreelancer(freelancerRepository.findById(educationDTORequest.getFreelancerId()).orElse(null));

        Education savedEducation = educationRepository.save(education);
        return new EducationDTOResponse(savedEducation.getId(), savedEducation.getStartDate(), savedEducation.getEndDate(), savedEducation.getDescription(),
                savedEducation.getSchool(), savedEducation.getDegree(), savedEducation.getMajor(), savedEducation.getFreelancer());
    }

    @Override
    public Optional<EducationDTOResponse> getById(Long id) {
        Optional<Education> educationOptional = educationRepository.findById(id);
        if (educationOptional.isPresent()) {
            Education education = educationOptional.get();
            EducationDTOResponse response = new EducationDTOResponse(education.getId(), education.getStartDate(), education.getEndDate(),
                    education.getDescription(), education.getSchool(), education.getDegree(), education.getMajor(), education.getFreelancer());
            return Optional.of(response);
        }
        return Optional.empty();
    }

    @Override
    public List<EducationDTOResponse> getAll() {
        List<Education> educationList = educationRepository.findAll();
        return educationList.stream().map(education -> new EducationDTOResponse(
                education.getId(), education.getStartDate(), education.getEndDate(), education.getDescription(),
                education.getSchool(), education.getDegree(), education.getMajor(), education.getFreelancer()
        )).toList();
    }

    @Override
    public Boolean deleteById(Long id) {
        if (educationRepository.existsById(id)) {
            educationRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public List<EducationDTOResponse> getByFreelancerId(Long freelancerId) {
        List<Education> educationList = educationRepository.findByFreelancerId(freelancerId);
        return educationList.stream()
                .map(education -> new EducationDTOResponse(
                        education.getId(),
                        education.getStartDate(),
                        education.getEndDate(),
                        education.getDescription(),
                        education.getSchool(),
                        education.getDegree(),
                        education.getMajor(),
                        education.getFreelancer()
                ))
                .toList();
    }

    @Override
    public EducationDTOResponse update(Long id, EducationDTORequest educationDTORequest) {
        Optional<Education> educationOptional = educationRepository.findById(id);
        if (educationOptional.isEmpty()) {
            throw new RuntimeException("Education with id " + id + " not found");
        }

        Education education = educationOptional.get();
        education.setStartDate(educationDTORequest.getStartDate());
        education.setEndDate(educationDTORequest.getEndDate());
        education.setDescription(educationDTORequest.getDescription());

        education.setSchool(schoolRepository.findById(educationDTORequest.getSchoolId())
                .orElseThrow(() -> new RuntimeException("School not found")));
        education.setDegree(degreeRepository.findById(educationDTORequest.getDegreeId())
                .orElseThrow(() -> new RuntimeException("Degree not found")));
        education.setMajor(majorRepository.findById(educationDTORequest.getMajorId())
                .orElseThrow(() -> new RuntimeException("Major not found")));
        education.setFreelancer(freelancerRepository.findById(educationDTORequest.getFreelancerId())
                .orElseThrow(() -> new RuntimeException("Freelancer not found")));

        Education updatedEducation = educationRepository.save(education);

        return new EducationDTOResponse(
                updatedEducation.getId(),
                updatedEducation.getStartDate(),
                updatedEducation.getEndDate(),
                updatedEducation.getDescription(),
                updatedEducation.getSchool(),
                updatedEducation.getDegree(),
                updatedEducation.getMajor(),
                updatedEducation.getFreelancer()
        );
    }
}