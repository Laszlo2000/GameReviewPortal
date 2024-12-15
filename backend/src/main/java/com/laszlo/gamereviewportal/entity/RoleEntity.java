package com.laszlo.gamereviewportal.entity;

import jakarta.persistence.*;
import jakarta.persistence.Id;
import org.springframework.security.core.GrantedAuthority;

import java.util.Objects;

@Entity
@Table(name = "roles")
public class RoleEntity implements GrantedAuthority {

    @Id
    @Column(name = "role_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "role_name", nullable = false)
    private String role;

    public RoleEntity() {
    }

    public RoleEntity(int id, String role) {
        this.id = id;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int roleId) {
        this.id = roleId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String roleName) {
        this.role = roleName;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RoleEntity that = (RoleEntity) o;
        return id == that.id && Objects.equals(role, that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, role);
    }

    @Override
    public String getAuthority() {
        return role;
    }
}
