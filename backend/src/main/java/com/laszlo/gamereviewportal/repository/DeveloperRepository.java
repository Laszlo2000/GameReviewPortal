package com.laszlo.gamereviewportal.repository;

import com.laszlo.gamereviewportal.entity.DeveloperEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeveloperRepository extends JpaRepository<DeveloperEntity, Long> {
    Optional<DeveloperEntity> findByName(String name);
    List<DeveloperEntity> findByNameContainingIgnoreCase(String name);
    boolean existsByName(String name);
    Optional<DeveloperEntity> findById(Long id); // Ez a metódus alapból elérhető, de explicit is megadhatjuk
}
