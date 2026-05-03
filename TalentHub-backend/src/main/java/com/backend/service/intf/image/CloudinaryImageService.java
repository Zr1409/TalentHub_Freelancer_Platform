package com.backend.service.intf.image;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CloudinaryImageService {
    public String uploadImage(MultipartFile file, String folderName);

    public List<String> uploadMultipleImages(MultipartFile[] files, String folderName);

    public String updateImage(String oldSecureUrl, MultipartFile newFile, String folderName);

    public void deleteImage(String secureUrl);
}
