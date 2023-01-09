package com.instagramclone.payload.authDto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
@JsonPropertyOrder({"username","fullName","profilePhoto"})
public class UserSignInResponseDto {
    private String username;
    private String fullName;
    private String profilePhoto;
}
