package com.instagramclone.payload.authDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class JwtAuthResponse {
    private String token;
    private UserSignInResponseDto user;

    public JwtAuthResponse(String token) {
        this.token = token;
    }
}
