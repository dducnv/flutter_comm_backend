package com.example.flutter_comm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Setter
@Getter
@Table(name = "tags")
public class Tag {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="tag_name")
    private String name;
    @Column(unique = true, name = "tag_slug")
    private String slug;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    //relationship
    @ManyToOne
    @JoinColumn(name = "tag_created_by", nullable = false)
    private User created_by;
    @JsonIgnore
    @ManyToMany(mappedBy = "tags", targetEntity = Post.class)
    @JsonIgnoreProperties("tags")
    List<Post> posts = new ArrayList<>();

    public Tag() {
    }

    public Tag(String name, String slug, User created_by) {
        this.name = name;
        this.slug = slug;
        this.created_by = created_by;
    }

    public Tag(Long id, String name, String slug, LocalDateTime createdAt, LocalDateTime updatedAt, User created_by, List<Post> posts) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.created_by = created_by;
        this.posts = posts;
    }
}
