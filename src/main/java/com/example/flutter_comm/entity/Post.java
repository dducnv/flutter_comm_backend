package com.example.flutter_comm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

@Table(name = "posts")
public class Post {
    @Id
    @JsonIgnore
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "post_title")
    private String title;
    @Column(name = "post_slug",unique = true)
    private String slug;
    @Column(name = "post_uuid", columnDefinition = "char(36)")
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID uuid;
    @Column(columnDefinition = "longtext", name = "post_content")
    private String content;
    @Column(columnDefinition = "longtext",name = "post_desc")
    private String description;
    @Column(columnDefinition = "int(11) default 0", name = "view_count")
    private int viewCount;

    //relationship
    @ManyToOne
    @JoinColumn(name = "author", nullable = false)
    private User author;
    @ManyToOne
    @JoinColumn(name = "post_category_id", nullable = false)
    private Category category;
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private Set<ReactionPostCount> reactionCounts = new HashSet<>();
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private Set<CommentPost> commentPosts = new HashSet<>();
    @ManyToMany
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinTable(
            name = "post_tags",
            joinColumns = @JoinColumn(name = "post_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id"))
    @JsonIgnoreProperties("posts")
    private Set<Tag> tags = new HashSet<>();
    //status info
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    @Column(name = "post_public")
    private boolean postPublic = Boolean.TRUE;
    private String status;
}
