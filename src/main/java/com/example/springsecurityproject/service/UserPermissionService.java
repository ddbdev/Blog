package com.example.springsecurityproject.service;

import com.example.springsecurityproject.entity.RoleEntity;
import com.example.springsecurityproject.entity.UserEntity;
import com.example.springsecurityproject.entity.UserPermissionEntity;
import com.example.springsecurityproject.repository.RoleRepository;
import com.example.springsecurityproject.repository.UserPermissionRepository;
import com.example.springsecurityproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserPermissionService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    private final UserPermissionRepository userPermissionRepository;

    public void addRoleToUser(RoleEntity role, UserEntity user){

        UserPermissionEntity userPermissionEntity = new UserPermissionEntity();
        userPermissionEntity.setUser(user);
        userPermissionEntity.setRole(role);

        userPermissionRepository.save(userPermissionEntity);
    }

    public List<String> getRolesByUser(UserEntity userEntity){

        return userPermissionRepository.getUserPermissionEntity(userEntity);
    }

    public List<String> getRoleId(UserEntity user){
        List<String> roleId = userPermissionRepository.getUserPermissionId(user);
        return roleId;
    }

    public List<String> getUserPermissions (String ids){
        return userPermissionRepository.getPermissionNameBySet(ids);
    }
    public Set<SimpleGrantedAuthority> getGrantedAuthorities(List<String> roles){
        Set<SimpleGrantedAuthority> permissions = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
        for (String p : roles)
        {
            permissions.add(new SimpleGrantedAuthority(p));
        }

        return permissions;
    }
    public void setGrantedAuthorities (Set<SimpleGrantedAuthority> authorities, UserEntity userEntity){
        userEntity.setAuthorities(authorities);
    }
}
