package com.example.springsecurityproject.dto;

import com.example.springsecurityproject.entity.PostEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {

    private Long id;
    private String title;
    private String description;
    private String user;

    private List<CommentDTO> comments;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt = null;

    public PostDTO (PostEntity postEntity){
        this.id = postEntity.getId();
        this.title = postEntity.getTitle();
        this.description= postEntity.getDescription();
        this.user = postEntity.getUser().getUsername();
        this.createdAt = postEntity.getCreatedAt();
        this.updatedAt = postEntity.getUpdatedAt();
    }

    public PostDTO(PostEntity postEntity, List<CommentDTO> comments){
        this.id = postEntity.getId();
        this.title = postEntity.getTitle();
        this.description= postEntity.getDescription();
        this.user = postEntity.getUser().getUsername();
        this.comments = comments;
        this.createdAt = postEntity.getCreatedAt();
        this.updatedAt = postEntity.getUpdatedAt();
    }
}
