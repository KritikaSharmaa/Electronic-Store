package com.electronic.store.Electronic_Store.Services.Implementations;

import com.electronic.store.Electronic_Store.DTOs.RefreshTokenDTO;
import com.electronic.store.Electronic_Store.DTOs.userDTO;
import com.electronic.store.Electronic_Store.Entities.RefreshToken;
import com.electronic.store.Electronic_Store.Entities.User;
import com.electronic.store.Electronic_Store.ExceptionHandler.CustomExceptions.ResourceNotFoundException;
import com.electronic.store.Electronic_Store.Repositories.RefreshTokenRepository;
import com.electronic.store.Electronic_Store.Repositories.userRepository;
import com.electronic.store.Electronic_Store.Services.RefreshTokenService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Ref;
import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenServiceImplementation implements RefreshTokenService {

    @Autowired
    userRepository userRepository;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public RefreshTokenDTO generateRefreshToken(String username) {
        User user = userRepository.findByEmail(username).orElseThrow(()-> new ResourceNotFoundException("User not found"));
        RefreshToken ref_token = refreshTokenRepository.findByUser(user).orElse(null);
        if(ref_token==null){
            ref_token = RefreshToken
                    .builder()
                    .user(user)
                    .token(UUID.randomUUID().toString())
                    .expiryDate(Instant.now().plusSeconds(7*24*60*60))
                    .build();
        }else{
            ref_token.setToken(UUID.randomUUID().toString());
            ref_token.setExpiryDate(Instant.now().plusSeconds(7*24*60*60));
        }
        refreshTokenRepository.save(ref_token);
        return modelMapper.map(ref_token, RefreshTokenDTO.class);
    }

    @Override
    public RefreshTokenDTO findByToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token).orElseThrow(()->new ResourceNotFoundException("Refresh Token Not Found"));
        return modelMapper.map(refreshToken, RefreshTokenDTO.class);
    }

    @Override
    public RefreshTokenDTO verifyToken(RefreshTokenDTO token) {
        RefreshToken refreshToken = modelMapper.map(token, RefreshToken.class);
        if(token.getExpiryDate().compareTo(Instant.now())<0){
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException("Refresh Token Expired !!");
        }
        return modelMapper.map(refreshToken, RefreshTokenDTO.class);
    }

    @Override
    public userDTO getUser(RefreshTokenDTO refreshTokenDTO) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenDTO.getToken()).orElseThrow(()-> new ResourceNotFoundException("Refresh Token not Found"));
        User user = refreshToken.getUser();
        return modelMapper.map(user, userDTO.class);
    }
}
