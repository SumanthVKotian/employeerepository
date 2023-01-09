package com.instagramclone.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.Singleton;
import com.cloudinary.utils.ObjectUtils;
import com.instagramclone.entity.user.User;
import com.instagramclone.exception.ResourceNotFoundException;
import com.instagramclone.exception.UnexpectedException;
import com.instagramclone.payload.userDto.UserSuggestionsDto;
import com.instagramclone.respository.user.UserRepository;
import com.instagramclone.service.UserService;
import com.instagramclone.utils.ErrorConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    private final Cloudinary cloudinary = Singleton.getCloudinary();

    @Autowired
    UserRepository userRepository;

    @Override
    public List<UserSuggestionsDto> getUserSuggestions(int pageNo,int pageSize,String sortDir) {
       try{
           Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
           String loggedInUserName = authentication.getName();
           Pageable pageable = PageRequest.of(pageNo,pageSize);
           return userRepository.getUserSuggestions(loggedInUserName,pageable);
       } catch (Exception ex) {
           throw new UnexpectedException("Unexpected exception occurred, failed to fetch data. " + ex, ErrorConstants.UNEXCPECTED_ERROR_CODE);
       }
    }

    @Override
    public Map<String,String> uploadUserProfile(MultipartFile media) {
        Map<String, String> response = new HashMap<>();
        String public_url="";
        String public_id="";

        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String loggedInUserName = authentication.getName();

            User user = userRepository.findByUsername(loggedInUserName).orElseThrow(
                    ()-> new ResourceNotFoundException("user","username",loggedInUserName));

            Map uploadResult = cloudinary.uploader().upload(media.getBytes(),ObjectUtils.emptyMap());

            public_url = uploadResult.get("url").toString();
            public_id = uploadResult.get("public_id").toString();

            // Check if the user has default profile or not, if exists, remove the existing profile from cloud
            if(user.getHasProfile()) {
               if(!removeProfileFromCloudinary(user.getProfileId())) {
                   throw new UnexpectedException("Failed to delete the profile photo form cloud", ErrorConstants.UNEXCPECTED_ERROR_CODE);
               };
            }
            user.setCurrentProfilePicUrl(public_url);
            user.setProfileId(public_id);
            if(!user.getHasProfile()) {
                user.setHasProfile(true);
            }
            userRepository.save(user);

            response.put("public_url",public_url);
            response.put("status","ok");

            return response;
        }catch (Exception ex) {
            throw new RuntimeException("failed to upload, "+ex );
        }
    }

    private boolean removeProfileFromCloudinary(String public_id) throws IOException {
        Map result = cloudinary.uploader().destroy(public_id,ObjectUtils.emptyMap());
        return (result.get("result").toString().equalsIgnoreCase("ok") || result.get("result").toString().equalsIgnoreCase("not found"));
    }
}
