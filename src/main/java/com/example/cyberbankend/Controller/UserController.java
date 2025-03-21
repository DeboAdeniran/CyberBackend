package com.example.cyberbankend.Controller;

import com.example.cyberbankend.Dto.Response.LoginResponse;
import com.example.cyberbankend.Dto.Request.ResponseDto;
import com.example.cyberbankend.Dto.Request.LoginRequest;
import com.example.cyberbankend.Dto.Response.RequestDto;
import com.example.cyberbankend.Dto.Response.VerifyOtpDto;
import com.example.cyberbankend.Service.PendingUserService;
import com.example.cyberbankend.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;
    private final PendingUserService pendingUserService;

    public UserController(UserService userService, PendingUserService pendingUserService) {
        this.userService = userService;
        this.pendingUserService = pendingUserService;
    }

    @PostMapping("/SignUp")
    public ResponseDto SignUp(@RequestBody RequestDto requestDto){
        System.out.println(requestDto);
        return userService.SignUp(requestDto);
    }

    @PostMapping("/Login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest){
        LoginResponse loginResponse = userService
                .Login(loginRequest);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/verifyOTP")
    public VerifyOtpDto VerifyOTP(@RequestBody RequestDto requestDto){
        return pendingUserService.SendOTP(requestDto);
    }


}
