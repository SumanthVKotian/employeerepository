package com.instagramclone.service.impl;

import com.instagramclone.entity.user.Role;
import com.instagramclone.entity.user.User;
import com.instagramclone.exception.APIException;
import com.instagramclone.exception.ResourceNotFoundException;
import com.instagramclone.exception.UnexpectedException;
import com.instagramclone.payload.authDto.JwtAuthResponse;
import com.instagramclone.payload.authDto.SignInDto;
import com.instagramclone.payload.authDto.SignUpDto;
import com.instagramclone.payload.authDto.UserSignInResponseDto;
import com.instagramclone.respository.user.RoleRepository;
import com.instagramclone.respository.user.UserRepository;
import com.instagramclone.security.JwtTokenProvider;
import com.instagramclone.service.AuthService;
import com.instagramclone.utils.ErrorConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public JwtAuthResponse signIn(SignInDto signInDto) {
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(signInDto.getPhoneNumberOrEmailOrUsername(),
                            signInDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwtToken = jwtTokenProvider.generateToken(authentication);

            String userNameOrEmailOrPhoneNumber = signInDto.getPhoneNumberOrEmailOrUsername();

            User loggedInUser = userRepository.findByUsernameOrEmailOrPhoneNumber(userNameOrEmailOrPhoneNumber,
                    userNameOrEmailOrPhoneNumber, userNameOrEmailOrPhoneNumber).orElseThrow(
                    () -> new ResourceNotFoundException("username", "username/email/phoneNumber", userNameOrEmailOrPhoneNumber)
            );

            JwtAuthResponse jwtAuthResponse = new JwtAuthResponse(jwtToken);
            UserSignInResponseDto userSignInResponseDto = new UserSignInResponseDto();

            userSignInResponseDto.setUsername(loggedInUser.getUsername());
            userSignInResponseDto.setFullName(loggedInUser.getFullName());
            userSignInResponseDto.setProfilePhoto(loggedInUser.getCurrentProfilePicUrl());

            jwtAuthResponse.setUser(userSignInResponseDto);
            return jwtAuthResponse;

        }catch (Exception ex) {
            throw new APIException(HttpStatus.UNAUTHORIZED,"Invalid Credentials "+ex);
        }
    }

    @Override
    public Boolean register(SignUpDto signUpDto) {
       try{
           User user = new User();
           Role role = new Role();
           role.setRoleType("ROLE_USER");
           roleRepository.save(role);

           user.setFullName(signUpDto.getFullName());
           user.setUsername(signUpDto.getUserName());
           user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
           user.setBirthDate(signUpDto.getBirthDay());

           if(signUpDto.getEmailOrPhoneNumber().contains("@")){
               user.setEmail(signUpDto.getEmailOrPhoneNumber());
           }else{
               user.setPhoneNumber(signUpDto.getEmailOrPhoneNumber());
           }
//                Role roles = roleRepository.findByRoleType("ROLE_USER").get();
//                user.setRoles(Collections.singleton(roles));
           userRepository.save(user);
           return true;
       } catch (Exception ex) {
           throw new UnexpectedException("Failed to register the user "+ex, ErrorConstants.USER_REGISTRATION_FAILED_ERROR_CODE);
       }
    }
}
