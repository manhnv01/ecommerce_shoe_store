package com.nvm.shoestoreapi.controller;

import com.nvm.shoestoreapi.service.StorageService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import javax.servlet.http.HttpServletRequest;
import java.nio.file.Path;
import java.io.IOException;
import java.nio.file.Paths;

@RestController
@CrossOrigin("http://localhost:4200")
@AllArgsConstructor
public class ImageController {
    final StorageService storageService;

    @GetMapping("/images/**")
    public ResponseEntity<?> serveImage(HttpServletRequest request) throws IOException {
        // Lấy đường dẫn từ URL của yêu cầu
        String path = request.getRequestURI().substring(request.getContextPath().length() + "/images/".length());
        Path uploadPath = Paths.get("./uploads").resolve(path);

        // Kiểm tra xem tệp có tồn tại không
        Resource resource = new UrlResource(uploadPath.toUri());
        if (resource.exists() && resource.isReadable()) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "image/" + path)
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
