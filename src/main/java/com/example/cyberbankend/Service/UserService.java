package com.example.cyberbankend.Service.Implementation;

import com.example.cyberbankend.Dto.Request.ResponseDto;
import com.example.cyberbankend.Dto.Response.RequestDto;

public interface UserService {
    public ResponseDto SignUp(RequestDto requestDto);
}
