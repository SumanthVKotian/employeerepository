package com.instagramclone.payload.authDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class SignUpDto {

    @NotEmpty
    private String fullName;

    @NotEmpty
    private String emailOrPhoneNumber;

    @NotEmpty
    @Size(min = 5,max = 20)
    private String userName;

    private String gender;

    @NotEmpty
    @Size(min = 6,max = 12)
    private String password;

    @NotEmpty
    private LocalDateTime birthDay;

}
