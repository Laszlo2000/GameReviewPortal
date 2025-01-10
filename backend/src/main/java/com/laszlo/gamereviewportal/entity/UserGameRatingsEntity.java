package com.laszlo.gamereviewportal.entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "user_game_ratings")
public class UserGameRatingsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "game_id", nullable = false)
    private GameEntity game;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public UserGameRatingsEntity() {
    }

    public UserGameRatingsEntity(GameEntity game, UserEntity user) {
        this.game = game;
        this.user = user;
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

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserGameRatingsEntity that = (UserGameRatingsEntity) o;
        return Objects.equals(game, that.game) && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(game, user);
    }

    @Override
    public String toString() {
        return "UserGameRatingsEntity{" +
                "id=" + id +
                ", game=" + game +
                ", user=" + user +
                '}';
    }
}
