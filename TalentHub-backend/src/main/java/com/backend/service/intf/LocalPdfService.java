package com.backend.service.intf;

import com.backend.dto.response.cv.CVWithJobsDTO;
import com.backend.entity.child.account.freelancer.CV;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface LocalPdfService {
    String uploadPdf(MultipartFile file, Long freelancerId);

    List<String> uploadMultiplePdfs(MultipartFile[] files);

    void deletePdfById(Long cvId);

    void deletePdf(String filePath);

    String getPdfUrl(String filePath);

    byte[] downloadPdf(String filePath);

    List<CV> getCVsByFreelancerId(Long freelancerId);

    List<CVWithJobsDTO> getCVsWithJobsByFreelancerId(Long freelancerId);
}