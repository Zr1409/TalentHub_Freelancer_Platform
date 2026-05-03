package com.backend.service.impl.account.freelancer;

import lombok.RequiredArgsConstructor;
import com.backend.dto.request.account.freelancer.SchoolDTORequest;
import com.backend.dto.response.account.freelancer.SchoolDTOResponse;
import com.backend.entity.child.account.freelancer.Education;
import com.backend.entity.child.account.freelancer.School;
import com.backend.repository.SchoolRepository;
import com.backend.repository.EducationRepository;
import com.backend.service.intf.account.freelancer.SchoolService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SchoolServiceImpl implements SchoolService {

    private final SchoolRepository schoolRepository;
    private final EducationRepository educationRepository;

    @Override
    public SchoolDTOResponse create(SchoolDTORequest schoolRequestDTO) {
        School school = new School();
        school.setSchoolName(schoolRequestDTO.getSchoolName());

        school = schoolRepository.save(school);

        Long quantityEducation = educationRepository.countBySchoolId(school.getId());

        return new SchoolDTOResponse(school.getId(), school.getSchoolName(), quantityEducation);
    }

    @Override
    public Optional<SchoolDTOResponse> getById(Long id) {
        Optional<School> school = schoolRepository.findById(id);
        if (school.isPresent()) {
            Long quantityEducation = educationRepository.countBySchoolId(school.get().getId());
            return Optional.of(new SchoolDTOResponse(school.get().getId(), school.get().getSchoolName(), quantityEducation));
        }
        return Optional.empty();
    }

    @Override
    public List<SchoolDTOResponse> getAll() {
        List<School> schools = schoolRepository.findAll();
        return schools.stream()
                .map(s -> {
                    Long quantityEducation = educationRepository.countBySchoolId(s.getId());
                    return new SchoolDTOResponse(s.getId(), s.getSchoolName(), quantityEducation);
                })
                .collect(Collectors.toList());
    }

    @Override
    public Boolean deleteById(Long id) {
        if (schoolRepository.existsById(id)) {
            List<Education> educations = educationRepository.findBySchoolId(id);
            for (Education education : educations) {

                educationRepository.delete(education);
            }
            schoolRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public SchoolDTOResponse update(Long id, SchoolDTORequest schoolDTORequest) {
        Optional<School> optionalSchool = schoolRepository.findById(id);
        if (!optionalSchool.isPresent()) {
            return null;
        }

        School school = optionalSchool.get();
        school.setSchoolName(schoolDTORequest.getSchoolName());

        school = schoolRepository.save(school);

        Long quantityEducation = educationRepository.countBySchoolId(school.getId());

        return new SchoolDTOResponse(school.getId(), school.getSchoolName(), quantityEducation);
    }
}
