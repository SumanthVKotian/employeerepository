package com.instagramclone.controller;

import com.instagramclone.payload.userDto.UserSuggestionsDto;
import com.instagramclone.service.UserService;
import com.instagramclone.utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/suggestions")
    public ResponseEntity<List<UserSuggestionsDto>> userSuggestions(
            @RequestParam(value = "pageNo",defaultValue = AppConstants.DEFAULT_PAGE_NUMBER,required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE,required = false) int pageSize,
            @RequestParam(value = "sortDir",defaultValue = AppConstants.DEFAULT_SORT_DIRECTION,required = false) String sortDir
    ) {
        return new ResponseEntity<>(userService.getUserSuggestions(pageNo,pageSize,sortDir), HttpStatus.OK);
    }

    @PostMapping("/user-profile-upload")
    public ResponseEntity<Map<String,String>> uploadProfilePhoto(@RequestParam("media") MultipartFile media){
        return new ResponseEntity<>(userService.uploadUserProfile(media),HttpStatus.OK);
    }

}
