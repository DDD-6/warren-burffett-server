package com.warrenbuffett.server.controller.user;

import com.warrenbuffett.server.controller.dto.*;
import com.warrenbuffett.server.domain.User;
import com.warrenbuffett.server.repository.UserRepository;
import com.warrenbuffett.server.service.UserPasswordResetVerifyEmailService;
import com.warrenbuffett.server.service.UserService;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*" , exposedHeaders = "**")
@RestController
@RequiredArgsConstructor
@RequestMapping(value="/api/user/password")
public class UserPasswordResetController {
    private final UserPasswordResetVerifyEmailService userPasswordResetVerifyEmailService;
    private final UserRepository userRepository;
    private final UserService userService;

    // password reset mail
    @GetMapping("/verify")
    public ResponseEntity verify(@RequestParam String email) throws NotFoundException {
        User user = userRepository.findByEmail(email).orElse(null);
        if(user!=null) {
            userPasswordResetVerifyEmailService.sendVerificationMail(user.getEmail());
            return new ResponseEntity(HttpStatus.OK);
        }
        else return new ResponseEntity(HttpStatus.NO_CONTENT);

    }

    @GetMapping("/verify/{key}")
    public ResponseEntity getVerify(@PathVariable String key) throws NotFoundException {
        try {
            userPasswordResetVerifyEmailService.verifyEmail(key);
            return new ResponseEntity(HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
    // password reset -> email은 hidden input으로?
    @PostMapping("/")
    public ResponseEntity resetPassword(@RequestBody PasswordResetRequestDto requestDto) {
        try{
            userService.resetUserPassword(requestDto);
            return new ResponseEntity(HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }


    }
}
