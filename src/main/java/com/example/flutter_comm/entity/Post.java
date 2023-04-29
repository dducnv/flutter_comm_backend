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
@Table(name = "posts")
public class Post {
    @Id
    @JsonIgnore
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "post_title")
    private String title;
    @Column(unique = true,name = "post_slug")
    private String slug;

    @Column(columnDefinition = "text", name = "post_content")
    private String content;
    @Column(columnDefinition = "text",name = "post_desc")
    private String description;

    //stats
    @Column(columnDefinition = "int(11) default 0", name = "vote_count")
    private int voteCount;

    //relationship
    @ManyToOne
    @JoinColumn(name = "author", nullable = false)
    private User author;

    @ManyToOne
    @JoinColumn(name = "post_category_id", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private Set<ReactionPostCount> reactionCounts = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.MERGE, targetEntity = Tag.class)
    @JoinTable(
            name = "posts_tags",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    @JsonIgnoreProperties("posts")
    Set<Tag> tags = new HashSet<>();
    //status info
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    @Column(name = "post_status")
    private String status;
}
