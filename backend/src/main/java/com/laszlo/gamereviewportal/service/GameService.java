package com.laszlo.gamereviewportal.service;

import com.laszlo.gamereviewportal.dto.GameDto;
import com.laszlo.gamereviewportal.entity.GameEntity;
import com.laszlo.gamereviewportal.repository.GameRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class GameService {

    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public List<GameDto> getAllGames() {
        return gameRepository.findAll()
                .stream()
                .map(game -> new GameDto(
                        game.getId(),
                        game.getTitle(),
                        game.getDeveloper().getId(),
                        game.getReleaseDate(),
                        game.getCoverImage(),
                        game.getDescription(),
                        game.getPrice(),
                        game.getAverageRating(),
                        game.getCreatedAt(),
                        game.getUpdatedAt()))
                .toList();
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
