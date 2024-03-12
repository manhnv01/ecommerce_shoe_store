package com.nvm.shoestoreapi.service.impl;

import com.nvm.shoestoreapi.service.StorageService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.nvm.shoestoreapi.util.Constant.*;

@Service
@Transactional
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
                    throw new RuntimeException(IMAGE_SIZE_TOO_LARGE_10MB);
                }
                String contentType = file.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    throw new RuntimeException(IMAGE_NOT_VALID);
                }
                //String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
                String uniqueFilename = StringUtils.cleanPath(
                        UUID.randomUUID() + "." + FilenameUtils.getExtension(file.getOriginalFilename()));
                //String uniqueFilename = UUID.randomUUID() + "_" + filename;
                Path destination = Paths.get(uploadDir.toString(), uniqueFilename);
                Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
                return uniqueFilename;
            } else
                throw new RuntimeException(IMAGE_NOT_FOUND);
        } catch (IOException e) {
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
                        throw new RuntimeException(IMAGE_SIZE_TOO_LARGE_10MB);
                    }
                    String contentType = file.getContentType();
                    if (contentType == null || !contentType.startsWith("image/")) {
                        throw new RuntimeException(IMAGE_NOT_VALID);
                    }
                    //String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
                    String uniqueFilename = StringUtils.cleanPath(
                            UUID.randomUUID() + "." + FilenameUtils.getExtension(file.getOriginalFilename()));
                    //String uniqueFilename = UUID.randomUUID() + "_" + filename;
                    Path destination = Paths.get(uploadDir.toString(), uniqueFilename);
                    Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
                    uniqueFilenames.add(uniqueFilename);
                } else {
                    throw new RuntimeException(IMAGE_NOT_FOUND);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return uniqueFilenames;
    }

    @Override
    public void deleteFile(String filename) {
        try {
            Path filePath = Paths.get("./uploads").resolve(filename).normalize().toAbsolutePath();
            Files.delete(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
