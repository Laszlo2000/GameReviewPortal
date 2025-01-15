package com.laszlo.gamereviewportal.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.laszlo.gamereviewportal.entity.GameEntity;
import com.laszlo.gamereviewportal.entity.PlatformEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public class GameDto {
    private Long id;
    private String title;
    private Long developerId;
    private String developerName; // Új mező
    private LocalDate releaseDate;
    private String coverImageUrl;
    private String description;
    private BigDecimal price;
    private BigDecimal averageRating;
    private Set<String> gameGenres;
    private Set<String> platforms;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    public GameDto() {
    }

    public GameDto(long id, String title, long developerId, String developerName, LocalDate releaseDate,
                   String coverImage, String description, BigDecimal price,
                   BigDecimal averageRating, LocalDateTime createdAt,
                   LocalDateTime updatedAt, Set<String> platforms, Set<String> gameGenres) {
        this.id = id;
        this.title = title;
        this.developerId = developerId;
        this.developerName = developerName;
        this.releaseDate = releaseDate;
        this.coverImageUrl = coverImage;
        this.description = description;
        this.price = price;
        this.averageRating = averageRating;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.platforms = platforms;
        this.gameGenres = gameGenres;
    }

    public <R> GameDto(long id, String title, long id1, LocalDate releaseDate, String coverImage, String description, BigDecimal price, BigDecimal averageRating, LocalDateTime createdAt, LocalDateTime updatedAt, R collect, R collect1) {
    }

    public static GameDto fromEntity(GameEntity game) {
        return new GameDto(
                game.getId(),
                game.getTitle(),
                game.getDeveloper().getId(),
                game.getReleaseDate(),
                game.getCoverImage(),
                game.getDescription(),
                game.getPrice(),
                game.getAverageRating(),
                game.getCreatedAt(),
                game.getUpdatedAt(),
                game.getPlatforms().stream()
                        .map(PlatformEntity::getName)
                        .collect(Collectors.toSet()),
                game.getGameGenres().stream()
                        .map(genre -> genre.getGenre().getName()) // Ha van "genre" mező
                        .collect(Collectors.toSet())
        );
    }

    public String getDeveloperName() {
        return developerName;
    }

    public void setDeveloperName(String developerName) {
        this.developerName = developerName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getDeveloperId() {
        return developerId;
    }

    public void setDeveloperId(Long developerId) {
        this.developerId = developerId;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(BigDecimal averageRating) {
        this.averageRating = averageRating;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
