package com.example.springsecurityproject.service;

import com.example.springsecurityproject.dto.CommentDTO;
import com.example.springsecurityproject.dto.PostDTO;
import com.example.springsecurityproject.entity.CommentEntity;
import com.example.springsecurityproject.entity.PostEntity;
import com.example.springsecurityproject.entity.UserEntity;
import com.example.springsecurityproject.repository.CommentRepository;
import com.example.springsecurityproject.repository.PostRepository;
import com.example.springsecurityproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import javax.xml.stream.events.Comment;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;

    public void addPost(PostEntity postEntity){
        postRepository.save(postEntity);
    }

    public void editPost(PostDTO postDTO, Long id)
    {
        postRepository.editPost(postDTO.getTitle(), postDTO.getDescription(), id);
    }

    public void editComment(CommentDTO commentDTO, Long id)
    {
        postRepository.editComment(commentDTO.getText(), id);
    }
    public List<PostDTO> findAllPost(){

        List<PostEntity> listPost = postRepository.findAll();
        List<PostDTO> postDTO = new ArrayList<>();
        for (PostEntity post : listPost){
            postDTO.add(new PostDTO(post));
        }

        return postDTO;
    }

    public void deletePost(PostDTO postDTO){
        PostEntity originalPost = postRepository.getReferenceById(postDTO.getId());
        postRepository.deleteByPostId(originalPost.getId());
    }

    public void deleteComment(Long id){
        commentRepository.delete(commentRepository.getReferenceById(id));
    }
    public PostEntity findPostByIdEntity(Long id)
    {
        return postRepository.getReferenceById(id);
    }
    public PostDTO findPostById(Long id){
        Optional<PostEntity> listPostById = postRepository.findById(id);

        if (listPostById.isEmpty()){
            throw new IllegalStateException("Post not found");
        }

        PostEntity foundPost = listPostById.get();
        List<CommentDTO> commentDTO = commentRepository.getCommentEntitiesByPostId(listPostById.get().getId());

        return new PostDTO(foundPost, commentDTO);
    }

    public void addCommentToPost(Long postId, CommentEntity commentEntity){
        Optional<PostEntity> post = postRepository.findById(postId);

        if (post.isEmpty()){
            throw new IllegalStateException("Post not found - Comment function");
        }
        PostEntity foundPost = post.get();
        commentRepository.addCommentToPost(commentEntity.getText(), foundPost, commentEntity.getUser());
    }

    public CommentEntity getCommentEntityById(Long id){
        Optional<CommentEntity> commentFound = commentRepository.findById(id);

        if (commentFound.isEmpty()){
            throw new IllegalStateException("Comment not found");
        }
        else
            return commentFound.get();
    }
    public List<CommentDTO> findCommentsById(Long id){
        return commentRepository.getCommentEntitiesByPostId(id);
    }

}
