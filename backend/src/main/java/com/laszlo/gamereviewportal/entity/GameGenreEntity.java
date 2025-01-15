package com.laszlo.gamereviewportal.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "game_genres")
public class GameGenreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    @JsonBackReference
    private GameEntity game;

    @ManyToOne
    @JoinColumn(name = "genre_id", nullable = false)
    private GenreEntity genre;

    public GameGenreEntity() {
    }

    public GameGenreEntity(GameEntity game, GenreEntity genre) {
        this.game = game;
        this.genre = genre;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public GameEntity getGame() {
        return game;
    }

    public void setGame(GameEntity game) {
        this.game = game;
    }

    public GenreEntity getGenre() {
        return genre;
    }

    public void setGenre(GenreEntity genre) {
        this.genre = genre;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        GameGenreEntity that = (GameGenreEntity) o;
        return Objects.equals(game, that.game) && Objects.equals(genre, that.genre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(game, genre);
    }

    @Override
    public String toString() {
        return "GameGenreEntity{" +
                "id=" + id +
                ", game=" + game +
                ", genre=" + genre +
                '}';
    }
}
