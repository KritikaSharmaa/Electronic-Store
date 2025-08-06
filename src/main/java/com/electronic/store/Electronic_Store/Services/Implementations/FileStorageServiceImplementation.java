package com.electronic.store.Electronic_Store.Services.Implementations;

import com.electronic.store.Electronic_Store.ExceptionHandler.CustomExceptions.FileStorageException;
import com.electronic.store.Electronic_Store.Services.FileStorageService;
import com.electronic.store.Electronic_Store.Utils.ImageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageServiceImplementation implements FileStorageService {
    @Value("${file.upload-dir}")
    private String uploadPath;

    @Autowired
    ImageHelper imageHelper;

    @Override
    public String uploadFile(MultipartFile file, String path){
         // 1. Get original filename and extract extension
        String Original_filename = file.getOriginalFilename();

        String file_extension= Original_filename != null ? Original_filename.substring(Original_filename.lastIndexOf(".")) : null;

        // 2. Generate a unique file name
        String NewGeneratedFileName = UUID.randomUUID().toString()+file_extension;

        // 3. Full path where file will be saved
        //String fullPath = uploadPath + File.separator + path + File.separator+ NewGeneratedFileName;
        String fullPath = Paths.get(uploadPath, path, NewGeneratedFileName).toAbsolutePath().toString();

        // 4. Ensure directory exists
        File dir = new File(Paths.get(uploadPath, path).toString());
        if (!dir.exists()) {
            System.out.println("folder already exists..");
            dir.mkdirs();
        }
        // 5. Save the file in given path
        try {
            file.transferTo(new File(fullPath));
        } catch (IOException e) {
            throw new FileStorageException("Could not store file: " + e.getMessage());
        }
        return NewGeneratedFileName;
    }

    @Override
    public ResponseEntity<Resource> getUploadedImage(Long id, String entity) throws IOException {
        //1. Get Image Name
        String imageName= imageHelper.getImageName(id, entity);
        // 2. Build image path
        Path fullPath = Paths.get(uploadPath, entity, imageName).toAbsolutePath();

        // 3. Wrap image in a Resource
        Resource resource = new UrlResource(fullPath.toUri());

        String contentType = Files.probeContentType(fullPath);

        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        // 4. Return the image as response
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }

    @Override
    public void deleteUploadedImage(Long id, String entity) {
        String imageName= imageHelper.getImageName(id, entity);
        if (imageName != null && !imageName.isBlank()) {
            Path fullPath = Paths.get(uploadPath,entity, imageName).toAbsolutePath();
            try {
                Files.deleteIfExists(fullPath);
                System.out.println("Profile image deleted from: " + fullPath);
            } catch (IOException e) {
                System.err.println("Failed to delete image: " + e.getMessage());
            }
        }
    }

}
