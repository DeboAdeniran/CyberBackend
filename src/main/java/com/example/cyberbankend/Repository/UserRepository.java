package com.example.cyberbankend.Repository;

import com.example.cyberbankend.Model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {

    Users findByEmail(String email);

    Boolean existsByEmail(String email);

}
