package com.example.springsecurityproject.service;

import com.example.springsecurityproject.entity.TokenEntity;
import com.example.springsecurityproject.entity.UserEntity;
import com.example.springsecurityproject.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserPermissionService userPermissionService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findUsersByUsername(username);

        if (user == null){
            throw new NullPointerException("User not found");
            //TODO Gestire l'eccezione.
        }

        List<String> roleId = userPermissionService.getRoleId(user);
        StringBuilder rolesId = new StringBuilder();

        for (String str : roleId){
            rolesId.append(str).append(",");
        }
        if (rolesId.length() > 0)
            rolesId.replace(rolesId.lastIndexOf(","), rolesId.lastIndexOf(",") + 1, "");

        List<String> auth = userPermissionService.getRolesByUser(user);
        auth.addAll(userPermissionService.getUserPermissions(rolesId.toString()));

        log.error("ruoli {}", auth);
        Set<SimpleGrantedAuthority> authorities = userPermissionService.getGrantedAuthorities(auth);
        userPermissionService.setGrantedAuthorities(authorities, user);

        return user;
    }

    public UserEntity addUser(UserEntity userEntity){
        String encodedPassword = this.passwordEncoder.encode(userEntity.getPassword());
        userEntity.setPassword(encodedPassword);
        return userRepository.save(userEntity);
    }

    public void setToken(String token){
        userRepository.setConfirmTokenToNull(token);
    }

    public boolean findToken(String token){
        UserEntity user = userRepository.findUserByConfirmToken(token);
        return user != null;
    }

    public UserEntity findUserById(Long id){
        return userRepository.getReferenceById(id);
    }
}
