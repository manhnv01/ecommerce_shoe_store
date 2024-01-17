package com.nvm.shoestoreapi.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface StorageService {
    String saveFile(MultipartFile file);
    List<String> saveFiles(List<MultipartFile> files);
    void deleteFile(String filename) throws IOException;
}
