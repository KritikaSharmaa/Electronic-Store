package com.electronic.store.Electronic_Store.Services;

import com.electronic.store.Electronic_Store.DTOs.PaginatedResponseDTO;
import com.electronic.store.Electronic_Store.DTOs.userDTO;
import java.util.List;

public interface userServices {
    //create user
    userDTO createUser(userDTO userDto);

    //update user
    userDTO updateUser(userDTO userDto, Long userId);

    //delete user
    userDTO deleteUser(Long userId);

    //get all users
    PaginatedResponseDTO<userDTO> getAllUsers(int pageNum, int pageSize, String sortBy, String sortDir);

    //get single user by id
    userDTO getUserById(Long userId);

    //get single user by email
    userDTO getUserByEmail(String email);

    //search user
    List<userDTO> searchUser(String keyword);

    //update product Image
    void updateProfileImage(String filename, Long userId);
}
