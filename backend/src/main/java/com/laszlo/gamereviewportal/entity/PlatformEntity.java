package com.laszlo.gamereviewportal.entity;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "platforms")
public class PlatformEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "platforms")
    private Set<GameEntity> games = new HashSet<>();

    public PlatformEntity() {
    }

    public PlatformEntity(String name) {
        this.name = name;
    }

    public Set<GameEntity> getGames() {
        return games;
    }

    public void setGames(Set<GameEntity> games) {
        this.games = games;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PlatformEntity that = (PlatformEntity) o;
        return id == that.id && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "PlatformEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
