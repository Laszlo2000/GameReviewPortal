package com.laszlo.gamereviewportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.laszlo.gamereviewportal.entity.UsersEntity;

@Repository
public interface UserRepository extends JpaRepository<UsersEntity, Long> {
    UsersEntity findByUsername(String username);
}
