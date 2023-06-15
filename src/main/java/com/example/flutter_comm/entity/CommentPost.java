package com.example.flutter_comm.entity;

import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "comments")
public class CommentPost {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "longtext")
    private String content;
    @Column(name = "comment_uuid", columnDefinition = "char(36)",unique = true)
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID uuid;
    @ManyToOne
    private User user;
    @ManyToOne
    private Post post;
    @ManyToOne
    private CommentPost supperComment;
    @ManyToOne
    private CommentPost parent;
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(mappedBy = "supperComment")
    private List<CommentPost> listOfSupperComment;
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(mappedBy = "parent")
    private List<CommentPost> replies;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    private LocalDateTime editedAt;
    private boolean isDelete = Boolean.FALSE;
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(mappedBy = "commentPost", cascade = CascadeType.ALL)
    private Set<ReactionCommentCount> reactionCommentCounts = new HashSet<>();

    public void addReply(CommentPost commentPost){
        this.replies.add(commentPost);
    }
}
