package com.warrenbuffett.server.controller.user;
import com.warrenbuffett.server.common.ErrorResponse;
import com.warrenbuffett.server.domain.User;
import com.warrenbuffett.server.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value="/api/user")
public class UserController {
    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> searchUser(@PathVariable final Long userId) {
        User user = userService.searchUser(userId);
        if(user!=null) {
            return ResponseEntity.ok(
                    new UserResponseDto(userService.searchUser(userId))
            );
        }
        else return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping
    public ResponseEntity<?> createUSer(@Validated @RequestBody final UserCreateRequestDto userCreateRequestDto, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(e -> e.getDefaultMessage()).collect(Collectors.toList());
            // 200 response with 404 status code
            return ResponseEntity.ok(new ErrorResponse("404", "Validation failure", errors));
            // 404 request
//             return ResponseEntity.badRequest().body(new ErrorResponse("404", "Validation failure", errors));
        }
        try {
            final User user = userService.searchUserByEmail(userCreateRequestDto.toEntity().getEmail());
        }catch (Exception e){
            return ResponseEntity.ok(
                    new UserResponseDto(userService.createUser(userCreateRequestDto.toEntity()))
            );
        }
        // user already exist
        return ResponseEntity.ok(
                new UserResponseDto(userService.searchUserByEmail(userCreateRequestDto.toEntity().getEmail()))
        );

    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long userId) {
        boolean status = userService.deleteUser(userId);
        if (status==true) return new ResponseEntity( HttpStatus.OK);
        else return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}