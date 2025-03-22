package com.example.cyberbankend.Service;

import com.example.cyberbankend.Dto.Response.LoginResponse;
import com.example.cyberbankend.Dto.Request.ResponseDto;
import com.example.cyberbankend.Dto.Request.LoginRequest;
import com.example.cyberbankend.Dto.Response.RequestDto;
import com.example.cyberbankend.Dto.Response.VerifyOtpDto;

public interface UserService {


    public ResponseDto SignUp(RequestDto requestDto);

    public LoginResponse Login(LoginRequest loginRequest);

}
