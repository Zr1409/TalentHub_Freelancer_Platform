package com.backend.service.impl.account.freelancer;

import lombok.RequiredArgsConstructor;
import com.backend.dto.response.account.freelancer.FreelancerDetailDTOResponse;
import com.backend.entity.child.account.freelancer.Freelancer;
import com.backend.entity.child.account.freelancer.Project;
import com.backend.enums.StatusFreelancerJob;
import com.backend.exception.BadRequestException;
import com.backend.repository.ClientReviewRepository;
import com.backend.repository.FreelancerJobRepository;
import com.backend.repository.FreelancerRepository;
import com.backend.repository.ProjectRepository;
import com.backend.service.intf.account.freelancer.FreelancerDetailService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FreelancerDetailServiceImpl implements FreelancerDetailService {
    private final FreelancerRepository freelancerRepository;
    private final ClientReviewRepository clientReviewRepository;
    private final FreelancerJobRepository freelancerJobRepository;
    private final ProjectRepository projectRepository;

    @Override
    public FreelancerDetailDTOResponse getFreelancerDetail(Long freelancerId) {
        Optional<Freelancer> freelancer = freelancerRepository.findById(freelancerId);

        if (!freelancer.isPresent()) {
            throw new BadRequestException("Freelancer not found");
        }

        Float rating = clientReviewRepository.findAverageRating(freelancerId);
        Long completedProject = freelancerJobRepository.countByFreelancerIdAndStatusAndHasClientReview(
                freelancerId, StatusFreelancerJob.Approved);

        List<Project> projects = projectRepository.findByFreelancerId(freelancerId);

        Freelancer freelancerExist = freelancer.get();
        FreelancerDetailDTOResponse dtoResponse = new FreelancerDetailDTOResponse();
        dtoResponse.setId(freelancerExist.getId());
        dtoResponse.setName(freelancerExist.getUser().getLastName() + " " + freelancerExist.getUser().getFirstName());
        dtoResponse.setTitle(freelancerExist.getUser().getTitle());
        dtoResponse.setUserId(freelancerExist.getUser().getId());
        dtoResponse.setAvatar(freelancerExist.getUser().getImage());
        dtoResponse.setProvince(freelancerExist.getUser().getProvince());
        dtoResponse.setWard(freelancerExist.getUser().getWard());
        dtoResponse.setRating(rating != null ? rating : 0.0f);
        dtoResponse.setCompleteProject(completedProject != null ? completedProject : 0L);
        dtoResponse.setHourlyRate(freelancerExist.getHourlyRate());
        dtoResponse.setOverview(freelancerExist.getUser().getIntroduction());

        if (freelancerExist.getCategory() != null) {
            dtoResponse.setCategoryTitle(freelancerExist.getCategory().getCategoryTitle());
        }
        dtoResponse.setSkills(freelancerExist.getFreelancerSkills()
                .stream()
                .map(fs -> fs.getSkill().getSkillName())
                .collect(Collectors.toList()));

        dtoResponse.setEducations(freelancerExist.getEducations());
        dtoResponse.setExperiences(freelancerExist.getExperiences());
        dtoResponse.setProjects(projects); // Set the projects
        dtoResponse.setLat(freelancerExist.getUser().getAccount().getLat() != null ? freelancerExist.getUser().getAccount().getLat() : 0.0);
        dtoResponse.setLng(freelancerExist.getUser().getAccount().getLng() != null ? freelancerExist.getUser().getAccount().getLng() : 0.0);
        return dtoResponse;
    }
}
