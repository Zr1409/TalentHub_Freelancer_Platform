package com.backend.service.impl.job;

import com.backend.dto.response.job.*;
import com.backend.entity.child.job.*;
import com.backend.enums.*;
import com.backend.mapper.job.*;
import com.backend.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.backend.dto.request.job.CreateJobDTORequest;
import com.backend.dto.request.job.JobAdminDTOResponse;
import com.backend.dto.request.job.JobDTORequest;
import com.backend.dto.request.job.JobDetailDTORequest;
import com.backend.dto.response.account.freelancer.FreelancerDTOResponse;
import com.backend.entity.child.account.client.Client;
import com.backend.entity.child.account.client.Company;
import com.backend.entity.child.account.client.SoldPackage;
import com.backend.entity.child.account.freelancer.Freelancer;
import com.backend.entity.child.account.freelancer.FreelancerSkill;
import com.backend.exception.AuthenticationException;
import com.backend.exception.BadRequestException;
import com.backend.exception.CommonException;
import com.backend.mapper.Account.client.ClientMapper;
import com.backend.service.impl.account.client.ClientServiceImpl;
import com.backend.service.impl.account.freelancer.FreelancerServiceImpl;
import com.backend.service.intf.EmailService;
import com.backend.service.intf.job.JobService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final ClientRepository clientRepository;
    private final CategoryRepository categoryRepository;
    private final DetailJobMapper detailJobMapper;
    private final CompanyRepository companyRepository;
    private final FreelancerJobRepository freelancerJobRepository;
    private final ApplyJobsMapper applyJobsMapper;
    private final PostedJobsMapper postedJobsMapper;
    private final JobAdminMapper jobAdminMapper;
    private final JobSkillRepository jobSkillRepository;
    private final JobMapper jobMapper;
    private final CreateJobMapper createJobMapper;
    private final SkillRepository skillRepository;
    private final JobDetailMapper jobDetailMapper;
    private final SoldPackageRepository soldPackageRepository;
    private final FreelancerRepository freelancerRepository;
    private final FreelancerServiceImpl freelancerServiceImpl;
    private final ClientServiceImpl clientServiceImpl;

    @Override
    @Transactional
    public JobDetailDTOResponse updateJob(Long id, JobDetailDTORequest jobDetailDTORequest) {
        if (!clientServiceImpl.checkValidClient(jobDetailDTORequest.getClientId())) {
            throw new BadRequestException("Client is banned");
        }

        Job job = jobRepository.findById(id).orElseThrow(() -> new RuntimeException("Job not found"));

        Category category = categoryRepository.findById(jobDetailDTORequest.getCategoryId())
                .orElseThrow(() -> new BadRequestException("Category not found"));
        job.setCategory(category);

        job.setTitle(jobDetailDTORequest.getTitle());
        job.setScope(ScopeJob.valueOf(jobDetailDTORequest.getScope()));
        job.setHourWork(jobDetailDTORequest.getHourWork());
        job.setDuration(jobDetailDTORequest.getDuration());
        job.setJobOpportunity(jobDetailDTORequest.getJobOpportunity());
        job.setFromPrice(jobDetailDTORequest.getFromPrice());
        job.setToPrice(jobDetailDTORequest.getToPrice());
        job.setTypePrice(jobDetailDTORequest.getTypePrice());
        job.setDescription(jobDetailDTORequest.getDescription());
        job.setTypePayment(jobDetailDTORequest.getTypePayment());
        job.setStatus(jobDetailDTORequest.getStatusJob());

        job.getJobSkills().clear();

        Job savedJob = jobRepository.save(job);
        List<Long> skillIds = jobDetailDTORequest.getSkillId();
        skillIds.forEach(skillId -> {
            Skill skill = skillRepository.findById(skillId)
                    .orElseThrow(() -> new BadRequestException("Skill id not found"));

            JobSkill jobSkill = new JobSkill();
            jobSkill.setSkill(skill);
            jobSkill.setJob(savedJob);

            jobSkillRepository.save(jobSkill);
        });
        return jobDetailMapper.toResponseDto(job);
    }

    @Override
    public JobDetailDTOResponse getJobById(Long id) {
        Job job = jobRepository.findById(id).orElse(null);

        Long totalApplicants = freelancerJobRepository.countByJobAndStatus(job, StatusFreelancerJob.Applied);

        JobDetailDTOResponse jobDetailDTOResponse = jobDetailMapper.toResponseDto(job);
        jobDetailDTOResponse.setTotalApplicants(totalApplicants);

        return jobDetailDTOResponse;
    }

    @Override
    public Boolean banJob(Long id) {
        Optional<Job> job = jobRepository.findById(id);
        if (job.isPresent()) {
            Job foundAccount = job.get();
            foundAccount.setStatus(StatusJob.BANNED);
            jobRepository.save(foundAccount);
            return true;
        }
        return false;
    }

    private final FreelancerSkillRepository freelancerSkillRepository;
    private final EmailService emailService;
    @Value("${ui.url}")
    private String urlUI;

    @Override
    public Boolean unBanJob(Long id) {
        Optional<Job> job = jobRepository.findById(id);
        if (job.isPresent()) {
            Job foundAccount = job.get();
            foundAccount.setStatus(StatusJob.OPEN);
            jobRepository.save(foundAccount);
            return true;
        }
        return false;
    }

    @Override
    public CreateJobDTOResponse createJob(CreateJobDTORequest createJobDTORequest) {
        Client client = clientRepository.findById(createJobDTORequest.getClientId())
                .orElseThrow(() -> new AuthenticationException("Client not found"));

        if (!clientServiceImpl.checkValidClient(createJobDTORequest.getClientId())) {
            throw new AuthenticationException("Client is banned");
        }

        SoldPackage soldPackage = soldPackageRepository.findTopByClientIdAndStatusOrderByStartDateDesc(client.getId(),
                true);

        if (soldPackage == null) {
            throw new CommonException("Sold package not found");
        }

        Long numberPost = soldPackage.getNumberPost();
        Long numberPosted = soldPackage.getNumberPosted();

        if (numberPosted >= numberPost) {
            throw new CommonException("You have used up all your posts");
        }

        createJobDTORequest.setClientId(client.getId());

        Category category = categoryRepository.findById(createJobDTORequest.getCategoryId())
                .orElseThrow(() -> new CommonException("Category not found"));

        createJobDTORequest.setCategoryId(category.getId());

        Job job = createJobMapper.toEntity(createJobDTORequest);

        Long durationPost = soldPackage.getVoucherPackage().getDuration();

        job.setEndDate(
                Date.from(LocalDateTime.now().plusDays(durationPost).atZone(ZoneId.systemDefault()).toInstant()));

        Job savedJob = jobRepository.save(job);

        List<Long> skillIds = createJobDTORequest.getSkillId();

        skillIds.forEach(skillId -> {
            Skill skill = skillRepository.findById(skillId)
                    .orElseThrow(() -> new CommonException("Skilll id not found"));

            JobSkill jobSkill = new JobSkill();
            jobSkill.setSkill(skill);
            jobSkill.setJob(savedJob);

            jobSkillRepository.save(jobSkill);
        });

        soldPackage.setNumberPosted(soldPackage.getNumberPosted() + 1);
        soldPackageRepository.save(soldPackage);

        return createJobMapper.toResponseDto(job);
    }

    @Override
    public void notifyByJobId(Long jobId) {
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new BadRequestException("Job not found"));

        List<Long> skillIds = jobSkillRepository.findByJobId(jobId).stream()
                .map(jobSkill -> jobSkill.getSkill().getId()).collect(Collectors.toList());

        List<String> skillNames = skillRepository.findAllById(skillIds).stream().map(Skill::getSkillName)
                .collect(Collectors.toList());

        String jobUrl = urlUI + "/jobs/" + jobId;
        String emailBody = """
                Một công việc mới có tiêu đề "%s" phù hợp với kỹ năng của bạn.
                <a href="%s" style="display: inline-block; padding: 12px 24px; background: #4F46E5; color: white; text-decoration: none; border-radius: 6px; margin: 20px 0; font-weight: bold;">Xem Công Việc</a>
                """
                .formatted(job.getTitle(), jobUrl);

        List<FreelancerSkill> matchingFreelancerSkills = freelancerSkillRepository.findBySkill_SkillNameIn(skillNames);
        Set<String> uniqueEmails = new HashSet<>();

        matchingFreelancerSkills.forEach(freelancerSkill -> {
            Freelancer freelancer = freelancerSkill.getFreelancer();
            if (freelancer != null && freelancer.getUser() != null && freelancer.getUser().getAccount() != null) {
                String freelancerEmail = freelancer.getUser().getAccount().getEmail();
                uniqueEmails.add(freelancerEmail);
            }
        });

        uniqueEmails.forEach(email -> {
            emailService.sendEmail(email, EmailType.JOB_NOTIFICATION, emailBody);
        });
    }

    @Override
    public JobDTOResponse create(JobDTORequest jobDTORequest) {
        return null;
    }

    @Override
    public Optional<JobDTOResponse> getById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public List<JobDTOResponse> getAll() {
        return List.of();
    }

    public JobAdminDTOResponse getJobAdminDTOResponse(Job job) {
        Long appliedQuantity = freelancerJobRepository.countByJobAndStatus(job, StatusFreelancerJob.Applied);
        Long cancelledQuantity = freelancerJobRepository.countByJobAndStatus(job, StatusFreelancerJob.Cancelled);
//        Long inProgressQuantity = freelancerJobRepository.countByJobAndStatus(job, StatusFreelancerJob.InProgress);
        Long approvedQuantity = freelancerJobRepository.countByJobAndStatus(job, StatusFreelancerJob.Approved);
        Long rejectedQuantity = freelancerJobRepository.countByJobAndStatus(job, StatusFreelancerJob.Rejected);
        Long viewedQuantity = freelancerJobRepository.countByJobAndStatus(job, StatusFreelancerJob.Viewed);

        return jobAdminMapper.toResponseDto(job, appliedQuantity, cancelledQuantity, approvedQuantity, rejectedQuantity,
                viewedQuantity);
    }

    @Override
    public List<JobAdminDTOResponse> getAllAdmin() {
        return jobRepository.findAll().stream().map(this::getJobAdminDTOResponse).toList();
    }

    @Override
    public Boolean deleteById(Long id) {
        Optional<Job> optionalJob = jobRepository.findById(id);
        if (optionalJob.isPresent()) {
            Job job = optionalJob.get();
            job.setStatus(StatusJob.CLOSED);
            jobRepository.save(job); // cập nhật thay vì xóa
            return true;
        }
        return false;
    }

    /*
     * @Override public List<JobDTOResponse> findAllJobs() { List<JobDTOResponse>
     * jobs = jobRepository.findAll().stream() .filter(job -> job.getStatus() ==
     * StatusJob.POSTED) .map(job -> { JobDTOResponse dto =
     * jobMapper.toResponseDto(job); Long clientId = job.getClient().getId();
     * companyRepository.getCompanyByClientId(clientId) .ifPresent(company ->
     * dto.setCompanyName(company.getCompanyName())); return dto; })
     * .collect(Collectors.toList());
     *
     * return jobs; }
     */
    private final ClientMapper clientMapper;

    @Override
    public List<JobDTOResponse> findAllJobs(Long freelancerId) {
        List<JobDTOResponse> jobs = jobRepository.findOpenJobsWithNonBannedClients(StatusJob.OPEN, StatusAccount.LOCKED)
                .stream().map(job -> {
                    JobDTOResponse dto = jobMapper.toResponseDto(job);
                    Long clientId = job.getClient().getId();
                    Optional<Client> byId = clientRepository.findById(clientId);
                    Client client = null;
                    if (byId.isPresent()) {
                        client = byId.get();
                        dto.setClient(clientMapper.toResponseDto(client));
                    }
                    companyRepository.getCompanyByClientId(job.getClient().getId())
                            .ifPresent(company -> {
                                dto.setCompanyName(company.getCompanyName());
                                dto.setCompanyImage(company.getCompanyImage());
                                dto.setCompanyAddress(company.getAddress());
                            });

                    boolean seen = freelancerJobRepository.existsByFreelancerIdAndJobId(freelancerId, job.getId());
                    boolean applied = freelancerJobRepository.existsByFreelancerIdAndJobIdAndStatus(freelancerId,
                            job.getId(), StatusFreelancerJob.Applied);
                    dto.setJobOpportunity(job.getJobOpportunity());
                    dto.setSeen(seen);
                    dto.setApplied(applied);
                    return dto;
                }).sorted(Comparator.comparing(JobDTOResponse::isSeen).thenComparing(JobDTOResponse::getCreatedAt,
                        Comparator.reverseOrder()))
                .collect(Collectors.toList());

        return jobs;
    }

    @Override
    public Optional<DetailJobDTOResponse> getDetailJobById(Long id) {
        try {
            Optional<Job> jobOpt = jobRepository.getDetailJobById(id);

            if (jobOpt.isPresent()) {
                Job job = jobOpt.get();
                List<FreelancerJob> appliedQuantity = freelancerJobRepository.findByJobId(job.getId()).stream().filter(
                                item -> item.getStatus() != null && item.getStatus().equals(StatusFreelancerJob.Applied))
                        .toList();

                DetailJobDTOResponse dto = detailJobMapper.toResponseDto(job);
                dto.setTotalApplicants(Long.valueOf(appliedQuantity.size()));
                /*
                 * Company company =
                 * companyRepository.getCompanyByClientId(job.getClient().getId())
                 * .orElseThrow(() -> new RuntimeException("Company not found for client ID: " +
                 * job.getClient().getId())); dto.setCompanyName(company.getCompanyName());
                 */

                // company null allowed
                companyRepository.getCompanyByClientId(job.getClient().getId())
                        .ifPresent(company -> {
                            dto.setCompanyName(company.getCompanyName());
                            dto.setCompanyImage(company.getCompanyImage());
                            dto.setCompanyAddress(company.getAddress());
                        });

                Long totalApplicants = freelancerJobRepository.countByJobAndStatus(job, StatusFreelancerJob.Applied);
                dto.setTotalApplicants(totalApplicants);

                Long totalViews = freelancerJobRepository.countViewsByJob(job);
                dto.setTotalViews(totalViews);

                return Optional.of(dto);
            }

            return Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<ApplyJobsDTOResponse> getApplyJobs(Long freeLancerId) {
        List<FreelancerJob> freelancerJobs = freelancerJobRepository.findFreelancerJobsByStatusAndFreelancerId(
                freeLancerId, StatusFreelancerJob.Applied, StatusFreelancerJob.Approved, StatusFreelancerJob.Rejected);
        try {
            return freelancerJobs.stream().map(freelancerJob -> {
                ApplyJobsDTOResponse dto = applyJobsMapper.toResponseDto(freelancerJob);
                Long id = freelancerJob.getJob().getClient().getId();
                Company company = companyRepository.getCompanyByClientId(id).orElse(null);
                dto.setCompanyName(company != null ? company.getCompanyName() : null);
                return dto;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            throw new BadRequestException("Error while fetching apply jobs");
        }
    }

    public List<PostJobsDTOResponse> getPostedJobs(Long clientId) {
        return jobRepository.getPostedJobs(clientId).stream().map(postedJobsMapper::toResponseDto).collect(toList());
    }

    @Override
    public List<JobWithPackageDTOResponse> getTop6JobsByTypePriority() {
        List<JobWithPackageDTOResponse> result = new ArrayList<>();
        Set<Long> usedClientIds = new HashSet<>();
        TypePackage[] priorityOrder = {TypePackage.DIAMOND, TypePackage.GOLD, TypePackage.SILVER, TypePackage.NORMAL};

        for (TypePackage type : priorityOrder) {
            if (result.size() >= 9)
                break;

            List<SoldPackage> soldPackages = soldPackageRepository.findByVoucherPackageType(type);
            for (SoldPackage sp : soldPackages) {
                if (result.size() >= 9)
                    break;

                Long clientId = sp.getClient().getId();
                if (!usedClientIds.contains(clientId)) {
                    List<Job> jobsByClient = jobRepository
                            .findByClientIdAndStatusNotBannedOrderByCreatedAtDesc(clientId, StatusAccount.LOCKED);
                    for (Job job : jobsByClient) {
                        if (result.size() < 9) {
                            JobWithPackageDTOResponse dto = jobMapper.toResponseWithPackageDto(job, type);
                            companyRepository.getCompanyByClientId(job.getClient().getId())
                                    .ifPresent(company -> {
                                        dto.setCompanyName(company.getCompanyName());
                                        dto.setCompanyImage(company.getCompanyImage());
                                        dto.setCompanyAddress(company.getAddress());
                                    });
                            result.add(dto);
                        } else {
                            break;
                        }
                    }
                    usedClientIds.add(clientId);
                }
            }
        }

        if (result.size() < 9) {
            List<Job> remainingJobs = jobRepository.findAllByOrderByCreatedAtDesc();
            for (Job job : remainingJobs) {
                if (!result.stream().anyMatch(dto -> dto.getId().equals(job.getId())) && result.size() < 6) {
                    JobWithPackageDTOResponse dto = jobMapper.toResponseWithPackageDto(job, TypePackage.NORMAL);

                    result.add(dto);
                }
                if (result.size() >= 9)
                    break;
            }
        }

        return result;
    }

    public List<JobDTOResponse> getRecommendedJobsForFreelancer(Long freelancerId) {
        log.info("Start fetching recommended jobs for freelancerId: {}", freelancerId);

        Freelancer freelancer = freelancerRepository.findById(freelancerId).orElseThrow(() -> {
            log.error("Freelancer with ID {} not found", freelancerId);
            return new BadRequestException("Freelancer not found");
        });

        Category freelancerCategory = freelancer.getCategory();
        Long categoryId = (freelancerCategory != null) ? freelancerCategory.getId() : null;
        log.info("Freelancer {} belongs to category ID: {}", freelancerId, categoryId);

        List<Job> recommendedJobs = jobRepository.findRecommendedJobsForFreelancer(categoryId, StatusAccount.LOCKED);
        log.info("Found {} recommended jobs for category ID: {}", recommendedJobs.size(), categoryId);

        List<JobDTOResponse> result = recommendedJobs.stream().limit(9).map(job -> {
            JobDTOResponse dto = jobMapper.toResponseDto(job);
            boolean applied = freelancerJobRepository.existsByFreelancerIdAndJobIdAndStatus(freelancerId, job.getId(),
                    StatusFreelancerJob.Applied);
            dto.setApplied(applied);

            companyRepository.getCompanyByClientId(job.getClient().getId())
                    .ifPresent(company -> {
                        dto.setCompanyName(company.getCompanyName());
                        dto.setCompanyImage(company.getCompanyImage());
                        dto.setCompanyAddress(company.getAddress());
                    });

            return dto;
        }).collect(Collectors.toList());

        log.info("Returning {} recommended jobs for freelancer {}", result.size(), freelancerId);
        return result;
    }

    @Override
    public List<FreelancerDTOResponse> getFreelancersByClientJobCategories(Long clientId) {
        List<Job> clientJobs = jobRepository.findByClientId(clientId);
        Set<Long> categoryIds = clientJobs.stream().filter(job -> job.getCategory() != null)
                .map(job -> job.getCategory().getId()).collect(Collectors.toSet());
        List<FreelancerDTOResponse> result = new ArrayList<>();
        for (Long categoryId : categoryIds) {
            List<FreelancerDTOResponse> freelancers = freelancerServiceImpl.getFreelancersByCategoryId(categoryId);
            result.addAll(freelancers);
            if (result.size() >= 9)
                break;
        }
        return result.stream().distinct().limit(9).collect(Collectors.toList());
    }

    @Override
    public boolean closeJob(Long id) {
        Optional<Job> job = jobRepository.findById(id);
        if (job.isPresent()) {
            job.get().setStatus(StatusJob.CLOSED);
            jobRepository.save(job.get());
            return true;
        }

        return false;
    }

}
