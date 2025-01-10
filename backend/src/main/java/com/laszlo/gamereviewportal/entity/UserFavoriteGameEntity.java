package com.laszlo.gamereviewportal.entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "user_favorite_games", uniqueConstraints = {
@UniqueConstraint(columnNames = {"game_id", "user_id"})
})
public class UserFavoriteGameEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "game_id", nullable = false)
    private GameEntity game;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    public UserFavoriteGameEntity() {
    }

    public UserFavoriteGameEntity(GameEntity game, UserEntity user) {
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
        UserFavoriteGameEntity that = (UserFavoriteGameEntity) o;
        return Objects.equals(game, that.game) && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(game, user);
    }

    @Override
    public String toString() {
        return "UserFavoriteGameEntity{" +
                "id=" + id +
                ", game=" + game +
                ", user=" + user +
                '}';
    }
}
