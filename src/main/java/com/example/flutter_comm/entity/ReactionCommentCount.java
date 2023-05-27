package com.example.flutter_comm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reaction_comment_counts")
public class ReactionCommentCount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JsonIgnore
    private CommentPost commentPost;
    @ManyToOne
    private Reaction reaction;

    @OneToMany(mappedBy = "reactionCommentCount")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<UserReactionComment> userReactionComments = new HashSet<>();
}
