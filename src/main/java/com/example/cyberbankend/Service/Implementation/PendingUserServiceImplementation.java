package com.example.cyberbankend.Service.Implementation;

import com.example.cyberbankend.Dto.Request.EmailDetails;
import com.example.cyberbankend.Dto.Response.RequestDto;
import com.example.cyberbankend.Dto.Response.VerifyOtpDto;
import com.example.cyberbankend.Model.PendingUser;
import com.example.cyberbankend.Repository.PendingUserRepository;
import com.example.cyberbankend.Repository.UserRepository;
import com.example.cyberbankend.Service.EmailService;
import com.example.cyberbankend.Service.PendingUserService;
import com.example.cyberbankend.Util.GenerateOtp;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class PendingUserServiceImplementation implements PendingUserService {
    private final UserRepository userRepository;
    private final PendingUserRepository pendingUserRepository;
    private final EmailService emailService;
    private final RedisTemplate<String, Object> redisTemplate;

    public PendingUserServiceImplementation(UserRepository userRepository, PendingUserRepository pendingUserRepository, JwtService jwtService, EmailService emailService, RedisTemplate<String, Object> redisTemplate) {
        this.userRepository = userRepository;
        this.pendingUserRepository = pendingUserRepository;
        this.emailService = emailService;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public VerifyOtpDto SendOTP(RequestDto requestDto) {

        if (userRepository.existsByEmail(requestDto.getEmail())  ){
            VerifyOtpDto verifyOtpDto = new VerifyOtpDto();
            verifyOtpDto.setMessage("Email already in use");
            return verifyOtpDto;
        }

        String otp = GenerateOtp.generateOTP();
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(10);
        PendingUser pendingUser = new PendingUser();
        pendingUser.setEmail(requestDto.getEmail());
        pendingUser.setOtp(otp);
        pendingUser.setOtpExpiry(expiry);

        PendingUser pendingUser1 = pendingUserRepository.save(pendingUser);
        redisTemplate.opsForValue().set("email:" + pendingUser1.getEmail(), pendingUser1.getId().toString());
        redisTemplate.opsForValue().set("otp:" + pendingUser1.getEmail(), otp);
        redisTemplate.expire("email:" + pendingUser1.getEmail(), 10, TimeUnit.MINUTES);
        redisTemplate.expire("otp:" + pendingUser1.getEmail(), 10, TimeUnit.MINUTES);
        System.out.println(otp);
        System.out.println(pendingUser1.getEmail());

        System.out.println(pendingUser1);
        EmailDetails emailDetails = new EmailDetails();
        emailDetails.setRecipient(pendingUser.getEmail());
        emailDetails.setSubject("Welcome to AjoPay - Start Saving Smartly! ");
        emailDetails.setMessageBody(String.format("""
    Dear %s,
    
    Thank you for your request. To complete your verification, please use the following One-Time Password (OTP):
    
    %s
    
    This OTP is valid for [duration, e.g., 10 minutes] and should not be shared with anyone for security reasons.
                    
    If you did not request this OTP, please ignore this email or contact our support team immediately.
                    
    Best regards,
    The AjoPay Team
    """, requestDto.getName(), pendingUser1.getOtp()));
        emailService.sendEmail(emailDetails);
        return new VerifyOtpDto("OTP Sent");
    }

//    @Override
//    public boolean doesEmailExit(String email) {
//        return redisTemplate.hasKey("email:" +email);
//    }
    @Override
    public Optional<PendingUser> getUserByEmail(String email) {
        String userID = (String) redisTemplate.opsForValue().get("email:" + email);

        if (userID != null && userID.matches("-?\\d+")) { // Ensure userID is numeric
            return pendingUserRepository.findById(Long.parseLong(userID));
        }
        return Optional.empty();
    }



}
