package com.electronic.store.Electronic_Store.Services.Implementations;

import com.electronic.store.Electronic_Store.DTOs.PaginatedResponseDTO;
import com.electronic.store.Electronic_Store.DTOs.userDTO;
import com.electronic.store.Electronic_Store.Entities.Role;
import com.electronic.store.Electronic_Store.Entities.User;
import com.electronic.store.Electronic_Store.ExceptionHandler.CustomExceptions.UserNotFoundException;
import com.electronic.store.Electronic_Store.Repositories.RoleRepository;
import com.electronic.store.Electronic_Store.Repositories.userRepository;
import com.electronic.store.Electronic_Store.Services.userServices;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class userServiceImplementation implements userServices {

    @Autowired
    private userRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    //CREATE USER IMPLEMENTATION
    @Override
    public userDTO createUser(userDTO userDto) {
        //DTO -> ENTITY
        User user = DtoToEntity(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Encrypt the password
        List<Role> userRoles = userDto.getRoles().stream()
                .map(roleName -> roleRepository.findByRole(roleName)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleName)))
                .collect(Collectors.toList());

        user.setRoles(userRoles); // Set roles for user
        User savedUser = userRepository.save(user); //But save needs Entity not class -->userDTO needs to be converted into Entity
       //ENTITY -> DTO
        return EntityToDto(savedUser);
    }

    //UPDATE USER IMPLEMENTATION
    @Override
    public userDTO updateUser(userDTO userDto, Long userId) {
        // 1. Fetch the existing user
        User user = userRepository.findById(userId).orElseThrow(()->new UserNotFoundException("Cannot update : invalid userId "+userId));

        // 2. Manually copy only the allowed updatable fields
        if (userDto.getName() != null) user.setName(userDto.getName());
        if (userDto.getEmail() != null) user.setEmail(userDto.getEmail());
        if (userDto.getPassword() != null) user.setPassword(userDto.getPassword());
        if (userDto.getGender() != null) user.setGender(userDto.getGender());
        if (userDto.getImage() != null) user.setImage(userDto.getImage());

        // 3. Save and return
        User updatedUser = userRepository.save(user);

        return EntityToDto(updatedUser);
    }

    //DELETE USER IMPLEMENTATION
    @Override
    public userDTO deleteUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException("Cannot delete : invalid user id "+userId));
        userRepository.deleteById(userId);
        return EntityToDto(user);
    }

    //GET ALL USER IMPLEMENTATION
    @Override
    public PaginatedResponseDTO<userDTO> getAllUsers(int pageNum, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);

        Page<User> page = userRepository.findAll(pageable);
        List<User> users = page.getContent();
        //return users.stream().map(user-> EntityToDto(user)).collect(Collectors.toList());
        List<userDTO> userDto =  users.stream().map(this::EntityToDto).toList(); //Method Reference
        return new PaginatedResponseDTO<>(
                userDto,
                pageNum,
                pageSize,
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }

    //GET SINGLE USER BY ID IMPLEMENTATION
    @Override
    public userDTO getUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()->new UserNotFoundException("User with id: "+userId+" not found..!!"));
        return EntityToDto(user);
    }

    //GET SINGLE USER BY EMAIL IMPLEMENTATION
    @Override
    public userDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(()->new UserNotFoundException("User with email id: "+ email+" not found..!!"));
        return EntityToDto(user);
    }

    //SEARCH USER IMPLEMENTATION
    @Override
    public List<userDTO> searchUser(String keyword) {
        List<User> searcheduser = userRepository.findByNameContaining(keyword).orElseThrow(()->new UserNotFoundException("Not User Found"));
        return searcheduser.stream().map((user)-> EntityToDto(user)).collect(Collectors.toList());
    }

    @Override
    public void updateProfileImage(String filename, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException("User not found with id: "+userId));
        user.setImage(filename);
        userRepository.save(user);
    }

    private User DtoToEntity(userDTO userDto){
//        User user = User.builder()
//                .userId(userDto.getUserId())
//                .name(userDto.getName())
//                .email(userDto.getEmail())
//                .password(userDto.getPassword())
//                .image(userDto.getImage())
//                .gender(userDto.getGender()).build();
//       return user;
         return modelMapper.map(userDto, User.class);
    }

    private userDTO EntityToDto(User user){
//        userDTO userDto = userDTO.builder()
//                .userId(user.getUserId())
//                .name(user.getName())
//                .email(user.getEmail())
//                .password(user.getPassword())
//                .image(user.getImage())
//                .gender(user.getGender())
//                .build();
//        return userDto;
        userDTO dto =  modelMapper.map(user, userDTO.class);
        List<String> roleNames = user.getRoles()
                .stream()
                .map(Role::getRole)
                .toList();

        dto.setRoles(roleNames);
        return dto;
    }

}
