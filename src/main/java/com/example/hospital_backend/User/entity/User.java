package com.example.hospital_backend.User.entity;

import com.example.hospital_backend.favorite.entity.Favorite;
import com.example.hospital_backend.review.entity.Review;
import jakarta.persistence.*;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String password;

    @OneToMany(mappedBy = "user")
    private List<Review> reviews;

    @OneToMany(mappedBy = "user")
    private List<Favorite> favorites;

    @Enumerated(EnumType.STRING)
    private Role role = Role.ROLE_USER; // 기본값 설정

    public User() {
        this.role = Role.ROLE_USER; // 기본 생성자에서 ROLE_USER로 초기화
    }

    public Set<GrantedAuthority> getAuthorities() {
        return Set.of(new SimpleGrantedAuthority(role.name()));
    }
}
