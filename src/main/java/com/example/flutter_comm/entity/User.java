package com.example.flutter_comm.entity;

import com.example.flutter_comm.entity.my_enum.AuthProvider;
import com.example.flutter_comm.entity.my_enum.UserStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User  {

    //information
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    @Column(name = "user_avatar")
    private String avatar;
    @Column(name = "full_name", nullable = false)
    private String name;
    @Column(name = "user_name", unique = true, nullable = false)
    @JsonIgnore
    private String username;
    @Column(name="web_url")
    private String webUrl;
    @JsonIgnore
    @Column(name="user_bio")
    private String bio;

    //for otp login
    @JsonIgnore
    @Column(name = "user_email", unique = true, nullable = false)
    private String email;
    @JsonIgnore
    private String one_time_password; // null
    @JsonIgnore
    private Date expire_time; //nul

    //for social login
    @Enumerated(EnumType.STRING)
    @JsonIgnore
    @Column(name = "provider")
    private AuthProvider provider; // enum
    @JsonIgnore
    @Column(name = "provider_id")
    private String providerId;
    @JsonIgnore
    @Column(name = "password")
    private String password;

    //base
    @CreationTimestamp
    @Column(name = "created_at")
    @JsonIgnore
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    @JsonIgnore
    private LocalDateTime updatedAt;
    @Column(name="user_status")
    private UserStatus status;

    //relationship
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnoreProperties("roles")
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles = new HashSet<>();

    public void addRole(Role role) {
        roles.add(role);
        role.getUsers().add(this);
    }
}
