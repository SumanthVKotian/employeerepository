package com.instagramclone.controller;

import com.instagramclone.config.SignInValidator;
import com.instagramclone.customAnnotation.ValidateSignInRq;
import com.instagramclone.payload.authDto.*;
import com.instagramclone.service.AuthService;
import com.instagramclone.utils.OTVCUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private OTVCUtils otvcUtils;

    @PostMapping("/otvc")
    public ResponseEntity<Boolean> generateOTVC(@RequestBody OTVCGeneratePayload otvcGeneratePayload) {

        if(otvcUtils.generateOtvcAndSendEmail(otvcGeneratePayload.getEmailOrPhoneNumber())) {
            return new ResponseEntity<>(true,HttpStatus.OK);
        }
        return new ResponseEntity<Boolean>(false, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/validate-otvc")
    public ResponseEntity<Boolean> validateOtvc(@RequestBody OTVCValidatePayload otvcValidatePayload) {

        if(otvcUtils.validateOtvc(otvcValidatePayload.getEmailOrPhoneNumber(),otvcValidatePayload.getOtvc())) {
            return new ResponseEntity<>(true,HttpStatus.OK);
        }
        return new ResponseEntity<>(false,HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/check-existing-data/{fieldName}")
    public ResponseEntity<Map> checkExistingData(@PathVariable String fieldName) {
        Map<String,Boolean> hasMapExists = new HashMap<>();
        if(otvcUtils.checkForExistingField(fieldName)){
            hasMapExists.put("exists",true);
            return new ResponseEntity<>(hasMapExists,HttpStatus.OK);
        }
        hasMapExists.put("exists",false);
       return new ResponseEntity<>(hasMapExists,HttpStatus.OK);
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthResponse> signIn (@RequestBody SignInDto signInDto) {
        //SignInValidator.validateSignInRq(signInDto);
        return new ResponseEntity<>(authService.signIn(signInDto),HttpStatus.OK);
      }

    @PostMapping("/register")
    public ResponseEntity<Boolean> registerUser(@RequestBody @NotNull SignUpDto signUpDto) {
        return new ResponseEntity<>(authService.register(signUpDto),HttpStatus.CREATED);
    }

}
