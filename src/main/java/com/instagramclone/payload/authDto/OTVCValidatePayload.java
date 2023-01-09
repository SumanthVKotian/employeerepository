package com.instagramclone.payload.authDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OTVCValidatePayload {

    private String emailOrPhoneNumber;
    private String otvc;
}
