package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entites.User;

public interface UserRepository extends JpaRepository<User, Integer>{
    // có thể thêm custom query nếu cần, ví dụ:
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
}
