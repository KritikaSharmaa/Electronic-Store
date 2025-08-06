package com.electronic.store.Electronic_Store.DTOs;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
    private  String JwtToken;
    private userDTO userDto;
    private RefreshTokenDTO refreshToken;
}
