package com.instagramclone.respository.post;

import com.instagramclone.entity.post.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findByPostId(long postId);
    List<Comment> findByUserId(long userId);
}
