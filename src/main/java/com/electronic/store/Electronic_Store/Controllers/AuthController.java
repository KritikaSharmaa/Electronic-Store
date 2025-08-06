package com.electronic.store.Electronic_Store.Controllers;
import com.electronic.store.Electronic_Store.DTOs.*;
import com.electronic.store.Electronic_Store.Entities.User;
import com.electronic.store.Electronic_Store.Repositories.RefreshTokenRepository;
import com.electronic.store.Electronic_Store.Security.JwtUtil;
import com.electronic.store.Electronic_Store.Services.Implementations.CustomSecurityDetailServiceImplementation;
import com.electronic.store.Electronic_Store.Services.RefreshTokenService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    CustomSecurityDetailServiceImplementation customSecurityDetailService;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> Login (@RequestBody JwtRequest request){
       try{
           Authentication authObj = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
           authenticationManager.authenticate(authObj);
           User user = (User)customSecurityDetailService.loadUserByUsername(request.getUsername());
           userDTO userDto = modelMapper.map(user, userDTO.class);

           //Generate Refresh Token
           RefreshTokenDTO refresh_token = refreshTokenService.generateRefreshToken(user.getUsername());

           //Generate JWT Token
           String token = jwtUtil.generateToken(user);

           JwtResponse jwtResponse = JwtResponse.builder().JwtToken(token).refreshToken(refresh_token).userDto(userDto).build();
           return ResponseEntity.ok(jwtResponse);

       }catch(BadCredentialsException ex){
           throw new BadCredentialsException("Invalid Username or Password !!");
       }
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<JwtResponse> reGenerateToken(@RequestBody RefreshTokenRequest request){ //refresh token as input
        RefreshTokenDTO refreshTokenDTO = refreshTokenService.findByToken(request.getRefreshToken());
        RefreshTokenDTO isVerifiedRefreshToken = refreshTokenService.verifyToken(refreshTokenDTO);
        userDTO user = refreshTokenService.getUser(refreshTokenDTO);
        String jwtToken = jwtUtil.generateToken(modelMapper.map(user, User.class));
        JwtResponse res = JwtResponse.builder()
                .JwtToken(jwtToken)
                .refreshToken(refreshTokenDTO)
                .userDto(user)
                .build();

        return ResponseEntity.ok(res);
    }
}
