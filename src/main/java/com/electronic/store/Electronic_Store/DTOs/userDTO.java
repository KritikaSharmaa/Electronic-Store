package com.electronic.store.Electronic_Store.DTOs;

import com.electronic.store.Electronic_Store.Entities.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class userDTO {
    private Long userId;
    private String name;
    @Email
    private String email;
    @Size(min = 5)
    private String password;
    private String gender;
    private List<String> roles;
    private String image;
}
