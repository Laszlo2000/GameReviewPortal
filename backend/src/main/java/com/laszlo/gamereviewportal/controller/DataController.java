package com.laszlo.gamereviewportal.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
public class DataController {

    @GetMapping("/api/data")
    public Map<String, String> getData() {
        // A válasz egy JSON objektum, ami tartalmazza a message kulcsot
        Map<String, String> response = new HashMap<>();
        response.put("message", "Hello from backend!🖐️");
        return response;
    }
}
