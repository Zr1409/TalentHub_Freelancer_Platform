package com.backend.controller.pdf;

import com.backend.dto.response.cv.CVWithJobsDTO;
import com.backend.service.intf.LocalPdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pdf")
public class LocalPDFController {

    private final LocalPdfService pdfService;

    @Autowired
    public LocalPDFController(LocalPdfService pdfService) {
        this.pdfService = pdfService;
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadPdf(
            @RequestParam("file") MultipartFile file,
            @RequestParam("freelancerId") Long freelancerId) {

        try {
            String filename = pdfService.uploadPdf(file, freelancerId);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Upload CV thành công");
            response.put("url", filename);
            response.put("status", HttpStatus.OK.value());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("message", "Không thể upload CV: " + e.getMessage());
            error.put("status", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @PostMapping("/upload-multiple")
    public ResponseEntity<Map<String, Object>> uploadMultiplePdfs(@RequestParam("files") MultipartFile[] files) {
        try {
            List<String> filenames = pdfService.uploadMultiplePdfs(files);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Upload nhiều CV thành công");
            response.put("urls", filenames);
            response.put("status", HttpStatus.OK.value());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("message", "Không thể upload nhiều CV: " + e.getMessage());
            error.put("status", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @DeleteMapping("/{cvId}")
    public ResponseEntity<Map<String, Object>> deletePdf(@PathVariable Long cvId) {
        try {
            pdfService.deletePdfById(cvId);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "CV đã được xóa thành công");
            response.put("status", HttpStatus.OK.value());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("message", "Không thể xóa CV: " + e.getMessage());
            error.put("status", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadPdf(@RequestParam("filePath") String filePath) {
        try {
            byte[] pdfBytes = pdfService.downloadPdf(filePath);
            ByteArrayResource resource = new ByteArrayResource(pdfBytes);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", filePath);
            headers.setContentLength(pdfBytes.length);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/view/{filePath}")
    public ResponseEntity<Resource> viewPdf(@PathVariable String filePath) {
        try {
            byte[] pdfBytes = pdfService.downloadPdf(filePath);
            ByteArrayResource resource = new ByteArrayResource(pdfBytes);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentLength(pdfBytes.length);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/freelancer/{freelancerId}")
    public ResponseEntity<Map<String, Object>> getCVsWithJobsByFreelancerId(@PathVariable Long freelancerId) {
        try {
            List<CVWithJobsDTO> cvs = pdfService.getCVsWithJobsByFreelancerId(freelancerId);

            Map<String, Object> response = new HashMap<>();
            response.put("data", cvs);
            response.put("status", HttpStatus.OK.value());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("message", "Không thể lấy danh sách CV và công việc: " + e.getMessage());
            error.put("status", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
}