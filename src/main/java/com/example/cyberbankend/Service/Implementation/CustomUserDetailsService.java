package com.example.cyberbankend.Service.Implementation;

import com.example.cyberbankend.Model.Users;
import com.example.cyberbankend.Repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    public final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Users> user = userRepository.findByEmail(username);

        if (user.isEmpty()){
            user = userRepository.findByPhoneNumber(username);
        }
        return user.map(users -> new CustomUserDetails(users, username))
                .orElseThrow(()-> new UsernameNotFoundException("User with email or phonenumber not found"+ username));
    }
}
