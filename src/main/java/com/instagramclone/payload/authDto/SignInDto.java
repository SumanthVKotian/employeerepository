package com.instagramclone.payload.authDto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import java.util.Map;

@Data
@Getter
@Setter
@NoArgsConstructor
public class SignInDto {
    private String phoneNumberOrEmailOrUsername;
    private String password;
    private Map<String,String> requestParameters;
}
