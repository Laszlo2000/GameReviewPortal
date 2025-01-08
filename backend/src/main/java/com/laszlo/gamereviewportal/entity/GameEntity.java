package com.laszlo.gamereviewportal.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "games")
public class GameEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "title", nullable = false, unique = true)
    @NotBlank(message = "Title cannot be empty")
    private String title;

    @ManyToOne
    //@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "developer_id", referencedColumnName = "id", nullable = false)
    private DeveloperEntity developer;

    @Column(name = "release_date", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;

    @Column(name = "cover_image_url", nullable = false, unique = true)
    @NotBlank(message = "Cover image URL cannot be empty")
    private String coverImage;

    @Column(name = "description", length = 5000)
    private String description;

    @Column(name = "price")
    @NotNull(message = "Price cannot be empty")
    @Positive(message = "Price must be greater than zero")
    private BigDecimal price;

    @Column(name = "average_rating")
    private BigDecimal averageRating;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    @UpdateTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<GameGenreEntity> gameGenres = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "game_platforms",
            joinColumns = @JoinColumn(name = "game_id"),
            inverseJoinColumns = @JoinColumn(name = "platform_id")
    )
    private Set<PlatformEntity> platforms = new HashSet<>();

    public GameEntity() {
    }

    public GameEntity(String title, DeveloperEntity developer, LocalDate releaseDate, String coverImage, String description, BigDecimal price, BigDecimal averageRating, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.title = title;
        this.developer = developer;
        this.releaseDate = releaseDate;
        this.coverImage = coverImage;
        this.description = description;
        this.price = price;
        this.averageRating = averageRating;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Set<PlatformEntity> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(Set<PlatformEntity> platforms) {
        this.platforms = platforms;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public DeveloperEntity getDeveloper() {
        return developer;
    }

    public void setDeveloper(DeveloperEntity developer) {
        this.developer = developer;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
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

    public Set<GameGenreEntity> getGameGenres() {
        return gameGenres;
    }

    public void setGameGenres(Set<GameGenreEntity> gameGenres) {
        this.gameGenres = gameGenres;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        GameEntity that = (GameEntity) o;
        return id == that.id && Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }

    @Override
    public String toString() {
        String genresString = gameGenres.stream()
                .map(gameGenre -> gameGenre.getGenre().getName())
                .reduce((a, b) -> a + ", " + b)
                .orElse("No genres");
        return "GameEntity{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", developer=" + (developer != null ? developer.getName() : "null") +
                ", release_date=" + releaseDate +
                ", cover_image_url='" + coverImage + '\'' +
                ", price=" + price +
                ", average_rating=" + averageRating +
                ", genres=[" + genresString + "]" +
                '}';
    }
}
