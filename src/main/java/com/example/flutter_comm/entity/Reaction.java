package com.example.flutter_comm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "reactions")
public class Reaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "reaction_name")
    private String name;
    @Column(name = "icon_url")
    private String iconUrl;
    @Column(name = "reaction_emoji")
    private String emoji;
    @JsonIgnore
    @OneToMany(mappedBy = "reaction", cascade = CascadeType.ALL)
    private Set<ReactionPostCount> reactionCounts = new HashSet<>();
}
