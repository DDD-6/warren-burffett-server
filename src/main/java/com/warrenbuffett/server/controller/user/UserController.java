package com.warrenbuffett.server.controller.user;
import com.warrenbuffett.server.common.ErrorResponse;
import com.warrenbuffett.server.controller.dto.*;
import com.warrenbuffett.server.domain.User;
import com.warrenbuffett.server.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*" , exposedHeaders = "**")
@RestController
@RequestMapping(value="/api/user")
public class UserController {
    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getMypageInfo() {
        User user = userService.getMyInfo();
        if(user==null) { return new ResponseEntity(HttpStatus.BAD_REQUEST); }
        return ResponseEntity.ok(new UserResponseDto(user));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> searchUser(@PathVariable final Long userId) {
        User user = userService.getUserInfo(userId);
        if(user!=null) { return ResponseEntity.ok(new UserResponseDto(user)); }
        else return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping
    public ResponseEntity<?> createUSer(@Validated @RequestBody final UserCreateRequestDto userCreateRequestDto, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(e -> e.getDefaultMessage()).collect(Collectors.toList());
            // 200 response with 404 status code
            //return ResponseEntity.ok(new ErrorResponse("404", "Validation failure", errors));
            // 404 request
            return ResponseEntity.badRequest().body(new ErrorResponse("404", "Validation failure", errors));
        }
        final User user = userService.searchUserByEmail(userCreateRequestDto.toEntity().getEmail());
        if(user==null) {
            return new ResponseEntity(
                    new UserResponseDto(userService.createUser(userCreateRequestDto.toEntity())),HttpStatus.CREATED
            );
        }
        return ResponseEntity.ok(new UserResponseDto(user));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        boolean status = userService.deleteUser(userId);
        if (status==true) return new ResponseEntity( HttpStatus.OK);
        else return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequestDto loginRequestDto){
        TokenDto loginResponseDto = userService.loginUser(loginRequestDto);
        if (loginResponseDto!=null) return ResponseEntity.ok(loginResponseDto);
        else return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        return ResponseEntity.ok(userService.reissue(tokenRequestDto));
    }
}