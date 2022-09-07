package com.example.springsecurityproject;

import com.example.springsecurityproject.entity.UserEntity;
import com.example.springsecurityproject.repository.RoleRepository;
import com.example.springsecurityproject.repository.UserRepository;
import com.example.springsecurityproject.service.PermissionService;
import com.example.springsecurityproject.service.RoleService;
import com.example.springsecurityproject.service.UserPermissionService;
import com.example.springsecurityproject.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;

@SpringBootApplication
public class SpringSecurityProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityProjectApplication.class, args);
    }
}