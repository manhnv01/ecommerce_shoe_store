package com.nvm.shoestoreapi.service.impl;

import com.nvm.shoestoreapi.service.StorageService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static java.text.Normalizer.normalize;

@Service
public class StorageServiceImpl implements StorageService {

    @Override
    public String saveFile(MultipartFile file) {
        try {
            if (file != null) {
                Path uploadDir = Paths.get("./uploads");
                if (!Files.exists(uploadDir)) {
                    Files.createDirectories(uploadDir);
                }
                if (file.getSize() > 10 * 1024 * 1024) {
                    throw new RuntimeException("Kích thước ảnh không được quá 10MB !");
                }
                String contentType = file.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    throw new RuntimeException("File không hợp lệ !");
                }
                String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
                String uniqueFilename = UUID.randomUUID() + "_" + filename;
                Path destination = Paths.get(uploadDir.toString(), uniqueFilename);
                Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
                return uniqueFilename;
            } else
                throw new RuntimeException("Không thể để trống Ảnh !");
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<String> saveFiles(List<MultipartFile> files) {
        List<String> uniqueFilenames = new ArrayList<>();

        try {
            for (MultipartFile file : files) {
                if (file != null && !file.isEmpty()) {
                    Path uploadDir = Paths.get("./uploads");
                    if (!Files.exists(uploadDir)) {
                        Files.createDirectories(uploadDir);
                    }
                    if (file.getSize() > 10 * 1024 * 1024) {
                        throw new RuntimeException("Kích thước ảnh không được quá 10MB !");
                    }
                    String contentType = file.getContentType();
                    if (contentType == null || !contentType.startsWith("image/")) {
                        throw new RuntimeException("File không hợp lệ !");
                    }
                    String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
                    String uniqueFilename = UUID.randomUUID() + "_" + filename;
                    Path destination = Paths.get(uploadDir.toString(), uniqueFilename);
                    Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
                    uniqueFilenames.add(uniqueFilename);
                } else {
                    throw new RuntimeException("Không thể để trống Ảnh !");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return uniqueFilenames;
    }

    @Override
    public void deleteFile(String filename) throws IOException {
        Path filePath = Paths.get("./uploads").resolve(filename).normalize().toAbsolutePath();
        Files.delete(filePath);
    }
}
