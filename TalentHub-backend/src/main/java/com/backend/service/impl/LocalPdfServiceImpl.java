package com.backend.service.impl;

import com.backend.entity.child.account.freelancer.CV;
import com.backend.entity.child.account.freelancer.Freelancer;
import com.backend.repository.CVRepository;
import com.backend.repository.FreelancerRepository;
import com.backend.service.intf.LocalPdfService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;
import com.backend.dto.response.cv.CVWithJobsDTO;
import com.backend.dto.response.cv.CVJobDTO;
import com.backend.entity.child.job.Job;
import com.backend.entity.child.account.client.Company;
import com.backend.repository.CompanyRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

@Service
public class LocalPdfServiceImpl implements LocalPdfService {

    private final CompanyRepository companyRepository;
    @Value("${pdf.storage.location}")
    private String storageLocation;

    @Value("${pdf.base.url}")
    private String baseUrl;

    private final FreelancerRepository freelancerRepository;
    private final CVRepository cvRepository;

    public LocalPdfServiceImpl(FreelancerRepository freelancerRepository, CVRepository cvRepository, CompanyRepository companyRepository) {
        this.freelancerRepository = freelancerRepository;
        this.cvRepository = cvRepository;
        this.companyRepository = companyRepository;
    }

    private void initStorageLocation() {
        try {
            Path storagePath = Paths.get(storageLocation);
            if (!Files.exists(storagePath)) {
                Files.createDirectories(storagePath);
                System.out.println("Đã tạo thư mục lưu trữ: " + storagePath.toAbsolutePath());
            }
        } catch (IOException e) {
            throw new RuntimeException("Không thể khởi tạo thư mục lưu trữ", e);
        }
    }

    @Override
    public String uploadPdf(MultipartFile file, Long freelancerId) {
        try {
            if (!file.getContentType().equals("application/pdf")) {
                throw new IllegalArgumentException("File phải là định dạng PDF");
            }

            initStorageLocation();

            String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
            String uniqueFilename = System.currentTimeMillis() + "-" + originalFilename;

            Path targetPath = Paths.get(storageLocation).resolve(uniqueFilename);

            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            Freelancer freelancer = freelancerRepository.findById(freelancerId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy Freelancer với ID: " + freelancerId));

            CV cv = new CV();
            cv.setTitle(originalFilename);
            cv.setUrl(uniqueFilename);
            cv.setStatus(true);
            cv.setFreelancer(freelancer);
            cvRepository.save(cv);

            return uniqueFilename;
        } catch (IOException e) {
            throw new RuntimeException("Không thể upload PDF: " + e.getMessage(), e);
        }
    }

    @Override
    public List<String> uploadMultiplePdfs(MultipartFile[] files) {
        List<String> filenames = new ArrayList<>();
        for (MultipartFile file : files) {
            if (!file.getContentType().equals("application/pdf")) {
                throw new IllegalArgumentException("Tất cả file phải là định dạng PDF");
            }

            try {
                initStorageLocation();
                String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
                String uniqueFilename = System.currentTimeMillis() + "-" + originalFilename;
                Path targetPath = Paths.get(storageLocation).resolve(uniqueFilename);
                Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
                filenames.add(uniqueFilename);
            } catch (IOException e) {
                throw new RuntimeException("Không thể upload nhiều PDF: " + e.getMessage(), e);
            }
        }
        return filenames;
    }

    @Override
    public void deletePdfById(Long cvId) {
        try {
            CV cv = cvRepository.findById(cvId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy CV với ID: " + cvId));
            Path filePath = Paths.get(storageLocation).resolve(cv.getUrl());
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }
            cvRepository.delete(cv);
        } catch (IOException e) {
            throw new RuntimeException("Không thể xóa CV: " + e.getMessage(), e);
        }
    }

    @Override
    public void deletePdf(String filePath) {
        try {
            Path path = Paths.get(storageLocation).resolve(filePath);
            if (Files.exists(path)) {
                Files.delete(path);
            } else {
                throw new RuntimeException("File không tồn tại: " + filePath);
            }
        } catch (IOException e) {
            throw new RuntimeException("Không thể xóa PDF: " + e.getMessage(), e);
        }
    }

    @Override
    public String getPdfUrl(String filePath) {
        return baseUrl + "/api/pdf/view/" + filePath;
    }

    @Override
    public byte[] downloadPdf(String filePath) {
        try {
            Path path = Paths.get(storageLocation).resolve(filePath);
            if (!Files.exists(path)) {
                throw new RuntimeException("File không tồn tại: " + filePath);
            }
            return Files.readAllBytes(path);
        } catch (IOException e) {
            throw new RuntimeException("Không thể tải PDF: " + e.getMessage(), e);
        }
    }

    @Override
    public List<CV> getCVsByFreelancerId(Long freelancerId) {
        Freelancer freelancer = freelancerRepository.findById(freelancerId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Freelancer với ID: " + freelancerId));
        return cvRepository.findByFreelancer(freelancer);
    }

    @Override
    public List<CVWithJobsDTO> getCVsWithJobsByFreelancerId(Long freelancerId) {
        Freelancer freelancer = freelancerRepository.findById(freelancerId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Freelancer với ID: " + freelancerId));

        List<CV> cvs = cvRepository.findByFreelancer(freelancer);

        return cvs.stream().map(cv -> {
            CVWithJobsDTO dto = new CVWithJobsDTO();
            dto.setId(cv.getId());
            dto.setTitle(cv.getTitle());
            dto.setUrl(cv.getUrl());
            dto.setStatus(cv.getStatus());

            List<CVJobDTO> jobDTOs = cv.getFreelancerJobs().stream()
                    .map(freelancerJob -> {
                        Job job = freelancerJob.getJob();
                        String companyName = "";
                        try {
                            Company company = companyRepository.getCompanyByClientId(job.getClient().getId())
                                    .orElse(null);
                            if (company != null) {
                                companyName = company.getCompanyName();
                            }
                        } catch (Exception e) {
                        }

                        return new CVJobDTO(
                                job.getId(),
                                job.getTitle(),
                                companyName,
                                freelancerJob.getStatus()
                        );
                    })
                    .collect(Collectors.toList());
            dto.setJobs(jobDTOs);
            return dto;
        }).collect(Collectors.toList());
    }
}