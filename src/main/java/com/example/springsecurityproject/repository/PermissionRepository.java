package com.example.springsecurityproject.repository;

import com.example.springsecurityproject.entity.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<PermissionEntity, Long> {
    @Override
    PermissionEntity getReferenceById(Long aLong);
}
