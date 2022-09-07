package com.example.springsecurityproject.controller;

import com.example.springsecurityproject.dto.CommentDTO;
import com.example.springsecurityproject.dto.PostDTO;
import com.example.springsecurityproject.entity.CommentEntity;
import com.example.springsecurityproject.entity.PostEntity;
import com.example.springsecurityproject.entity.UserEntity;
import com.example.springsecurityproject.jwt.UsernameAndPasswordAuthenticationRequest;
import com.example.springsecurityproject.service.PostService;
import com.example.springsecurityproject.service.UserService;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@RestController
@Slf4j
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<PostDTO>> showAllPost() {
        return ResponseEntity.ok().body(postService.findAllPost());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> showPostById(@PathVariable("id") Long id){
        return ResponseEntity.ok().body(postService.findPostById(id));
    }

    @PostMapping(value = "/new")
    public ResponseEntity<String> newPost(
            @RequestBody FormForPost form,
            @CurrentSecurityContext(expression="authentication?.name") String username
    ){
        PostEntity newPost = new PostEntity();
        newPost.setTitle(form.getTitle());
        newPost.setDescription(form.getDescription());

        newPost.setUser((UserEntity) userService.loadUserByUsername(username));
        postService.addPost(newPost);

        return ResponseEntity.ok().body("Post creato con id " + newPost.getId());
    }

    @PutMapping("{id}")
    public ResponseEntity<String> editPostById(@PathVariable("id") Long id, @RequestBody PostDTO postDTO){
        PostDTO originalPost = postService.findPostById(id);

        originalPost.setTitle(
                postDTO.getTitle() == null
                ? originalPost.getTitle()
                : postDTO.getTitle()
        );

        originalPost.setDescription(
                postDTO.getDescription() == null
                ? originalPost.getDescription()
                : postDTO.getDescription()
        );
        postService.editPost(originalPost, id);
        return ResponseEntity.ok().body("Modifica effettuata");
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable("id") Long id){
        PostDTO postDTO = postService.findPostById(id);
        postService.deletePost(postDTO);
        return ResponseEntity.ok().body("Post con id " + id + " eliminato");

    }


    @PostMapping(value = "/{postId}", consumes = {"application/json"})
    public ResponseEntity<String> addCommentToPost(
            @PathVariable("postId") Long id,
            @RequestBody CommentDTO commentDTO,
            @CurrentSecurityContext(expression="authentication?.name") String username){

        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setUser((UserEntity) userService.loadUserByUsername(username));
        commentEntity.setPost(postService.findPostByIdEntity(id));
        commentEntity.setText(commentDTO.getText());
        postService.addCommentToPost(id, commentEntity);

        return ResponseEntity.ok().body(username + " ha aggiunto al post con id " + id + " il commento.");
    }
    @PutMapping("/{postId}/comment")
    public ResponseEntity<String> editComment(
            @PathVariable("postId") Long postId,
            @RequestParam("edit") Long commentId,
            @RequestBody CommentDTO commentDTO,
            @CurrentSecurityContext(expression = "authentication?.name") String username,
            @CurrentSecurityContext(expression = "authentication")  Authentication authentication){

        CommentEntity commentEntity = postService.getCommentEntityById(commentId);
        UserEntity user = (UserEntity) userService.loadUserByUsername(username);
        if (
                commentEntity.getUser().equals(user) ||
                authentication.getAuthorities()
                        .stream()
                        .anyMatch(userCheck -> userCheck.getAuthority().equalsIgnoreCase("blog:write")))
            postService.editComment(commentDTO, commentId);
        else
            return ResponseEntity.status(403).body("Non sei autorizzato a modificare questo commento");

        return ResponseEntity.ok().body("Commento modificato");
    }

    @DeleteMapping("/{postId}/comment/{commentId}")
    public ResponseEntity<String> deleteComment(
            @PathVariable("postId") Long postId,
            @PathVariable("commentId") Long commentId
    ){
        postService.deleteComment(commentId);
        return ResponseEntity.ok().body("Commento con id " + commentId + " eliminato");
    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class FormForPost {
        private String title;
        private String description;
    }

}
