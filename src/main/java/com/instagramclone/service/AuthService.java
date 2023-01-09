package com.instagramclone.service;

import com.instagramclone.customAnnotation.ValidateSignInRq;
import com.instagramclone.payload.authDto.JwtAuthResponse;
import com.instagramclone.payload.authDto.SignInDto;
import com.instagramclone.payload.authDto.SignUpDto;
import org.springframework.validation.annotation.Validated;

@Validated
public interface AuthService {
    JwtAuthResponse signIn(@ValidateSignInRq SignInDto signInDto);
    Boolean register(SignUpDto signUpDto);
}
