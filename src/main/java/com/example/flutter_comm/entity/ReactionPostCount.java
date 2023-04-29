package com.example.flutter_comm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reaction_post_counts")

public class ReactionPostCount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JsonIgnore
    private Post post;
    @ManyToOne
    private Reaction reaction;

    @OneToMany(mappedBy = "reactionPostCount")
    private Set<UserReactionPost> userReactionPost = new HashSet<>();

    public void addUserReactPost(UserReactionPost userReactionPostParam){
        userReactionPost.add(userReactionPostParam);
        userReactionPostParam.setReactionPostCount(this);
    };
}
