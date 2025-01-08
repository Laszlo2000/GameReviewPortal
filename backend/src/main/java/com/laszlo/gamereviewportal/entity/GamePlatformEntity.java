package com.laszlo.gamereviewportal.entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "game_platforms")
public class GamePlatformEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private GameEntity game;

    @ManyToOne
    @JoinColumn(name = "platform_id", nullable = false)
    private PlatformEntity platform;

    public GamePlatformEntity() {
    }

    public GamePlatformEntity(GameEntity game, PlatformEntity platform) {
        this.game = game;
        this.platform = platform;
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

    public PlatformEntity getPlatform() {
        return platform;
    }

    public void setPlatform(PlatformEntity platform) {
        this.platform = platform;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        GamePlatformEntity that = (GamePlatformEntity) o;
        return id == that.id && Objects.equals(game, that.game) && Objects.equals(platform, that.platform);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, game, platform);
    }

    @Override
    public String toString() {
        return "GamePlatformEntity{" +
                "id=" + id +
                ", game=" + game +
                ", platform=" + platform +
                '}';
    }
}
