package com.backend.controller.pdf;

import com.backend.dto.response.cv.CVWithJobsDTO;
import com.backend.service.intf.CloudinaryPdfService;
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
@RequestMapping("/api/pdf/cloudinary")
public class PDFController {

    private final CloudinaryPdfService pdfService;


    public PDFController(CloudinaryPdfService pdfService) {
        this.pdfService = pdfService;
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadPdf(@RequestParam("file") MultipartFile file, @RequestParam("freelancerId") Long freelancerId) {
        String publicId = pdfService.uploadPdf(file, freelancerId);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Upload successful");
        response.put("url", publicId);
        response.put("status", HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }


    @PostMapping("/upload-multiple")
    public ResponseEntity<List<String>> uploadMultiplePdfs(@RequestParam("files") MultipartFile[] files) {
        List<String> publicIds = pdfService.uploadMultiplePdfs(files);
        return ResponseEntity.ok(publicIds);
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
    public ResponseEntity<byte[]> downloadPdf(@RequestParam("publicId") String publicId) {
        byte[] pdfBytes = pdfService.downloadPdf(publicId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "downloaded.pdf");
        headers.setContentLength(pdfBytes.length);
        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    //    @GetMapping("/freelancer/{freelancerId}")
//    public ResponseEntity<Map<String, Object>> getCVsByFreelancerId(@PathVariable Long freelancerId) {
//        Map<String, Object> response = new HashMap<>();
//        List<CV> cvs = pdfService.getCVsByFreelancerId(freelancerId);
//        response.put("data", cvs);
//        response.put("status", HttpStatus.OK.value());
//
//        return ResponseEntity.ok(response);
//    }
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

    @GetMapping("/preview-url")
    public ResponseEntity<Map<String, Object>> getPreviewUrl(@RequestParam("publicId") String publicId) {
        try {
            String previewUrl = pdfService.getPdfUrl(publicId);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Lấy URL xem trước thành công");
            response.put("url", previewUrl);
            response.put("status", HttpStatus.OK.value());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("message", "Không thể lấy URL xem trước: " + e.getMessage());
            error.put("status", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
}