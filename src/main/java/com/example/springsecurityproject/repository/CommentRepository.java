package com.example.springsecurityproject.repository;

import com.example.springsecurityproject.dto.CommentDTO;
import com.example.springsecurityproject.entity.CommentEntity;
import com.example.springsecurityproject.entity.PostEntity;
import com.example.springsecurityproject.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentDTO> getCommentEntitiesByPostId(Long id);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "INSERT INTO comment_entity (text,post_id,user_id,created_at) VALUES(?,?,?,NOW())")
    void addCommentToPost(String text, PostEntity post, UserEntity user);
}
