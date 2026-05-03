package com.backend.service.impl.pdf;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.backend.dto.response.cv.CVJobDTO;
import com.backend.dto.response.cv.CVWithJobsDTO;
import com.backend.entity.child.account.client.Company;
import com.backend.entity.child.account.freelancer.CV;
import com.backend.entity.child.account.freelancer.Freelancer;
import com.backend.entity.child.job.Job;
import com.backend.repository.CVRepository;
import com.backend.repository.CompanyRepository;
import com.backend.repository.FreelancerRepository;
import com.backend.service.intf.CloudinaryPdfService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CloudinaryPdfServiceImpl implements CloudinaryPdfService {

    private final Cloudinary cloudinary;
    private final FreelancerRepository freelancerRepository;
    private final CVRepository cvRepository;
    private final CompanyRepository companyRepository;

    public CloudinaryPdfServiceImpl(
            @Value("${cloudinary.cloud-name}") String cloudName,
            @Value("${cloudinary.api-key}") String apiKey,
            @Value("${cloudinary.api-secret}") String apiSecret,
            FreelancerRepository freelancerRepository,
            CVRepository cvRepository, CompanyRepository companyRepository) {
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret));
        this.freelancerRepository = freelancerRepository;
        this.cvRepository = cvRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    public String uploadPdf(MultipartFile file, Long freelancerId) {
        try {
            if (!file.getContentType().equals("application/pdf")) {
                throw new IllegalArgumentException("File phải là định dạng PDF");
            }

            String publicId = System.currentTimeMillis() + "-" + file.getOriginalFilename();

            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "resource_type", "image",
                    "public_id", publicId,
                    "folder", "talenthub_cvPDF"
            ));

            Freelancer freelancer = freelancerRepository.findById(freelancerId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy Freelancer với ID: " + freelancerId));

            CV cv = new CV();
            cv.setTitle(file.getOriginalFilename());
            cv.setUrl((String) uploadResult.get("secure_url"));
            cv.setFreelancer(freelancer);
            cv.setStatus(true);
            cvRepository.save(cv);

            return (String) uploadResult.get("secure_url");
        } catch (IOException e) {
            throw new RuntimeException("Không thể upload PDF: " + e.getMessage());
        }
    }

    @Override
    public List<String> uploadMultiplePdfs(MultipartFile[] files) {
        List<String> publicIds = new ArrayList<>();
        for (MultipartFile file : files) {
            if (!file.getContentType().equals("application/pdf")) {
                throw new IllegalArgumentException("Tất cả file phải là định dạng PDF");
            }

            try {
                String publicId = System.currentTimeMillis() + "-" + file.getOriginalFilename();
                Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                        "resource_type", "image",
                        "public_id", publicId,
                        "folder", "talenthub_cvPDF"
                ));
                publicIds.add((String) uploadResult.get("secure_url"));

            } catch (IOException e) {
                throw new RuntimeException("Không thể upload nhiều PDF: " + e.getMessage());
            }
        }
        return publicIds;
    }


    @Override
    public void deletePdfById(Long cvId) {
        try {
            CV cv = cvRepository.findById(cvId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy CV với ID: " + cvId));

            String publicId = extractPublicIdFromUrl(cv.getUrl());
            cloudinary.uploader().destroy(publicId, ObjectUtils.asMap("resource_type", "raw"));

            cvRepository.delete(cv);
        } catch (Exception e) {
            throw new RuntimeException("Không thể xóa CV: " + e.getMessage());
        }
    }

    private String extractPublicIdFromUrl(String secureUrl) {
        String[] parts = secureUrl.split("/upload/");
        if (parts.length < 2) {
            throw new IllegalArgumentException("Định dạng secure_url không hợp lệ");
        }
        String afterUpload = parts[1];
        String publicId = afterUpload.replaceAll("^v[0-9]+/", "").split("\\.")[0];
        return publicId;
    }

    @Override
    public void deletePdf(String secureUrl) {
        try {
            String publicId = extractPublicIdFromUrl(secureUrl);
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new RuntimeException("Không thể xóa PDF: " + e.getMessage());
        }
    }

    @Override
    public String getPdfUrl(String secureUrl) {
        try {
            String publicId = extractPublicIdFromUrl(secureUrl);
            Map resource = cloudinary.api().resource(publicId, ObjectUtils.asMap("resource_type", "raw"));
            return (String) resource.get("secure_url");
        } catch (Exception e) {
            throw new RuntimeException("Không thể lấy URL PDF: " + e.getMessage());
        }
    }

    @Override
    public byte[] downloadPdf(String publicId) {
        try {
            System.out.println("Downloading PDF with publicId: " + publicId);

            String signedUrl = cloudinary.url()
                    .resourceType("image")
                    .signed(true)
                    .publicId(publicId)
                    .generate();
            System.out.println("Generated signedUrl: " + signedUrl);

            URL url = new URL(signedUrl);
            Path tempFile = Paths.get("temp.pdf");
            Files.copy(url.openStream(), tempFile, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            byte[] pdfBytes = Files.readAllBytes(tempFile);
            Files.delete(tempFile);
            System.out.println("Downloaded PDF size: " + pdfBytes.length + " bytes");
            return pdfBytes;
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Không thể tải PDF: " + e.getMessage());
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