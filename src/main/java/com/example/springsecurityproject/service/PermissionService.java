package com.example.springsecurityproject.service;

import com.example.springsecurityproject.entity.PermissionEntity;
import com.example.springsecurityproject.entity.RoleEntity;
import com.example.springsecurityproject.repository.PermissionRepository;
import com.example.springsecurityproject.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;

    public void addPermissionToRoles(String permission, RoleEntity role)
    {
        RoleEntity roleEntity = roleRepository.getReferenceById(role.getId());
        if (roleEntity == null)
            throw new IllegalStateException("User not found");
        PermissionEntity permissionEntity = new PermissionEntity();
        permissionEntity.setPermissionName(permission);
        permissionEntity.setRoleEntity(roleEntity);
        permissionRepository.save(permissionEntity);

    }
}
