package org.nguyenlinhchi.dogiadung.SERVICE;

import org.nguyenlinhchi.dogiadung.CONFIG.FileStorageConfig;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileUploadService {

    private final FileStorageConfig fileStorageConfig;

    public FileUploadService(FileStorageConfig fileStorageConfig) {
        this.fileStorageConfig = fileStorageConfig;
    }

    public String saveImage(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        String uploadDir = fileStorageConfig.getUploadDir();
        Path uploadPath = Paths.get(uploadDir);

        // Tạo tên file unique
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null && originalFilename.contains(".")
                ? originalFilename.substring(originalFilename.lastIndexOf("."))
                : ".jpg";

        String fileName = UUID.randomUUID() + extension;
        Path filePath = uploadPath.resolve(fileName);

        Files.copy(file.getInputStream(), filePath);

        return "/uploads/products/" + fileName;   // URL truy cập
    }

    public boolean deleteImage(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) return false;
        try {
            String fileName = imageUrl.substring(imageUrl.lastIndexOf('/') + 1);
            Path filePath = Paths.get(fileStorageConfig.getUploadDir()).resolve(fileName);
            return Files.deleteIfExists(filePath);
        } catch (Exception e) {
            System.err.println("Không xóa được ảnh: " + e.getMessage());
            return false;
        }
    }
}