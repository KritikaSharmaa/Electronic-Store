package com.electronic.store.Electronic_Store.DTOs;

import com.electronic.store.Electronic_Store.Entities.User;
import lombok.*;

import java.time.Instant;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenDTO {
    private Long id;
    private  String token;
    private Instant expiryDate;
}

