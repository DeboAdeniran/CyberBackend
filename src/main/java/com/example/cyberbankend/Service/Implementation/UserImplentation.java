package com.example.cyberbankend.Service.Implementation;

import com.example.cyberbankend.Dto.Response.LoginResponse;
import com.example.cyberbankend.Dto.Request.ResponseDto;
import com.example.cyberbankend.Dto.Request.LoginRequest;
import com.example.cyberbankend.Dto.Response.RequestDto;
import com.example.cyberbankend.Model.Users;
import com.example.cyberbankend.Repository.UserRepository;
import com.example.cyberbankend.Service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserImplentation implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public UserImplentation(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    public ResponseDto SignUp(RequestDto requestDto) {
        if (userRepository.existsByEmail(requestDto.getEmail())  ){
            ResponseDto responseDto = new ResponseDto();
            responseDto.setMessage("Email already in use");
            return responseDto;
        } else if (userRepository.existsByPhoneNumber(requestDto.getPhoneNumber())) {
            ResponseDto responseDto = new ResponseDto();
            responseDto.setMessage("Phone number already in use");
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

    @Override
    public LoginResponse Login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getLoginIdentifier(), loginRequest.getPassword())
        );
        if (authentication.isAuthenticated()){
            Optional<Users> user = userRepository.findByEmail(loginRequest.getLoginIdentifier());
            if (user.isEmpty()){
                user = userRepository.findByPhoneNumber(loginRequest.getLoginIdentifier());
            }

            Users foundUser = user.orElseThrow(() -> new UsernameNotFoundException("User not found"));
            String jwtToken = jwtService.generateToken(loginRequest);
            return new LoginResponse(foundUser.getEmail(), foundUser.getPhoneNumber(), jwtToken);
        }else {
            throw new UsernameNotFoundException("Invalid Email or password");
        }
    }
}
