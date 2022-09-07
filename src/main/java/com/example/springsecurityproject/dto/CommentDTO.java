package com.example.springsecurityproject.dto;

import com.example.springsecurityproject.entity.CommentEntity;
import com.example.springsecurityproject.entity.PostEntity;
import com.example.springsecurityproject.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {

    private Long id;
    private String text;
    private String user;
    @JsonIgnore
    private PostEntity post;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CommentDTO(CommentEntity commentEntity) {
        this.id = commentEntity.getId();
        this.text = commentEntity.getText();
        this.user = commentEntity.getUser().getUsername();
        this.createdAt = commentEntity.getCreatedAt();
        this.updatedAt = commentEntity.getUpdatedAt();
    }
    public CommentDTO(CommentEntity commentEntity, PostEntity postEntity) {
        this.id = commentEntity.getId();
        this.text = commentEntity.getText();
        this.user = commentEntity.getUser().getUsername();
        this.post = postEntity;
        this.createdAt = commentEntity.getCreatedAt();
        this.updatedAt = commentEntity.getUpdatedAt();
    }
}
