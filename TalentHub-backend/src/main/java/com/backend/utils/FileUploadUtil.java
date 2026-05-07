package com.backend.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
public class FileUploadUtil {

    // Inject giá trị từ file cấu hình
    @Value("${file.upload.dir}")
    private String uploadDir;

    /**
     * Lưu tệp được upload với tên file truyền vào.
     *
     * @param fileName      Tên của tệp.
     * @param multipartFile Đối tượng MultipartFile chứa tệp được upload.
     * @throws IOException Nếu có lỗi khi lưu tệp.
     */
    public void saveFile(String fileName, MultipartFile multipartFile, String folderName) throws IOException {
        String directory = uploadDir + folderName; // Thư mục đầy đủ
        Path uploadPath = Paths.get(directory);

        // Tạo thư mục nếu chưa tồn tại
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(multipartFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IOException("Không thể lưu tệp: " + fileName, e);
        }
    }
}
