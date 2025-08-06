package com.electronic.store.Electronic_Store.DTOs;

import jakarta.persistence.Entity;
import lombok.*;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenRequest {
    private String refreshToken;
}
