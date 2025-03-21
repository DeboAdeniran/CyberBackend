package com.example.cyberbankend.Service.Implementation;

import com.example.cyberbankend.Dto.Request.EmailDetails;
import com.example.cyberbankend.Dto.Response.LoginResponse;
import com.example.cyberbankend.Dto.Request.ResponseDto;
import com.example.cyberbankend.Dto.Request.LoginRequest;
import com.example.cyberbankend.Dto.Response.RequestDto;
import com.example.cyberbankend.Dto.Response.VerifyOtpDto;
import com.example.cyberbankend.Model.PendingUser;
import com.example.cyberbankend.Model.Users;
import com.example.cyberbankend.Repository.PendingUserRepository;
import com.example.cyberbankend.Repository.UserRepository;
import com.example.cyberbankend.Service.EmailService;
import com.example.cyberbankend.Service.PendingUserService;
import com.example.cyberbankend.Service.UserService;
import com.example.cyberbankend.Util.GenerateOtp;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserImplementation implements UserService {

    private final UserRepository userRepository;
    private final PendingUserRepository pendingUserRepository;
    private final PendingUserService pendingUserService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final EmailService emailService;
    private final RedisTemplate redisTemplate;

    public UserImplementation(UserRepository userRepository, PendingUserRepository pendingUserRepository, PendingUserService pendingUserService, BCryptPasswordEncoder bCryptPasswordEncoder, AuthenticationManager authenticationManager, JwtService jwtService, EmailService emailService, RedisTemplate redisTemplate) {
        this.userRepository = userRepository;
        this.pendingUserRepository = pendingUserRepository;
        this.pendingUserService = pendingUserService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.emailService = emailService;
        this.redisTemplate = redisTemplate;
    }


    @Override
    public ResponseDto SignUp(RequestDto requestDto) {
        String userID = (String) redisTemplate.opsForValue().get("email:" + requestDto.getEmail());
        String storedOtp = (String) redisTemplate.opsForValue().get("otp:" + requestDto.getEmail());

        System.out.println("Retrieved User ID from Redis: " + userID);
        System.out.println("Retrieved OTP from Redis: " + storedOtp);

        if (userID == null || !userID.matches("-?\\d+")) {
            throw new RuntimeException("User not found in Redis");
        }

        // Retrieve pending user from database
        Optional<PendingUser> optionalPendingUser = pendingUserService.getUserByEmail(requestDto.getEmail());

        if (optionalPendingUser.isEmpty()) {
            throw new RuntimeException("User not found in database");
        }

        PendingUser pendingUser = optionalPendingUser.get();

        // Verify OTP (first check Redis, then fallback to DB)
        if (storedOtp == null || !storedOtp.equals(requestDto.getOtp())) {
            ResponseDto responseDto = new ResponseDto();
            responseDto.setMessage("Invalid OTP. Please try again.");
            return responseDto;
        }

        // Check OTP expiry from database
        if (pendingUser.getOtpExpiry().isBefore(LocalDateTime.now())) {
            pendingUserRepository.delete(pendingUser);
            redisTemplate.delete("email:" + requestDto.getEmail());
            redisTemplate.delete("otp:" + requestDto.getEmail()); // Clean up expired OTP

            ResponseDto responseDto = new ResponseDto();
            responseDto.setMessage("OTP expired. Please request a new one.");
            return responseDto;
        }

        // Proceed with user registration
        Users users = new Users();
        users.setName(requestDto.getName());
        users.setEmail(requestDto.getEmail());
        users.setPhoneNumber(requestDto.getPhoneNumber());
        users.setPassword(bCryptPasswordEncoder.encode(requestDto.getPassword()));

        userRepository.save(users); // Save the new user

        // Clean up Redis & PendingUser since the user has been successfully registered
        pendingUserRepository.delete(pendingUser);
        redisTemplate.delete("email:" + requestDto.getEmail());
        redisTemplate.delete("otp:" + requestDto.getEmail());

        return new ResponseDto(users.getEmail(), users.getPhoneNumber(), "200 OK");
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
