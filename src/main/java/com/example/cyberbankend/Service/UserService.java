package com.example.cyberbankend.Service;

import com.example.cyberbankend.Dto.Request.ResponseDto;
import com.example.cyberbankend.Dto.Response.RequestDto;

public interface UserService {
    public ResponseDto SignUp(RequestDto requestDto);
}
