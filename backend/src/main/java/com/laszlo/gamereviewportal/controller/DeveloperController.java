package com.laszlo.gamereviewportal.controller;

import com.laszlo.gamereviewportal.entity.DeveloperEntity;
import com.laszlo.gamereviewportal.service.DeveloperService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/developers")
@PreAuthorize("hasAuthority('admin')")
public class DeveloperController {

    private final DeveloperService developerService;

    public DeveloperController(DeveloperService developerService) {
        this.developerService = developerService;
    }

    // Összes fejlesztőstúdió listázása
    @GetMapping
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<List<DeveloperEntity>> getAllDevelopers() {
        return ResponseEntity.ok(developerService.getAllDevelopers());
    }

    // Fejlesztőstúdió keresése ID alapján
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<DeveloperEntity> getDeveloperById(@PathVariable Long id) {
        return developerService.getDeveloperById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Fejlesztőstúdió keresése név alapján
//    @GetMapping("/search")
//    public ResponseEntity<List<DeveloperEntity>> searchDevelopersByName(@RequestParam String name) {
//        return ResponseEntity.ok(developerService.searchDevelopersByName(name));
//    }

    // Új fejlesztőstúdió hozzáadása
    @PostMapping
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<DeveloperEntity> createDeveloper(@RequestBody DeveloperEntity developer) {
        DeveloperEntity savedDeveloper = developerService.saveDeveloper(developer);
        return ResponseEntity.ok(savedDeveloper);
    }

    // Fejlesztőstúdió törlése
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<String> deleteDeveloper(@PathVariable Long id) {
        developerService.deleteDeveloper(id);
        return ResponseEntity.ok("Developer deleted succesfully!");
    }
}
