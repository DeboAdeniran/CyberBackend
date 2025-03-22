package com.example.cyberbankend.Repository;

import com.example.cyberbankend.Model.PendingUser;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PendingUserRepository extends CrudRepository<PendingUser, Long> {
    Optional<PendingUser> findByOtp(String otp);
    Optional<PendingUser> findByEmail(String email);


}
