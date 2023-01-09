package com.instagramclone.entity.user;

import com.instagramclone.entity.post.Comment;
import com.instagramclone.entity.post.Like;
import com.instagramclone.entity.post.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = "phoneNumber",name = "user_phoneNumber_unique"),
        @UniqueConstraint(columnNames = "username",name = "user_username_unique"),
        @UniqueConstraint(columnNames = "email",name = "user_email_unique")})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String fullName;

    @Column(columnDefinition = "TEXT", nullable = true)
    private String phoneNumber;

    @Column(columnDefinition = "TEXT" ,nullable = false)
    private String username;

    @Column(columnDefinition = "TEXT", nullable = true)
    private String email;

    private LocalDateTime birthDate;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String password;

    @Column(columnDefinition = "TEXT", nullable = true)
    private String currentProfilePicUrl = "http://localhost:9090/images/avatar.jpg";

    @Column(columnDefinition = "TEXT", nullable = true)
    private String profileId;

    @Column(columnDefinition = "BOOLEAN", nullable = false)
    private Boolean hasProfile = false;

    @CreationTimestamp
    private LocalDateTime createdAt = LocalDateTime.now();

    @UpdateTimestamp
    private LocalDateTime updatedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Like> likes;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Following> followings;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Followers> followers;

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id",referencedColumnName = "id"),
            inverseJoinColumns=@JoinColumn(name="role_id",referencedColumnName = "id"))
    public Set<Role> roles;

}