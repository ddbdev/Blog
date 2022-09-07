package com.example.springsecurityproject.repository;

import com.example.springsecurityproject.dto.PostDTO;
import com.example.springsecurityproject.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface PostRepository extends JpaRepository<PostEntity, Long> {

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE post_entity AS t SET t.title = ?1, t.description = ?2, t.updated_at = NOW() WHERE t.id = ?3 ")
    void editPost(String text, String description, Long id);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE comment_entity AS t SET t.text = ?1, t.updated_at = NOW() WHERE t.id = ?2 ")
    void editComment(String text, Long id);
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM post_entity WHERE id = ?1")
    void deleteByPostId(Long id);
}
