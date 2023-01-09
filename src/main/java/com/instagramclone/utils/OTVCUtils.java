package com.instagramclone.utils;

import com.instagramclone.entity.auth.OTVC;
import com.instagramclone.exception.APIException;
import com.instagramclone.respository.auth.OtvcRepository;
import com.instagramclone.respository.user.UserRepository;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Date;

@Component
public class OTVCUtils {
    @Autowired
    private OtvcRepository otvcRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    private static final long OTP_VALID_DURATION = 5 * 60 * 1000;   // 5 minutes


    private PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public boolean generateOtvcAndSendEmail(String email) {
        return sendEmail(email, generateOtvc(email));
    }

    public boolean validateOtvc(String email, String otvc) {
        OTVC foundUser = otvcRepository.findByUserEmail(email).orElseThrow(()->
                new APIException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "User not found for OTVC validation"));

        String foundUserOtvc = foundUser.getOneTimeCode();
        long foundUserOtvcExpiryTime = foundUser.getExpiryTime().getTime();

        return foundUserOtvc.equals(otvc) && validateExpiry(foundUserOtvcExpiryTime, email);
    }

    private boolean validateExpiry(long otpRequestedTimeInMillis, String email) {

        long currentTimeInMillis = System.currentTimeMillis();

        if(otpRequestedTimeInMillis + OTP_VALID_DURATION > currentTimeInMillis) {
          OTVC otvcUser = otvcRepository.findByUserEmail(email).orElseThrow(()->new APIException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to find user while removing user fom OTVC table"));
            otvcRepository.delete(otvcUser);
            return true;
        } else {
            return false;
        }
    }

    private String generateOtvc(String email) {
        String OtvcGenerated = RandomString.make(8);
        boolean existingOtvcUser = otvcRepository.existsByUserEmail(email);

        if(existingOtvcUser) {
            OTVC foundOtvcUser = otvcRepository.findByUserEmail(email).orElseThrow(()->
                    new APIException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "User not found for OTVC"));

            foundOtvcUser.setExpiryTime(new Date());
            foundOtvcUser.setOneTimeCode(OtvcGenerated);
            otvcRepository.save(foundOtvcUser);

        }else {
            OTVC otvc = new OTVC();

            otvc.setUserEmail(email);
            otvc.setOneTimeCode(OtvcGenerated);
            otvc.setExpiryTime(new Date());

            otvcRepository.save(otvc);
        }

        return OtvcGenerated;
    }

    private boolean sendEmail(String email, String otvc) {

        MimeMessage msg = javaMailSender.createMimeMessage();
        try{
            MimeMessageHelper helper = new MimeMessageHelper(msg, true);
            String subject = "Here's your One Time Verification Code (OTVC) - Expire in 5 minutes!";

            String content = "<p>Hello user, </p>"
                    + "<p>For security reason, you're required to use the following "
                    + "One Time Verification Code to complete the registration:</p>"
                    + "<h2><b>" + otvc + "</b></h2>"
                    + "<br>"
                    + "<p>Note: this OTVC is set to expire in 5 minutes.</p>";

            helper.setFrom("No.Reply19965@gmail.com","Instagram Registration");
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(content,true);
            javaMailSender.send(msg);
            return true;
        } catch (MessagingException | UnsupportedEncodingException messagingException) {
            throw new APIException(HttpStatus.INTERNAL_SERVER_ERROR,messagingException.getMessage());
        }
    }

    public boolean checkForExistingField(String usernameOrEmailOrPhoneNumber) {
        return userRepository.existsByUsernameOrEmailOrPhoneNumber(usernameOrEmailOrPhoneNumber,
                usernameOrEmailOrPhoneNumber,usernameOrEmailOrPhoneNumber);
    }
}
