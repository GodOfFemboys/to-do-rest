package org.example.todorest.Entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "app_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String password;

    @ElementCollection(targetClass = RoleType.class, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id")
    )
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<RoleType> roleTypeSet = new HashSet<>();

    public User() {
    }

    public User(Set<RoleType> roleTypeSet, String password, String name) {
        this.roleTypeSet = roleTypeSet;
        this.password = password;
        this.name = name;
    }

    public Set<RoleType> getRoleTypeSet() {
        return roleTypeSet;
    }

    public void setRoleTypeSet(Set<RoleType> roleTypeSet) {
        this.roleTypeSet = roleTypeSet;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
