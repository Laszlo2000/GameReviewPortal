package com.laszlo.gamereviewportal.controller;

import com.laszlo.gamereviewportal.dto.GameDto;
import com.laszlo.gamereviewportal.entity.GameEntity;
import com.laszlo.gamereviewportal.service.GameService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/games")
@PreAuthorize("hasAuthority('admin')")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('admin')")
    public List<GameDto> getAllGames() {
        return gameService.getAllGames();
    }

    // Játék keresése cím alapján
    @GetMapping("/title")
    @PreAuthorize("hasAuthority('admin')")
    public Optional<GameEntity> getGameByTitle(@RequestParam String title) {
        return gameService.findByTitle(title);
    }

    // Játék keresése cím részlete alapján
    @GetMapping("/title/search")
    @PreAuthorize("hasAuthority('admin')")
    public List<GameEntity> getGamesByTitleContaining(@RequestParam String title) {
        return gameService.findByTitleContainingIgnoreCase(title);
    }

    // Játékok keresése fejlesztő szerint
    @GetMapping("/developer/{developerId}")
    @PreAuthorize("hasAuthority('admin')")
    public List<GameEntity> getGamesByDeveloper(@PathVariable Long developerId) {
        return gameService.findByDeveloperId(developerId);
    }

    // Játékok szűrése kiadási dátum szerint
    @GetMapping("/release-date")
    @PreAuthorize("hasAuthority('admin')")
    public List<GameEntity> getGamesByReleaseDateBetween(@RequestParam String startDate, @RequestParam String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        return gameService.findByReleaseDateBetween(start, end);
    }

    // Játékok listázása értékelések szerint (csökkenő)
    @GetMapping("/top-rated")
    @PreAuthorize("hasAuthority('admin')")
    public List<GameEntity> getTopRatedGames() {
        return gameService.findTopRatedGames();
    }

    // Játékok listázása értékelések szerint (növekvő)
    @GetMapping("/lowest-rated")
    @PreAuthorize("hasAuthority('admin')")
    public List<GameEntity> getLowestRatedGames() {
        return gameService.findLowestRatedGames();
    }

    // Pontos árra keresés
    @GetMapping("/price")
    @PreAuthorize("hasAuthority('admin')")
    public List<GameEntity> getGamesByPrice(@RequestParam BigDecimal price) {
        return gameService.findByPrice(price);
    }

    // Pontos értékelésre keresés
    @GetMapping("/rating")
    @PreAuthorize("hasAuthority('admin')")
    public List<GameEntity> getGamesByAverageRating(@RequestParam BigDecimal averageRating) {
        return gameService.findByAverageRating(averageRating);
    }

    // Játékok szűrése ár alapján
    @GetMapping("/price/less-than")
    @PreAuthorize("hasAuthority('admin')")
    public List<GameEntity> getGamesCheaperThan(@RequestParam BigDecimal price) {
        return gameService.findGamesCheaperThan(price);
    }

    @GetMapping("/price/greater-than")
    @PreAuthorize("hasAuthority('admin')")
    public List<GameEntity> getGamesMoreExpensiveThan(@RequestParam BigDecimal price) {
        return gameService.findGamesMoreExpensiveThan(price);
    }

    @GetMapping("/price/at-most")
    @PreAuthorize("hasAuthority('admin')")
    public List<GameEntity> getGamesAtMostPrice(@RequestParam BigDecimal price) {
        return gameService.findGamesAtMostPrice(price);
    }

    @GetMapping("/price/at-least")
    @PreAuthorize("hasAuthority('admin')")
    public List<GameEntity> getGamesAtLeastPrice(@RequestParam BigDecimal price) {
        return gameService.findGamesAtLeastPrice(price);
    }

    // Játékok szűrése értékelés alapján
    @GetMapping("/rating/below")
    @PreAuthorize("hasAuthority('admin')")
    public List<GameEntity> getGamesWithRatingBelow(@RequestParam BigDecimal averageRating) {
        return gameService.findGamesWithRatingBelow(averageRating);
    }

    @GetMapping("/rating/above")
    @PreAuthorize("hasAuthority('admin')")
    public List<GameEntity> getGamesWithRatingAbove(@RequestParam BigDecimal averageRating) {
        return gameService.findGamesWithRatingAbove(averageRating);
    }

    @GetMapping("/rating/at-most")
    @PreAuthorize("hasAuthority('admin')")
    public List<GameEntity> getGamesWithRatingAtMost(@RequestParam BigDecimal averageRating) {
        return gameService.findGamesWithRatingAtMost(averageRating);
    }

    @GetMapping("/rating/at-least")
    @PreAuthorize("hasAuthority('admin')")
    public List<GameEntity> getGamesWithRatingAtLeast(@RequestParam BigDecimal averageRating) {
        return gameService.findGamesWithRatingAtLeast(averageRating);
    }
}