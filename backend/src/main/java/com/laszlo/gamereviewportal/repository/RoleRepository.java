package com.laszlo.gamereviewportal.repository;

import com.laszlo.gamereviewportal.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findById(Long id);
}
