package com.electronic.store.Electronic_Store.Controllers;

import com.electronic.store.Electronic_Store.DTOs.PaginatedResponseDTO;
import com.electronic.store.Electronic_Store.DTOs.userDTO;
import com.electronic.store.Electronic_Store.Services.FileStorageService;
import com.electronic.store.Electronic_Store.Services.userServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/users")
public class userController {

    @Autowired
    private userServices userServices;

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping
    public ResponseEntity<userDTO> createUser(@Valid @RequestBody userDTO userDto){
       userDTO saved_user = userServices.createUser(userDto);
       return new ResponseEntity<>(saved_user, HttpStatus.CREATED);
    }

    @PatchMapping("/{userId}")
    @PreAuthorize("#userId == authentication.principal.userId or hasRole('ADMIN')")
    public ResponseEntity<userDTO> updateUser(@Valid @RequestBody userDTO userDto, @PathVariable("userId") Long userId){
        userDTO updatedUser = userServices.updateUser(userDto,userId);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("#userId == authentication.principal.userId or hasRole('ADMIN')")
    public ResponseEntity<userDTO> deleteUser(@PathVariable("userId") Long userId){
        fileStorageService.deleteUploadedImage(userId,"users");
        userDTO deletedUser = userServices.deleteUser(userId);
        return new ResponseEntity<>(deletedUser, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<PaginatedResponseDTO<userDTO>> getAllUsers(
            @RequestParam(value="pageNum", defaultValue = "0", required = false ) int pageNum,
            @RequestParam(value="pageSize", defaultValue = "3", required = false ) int pageSize,
            @RequestParam(value="sortBy", defaultValue = "name", required = false ) String sortBy,
            @RequestParam(value="sortDir", defaultValue = "asc", required = false ) String sortDir
    ){
        return new ResponseEntity<>(userServices.getAllUsers(pageNum, pageSize, sortBy, sortDir), HttpStatus.OK);
    }

 
    @GetMapping("/id/{userId}")
    @PreAuthorize("#userId == authentication.principal.userId or hasRole('ADMIN')")
    public ResponseEntity<userDTO> getUserById(@PathVariable("userId") Long userId){
        userDTO user = userServices.getUserById(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/email/{email}")
    @PreAuthorize("#email == authentication.principal.email or hasRole('ADMIN')")
    public ResponseEntity<userDTO> getUserByEmail(@PathVariable("email") String email){
        userDTO user = userServices.getUserByEmail(email);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<userDTO>> getSearched (@PathVariable("keyword") String keyword){
        return new ResponseEntity<>(userServices.searchUser(keyword), HttpStatus.OK);
    }

    @PostMapping(value="/uploadProfile/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("#userId == authentication.principal.userId")
    public ResponseEntity<String> UploadImage(@RequestPart("file") MultipartFile file, @PathVariable("userId") Long userId) {
        try{
            String filename = fileStorageService.uploadFile(file,"users");
            userServices.updateProfileImage(filename, userId);
            return ResponseEntity.ok("Image uploaded: " + filename);
        }catch (Exception e) {
            e.printStackTrace();  // or use logger
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload failed: " + e.getMessage());
        }
    }

    @GetMapping("/getProfileImage/{userId}")
    public ResponseEntity<Resource> getProfileImage (@PathVariable("userId") Long userId) throws IOException {
        return fileStorageService.getUploadedImage(userId, "users");
    }
}
