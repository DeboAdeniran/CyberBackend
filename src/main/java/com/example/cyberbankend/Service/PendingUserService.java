package com.example.cyberbankend.Service;

import com.example.cyberbankend.Dto.Response.RequestDto;
import com.example.cyberbankend.Dto.Response.VerifyOtpDto;
import com.example.cyberbankend.Model.PendingUser;
import org.springframework.cache.annotation.CachePut;

import java.util.Optional;

public interface PendingUserService {
    public VerifyOtpDto SendOTP(RequestDto requestDto);
    Optional<PendingUser> getUserByEmail(String email);

//    public boolean doesEmailExit(String email);
}
