package com.instagramclone.payload.authDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OTVCDto {

    private String emailOrPhoneNumber;
    private String oneTimeVerificationCode;

}
