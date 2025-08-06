package com.electronic.store.Electronic_Store.Services;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

public interface FileStorageService {
    String uploadFile(MultipartFile file, String path) throws IOException;
    ResponseEntity<Resource> getUploadedImage(Long id, String entity) throws IOException;
    void deleteUploadedImage(Long id, String entity);
}
