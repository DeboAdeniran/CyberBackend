package com.example.cyberbankend.Service.Implementation;

import com.example.cyberbankend.Dto.Request.ResponseDto;
import com.example.cyberbankend.Dto.Response.RequestDto;
import com.example.cyberbankend.Model.Users;
import com.example.cyberbankend.Repository.UserRepository;
import com.example.cyberbankend.Service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserImplentation implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserImplentation(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public ResponseDto SignUp(RequestDto requestDto) {
        if (userRepository.existsByEmail(requestDto.getEmail())){
            ResponseDto responseDto = new ResponseDto();
            responseDto.setMessage("Email already in use");
            return responseDto;
        }

        Users users = new Users();
        users.setName(requestDto.getName());
        users.setEmail(requestDto.getEmail());
        users.setPhoneNumber(requestDto.getPhoneNumber());
        users.setPassword(bCryptPasswordEncoder.encode(requestDto.getPassword()));
        Users newUser = userRepository.save(users);
        return new ResponseDto(users.getEmail(),users.getPhoneNumber(),"200 OK");
    }
}
