package com.example.flutter_comm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
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
    @JoinColumn(name = "tag_created_by")
    private User created_by;
    @JsonIgnore
    @ManyToMany(mappedBy = "tags", targetEntity = Post.class)
    @JsonIgnoreProperties("tags")
    private Set<Post> posts = new HashSet<>();


}
