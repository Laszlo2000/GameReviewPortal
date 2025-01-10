package com.laszlo.gamereviewportal.service;

import com.laszlo.gamereviewportal.entity.DeveloperEntity;
import com.laszlo.gamereviewportal.repository.DeveloperRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeveloperService {

    private final DeveloperRepository developerRepository;

    public DeveloperService(DeveloperRepository developerRepository) {
        this.developerRepository = developerRepository;
    }

    public List<DeveloperEntity> getAllDevelopers() {
        return developerRepository.findAll();
    }

    public Optional<DeveloperEntity> getDeveloperById(Long id) {
        return developerRepository.findById(id);
    }

    public Optional<DeveloperEntity> getDeveloperByName(String name) {
        return developerRepository.findByName(name);
    }

    public List<DeveloperEntity> searchDevelopersByName(String name) {
        return developerRepository.findByNameContainingIgnoreCase(name);
    }

    public DeveloperEntity saveDeveloper(DeveloperEntity developer) {
        if (developerRepository.existsByName(developer.getName())) {
            throw new IllegalArgumentException("Developer with this name already exists.");
        }
        return developerRepository.save(developer);
    }

    public void deleteDeveloper(Long id) {
        if (!developerRepository.existsById(id)) {
            throw new IllegalArgumentException("Developer with this ID does not exist.");
        }
        developerRepository.deleteById(id);
    }
}
