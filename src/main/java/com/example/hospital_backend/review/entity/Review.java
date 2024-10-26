package com.example.hospital_backend.review.entity;

import com.example.hospital_backend.User.entity.User;
import com.example.hospital_backend.hospital.entity.Hospital;
import com.example.hospital_backend.photo.entity.Photo;
import jakarta.persistence.*;
import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;
import java.util.List;
import org.hibernate.annotations.CreationTimestamp;

@Getter
@Setter
@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;

    private int rating; // 1-5 별점

    @Lob
    private String content;

    @CreationTimestamp
    private Date createdAt;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Photo> photos = new ArrayList<>();

}
