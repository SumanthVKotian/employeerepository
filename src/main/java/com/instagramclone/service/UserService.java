package com.instagramclone.service;

import com.instagramclone.payload.userDto.UserSuggestionsDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface UserService {
    List<UserSuggestionsDto> getUserSuggestions(int pageNo,int pageSize,String sortDir);

    Map<String,String> uploadUserProfile(MultipartFile media);
}
