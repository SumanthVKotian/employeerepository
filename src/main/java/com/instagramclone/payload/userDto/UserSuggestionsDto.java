package com.instagramclone.payload.userDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserSuggestionsDto {
    private Long id;
    private String fullName;
    private String profilePhoto;
}
