package com.electronic.store.Electronic_Store.Services;

import com.electronic.store.Electronic_Store.DTOs.RefreshTokenDTO;
import com.electronic.store.Electronic_Store.DTOs.userDTO;
import com.electronic.store.Electronic_Store.Entities.RefreshToken;

public interface RefreshTokenService {
    //Generate Refresh Token
    RefreshTokenDTO generateRefreshToken(String username);

    //Find by token
    RefreshTokenDTO findByToken(String token);

    //Verify
    RefreshTokenDTO verifyToken(RefreshTokenDTO token);

    userDTO getUser(RefreshTokenDTO refreshTokenDTO);
}
