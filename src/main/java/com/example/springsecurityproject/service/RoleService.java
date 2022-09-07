package com.example.springsecurityproject.service;

import com.example.springsecurityproject.entity.RoleEntity;
import com.example.springsecurityproject.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public ResponseEntity<String> addRole(String roleName){
        RoleEntity role = new RoleEntity();
        role.setRoleName(roleName);
        roleRepository.save(role);

        return ResponseEntity.ok().body("Ruolo aggiunto");
    }

    public RoleEntity findById(Long id){
        return roleRepository.getReferenceById(id);
    }
}
