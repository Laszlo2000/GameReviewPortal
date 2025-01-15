package com.laszlo.gamereviewportal.service;

import com.laszlo.gamereviewportal.dto.GameDto;
import com.laszlo.gamereviewportal.entity.DeveloperEntity;
import com.laszlo.gamereviewportal.entity.GameEntity;
import com.laszlo.gamereviewportal.repository.DeveloperRepository;
import com.laszlo.gamereviewportal.repository.GameRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GameService {

    private final GameRepository gameRepository;
    private final DeveloperRepository developerRepository; // helpers típus

    public GameService(GameRepository gameRepository, DeveloperRepository developerRepository) {
        this.gameRepository = gameRepository;
        this.developerRepository = developerRepository;
    }

    public void addGame(GameDto gameDto) {
        // Fejlesztő entitás keresése
        DeveloperEntity developer = developerRepository.findById(gameDto.getDeveloperId())
                .orElseThrow(() -> new EntityNotFoundException("Developer not found."));

        // Új játék entitás létrehozása
        GameEntity gameEntity = new GameEntity();
        gameEntity.setTitle(gameDto.getTitle());
        gameEntity.setDeveloper(developer);
        gameEntity.setReleaseDate(gameDto.getReleaseDate());
        gameEntity.setCoverImage(gameDto.getCoverImageUrl());
        gameEntity.setDescription(gameDto.getDescription());
        gameEntity.setPrice(gameDto.getPrice());
        gameEntity.setAverageRating(BigDecimal.ZERO); // Kezdetben nincs értékelés

        // Mentés az adatbázisba
        gameRepository.save(gameEntity);
    }

    public void deleteGame(Long id) {
        if (!gameRepository.existsById(id)) {
            throw new EntityNotFoundException("Game not found.");
        }
        gameRepository.deleteById(id);
    }

    public void updateGame(Long id, GameDto gameDto) {
        GameEntity gameEntity = gameRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Game not found."));

        // Csak a módosítható mezők frissítése
        gameEntity.setTitle(gameDto.getTitle());
        gameEntity.setCoverImage(gameDto.getCoverImageUrl());
        gameEntity.setReleaseDate(gameDto.getReleaseDate());
        gameEntity.setPrice(gameDto.getPrice());

        // Developer ID frissítése
        DeveloperEntity developer = developerRepository.findById(gameDto.getDeveloperId())
                .orElseThrow(() -> new EntityNotFoundException("Developer not found."));
        gameEntity.setDeveloper(developer);

        gameRepository.save(gameEntity);
    }

    public List<GameDto> getAllGames() {
        List<GameEntity> games = gameRepository.findAll();
        return games.stream().map(game -> {
            GameDto dto = new GameDto();
            dto.setId(game.getId());
            dto.setTitle(game.getTitle());
            dto.setDeveloperId(game.getDeveloper().getId());
            dto.setDeveloperName(game.getDeveloper().getName()); // Fejlesztő neve hozzáadva
            dto.setReleaseDate(LocalDate.parse(game.getReleaseDate().toString()));
            dto.setCoverImageUrl(game.getCoverImage());
            dto.setDescription(game.getDescription());
            dto.setPrice(game.getPrice());
            dto.setAverageRating(game.getAverageRating());
            dto.setCreatedAt(game.getCreatedAt());
            dto.setUpdatedAt(game.getUpdatedAt());
            return dto;
        }).collect(Collectors.toList());
    }


    // Játék keresése cím alapján
    public Optional<GameEntity> findByTitle(String title) {
        return gameRepository.findByTitle(title);
    }

    // Játék keresése cím részlete alapján
    public List<GameEntity> findByTitleContainingIgnoreCase(String title) {
        return gameRepository.findByTitleContainingIgnoreCase(title);
    }

    // Játékok keresése fejlesztő szerint
    public List<GameEntity> findByDeveloperId(Long developerId) {
        return gameRepository.findByDeveloper_Id(developerId);
    }

    // Játékok szűrése kiadási dátum szerint
    public List<GameEntity> findByReleaseDateBetween(LocalDate startDate, LocalDate endDate) {
        return gameRepository.findByReleaseDateBetween(startDate, endDate);
    }

    // Játékok listázása értékelések szerint (csökkenő)
    public List<GameEntity> findTopRatedGames() {
        return gameRepository.findByOrderByAverageRatingDesc();
    }

    // Játékok listázása értékelések szerint (növekvő)
    public List<GameEntity> findLowestRatedGames() {
        return gameRepository.findByOrderByAverageRatingAsc();
    }

    // Pontos árra keresés
    public List<GameEntity> findByPrice(BigDecimal price) {
        return gameRepository.findByPrice(price);
    }

    // Pontos értékelésre keresés
    public List<GameEntity> findByAverageRating(BigDecimal averageRating) {
        return gameRepository.findByAverageRating(averageRating);
    }

    // Játékok szűrése ár alapján
    public List<GameEntity> findGamesCheaperThan(BigDecimal price) {
        return gameRepository.findByPriceLessThan(price);
    }

    public List<GameEntity> findGamesMoreExpensiveThan(BigDecimal price) {
        return gameRepository.findByPriceGreaterThan(price);
    }

    public List<GameEntity> findGamesAtMostPrice(BigDecimal price) {
        return gameRepository.findByPriceLessThanEqual(price);
    }

    public List<GameEntity> findGamesAtLeastPrice(BigDecimal price) {
        return gameRepository.findByPriceIsGreaterThanEqual(price);
    }

    // Játékok szűrése értékelés alapján
    public List<GameEntity> findGamesWithRatingBelow(BigDecimal averageRating) {
        return gameRepository.findByAverageRatingLessThan(averageRating);
    }

    public List<GameEntity> findGamesWithRatingAbove(BigDecimal averageRating) {
        return gameRepository.findByAverageRatingGreaterThan(averageRating);
    }

    public List<GameEntity> findGamesWithRatingAtMost(BigDecimal averageRating) {
        return gameRepository.findByAverageRatingLessThanEqual(averageRating);
    }

    public List<GameEntity> findGamesWithRatingAtLeast(BigDecimal averageRating) {
        return gameRepository.findByAverageRatingGreaterThanEqual(averageRating);
    }
}
