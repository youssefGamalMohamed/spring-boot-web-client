package com.youssefgamal.springbootwithwebclient.controllers;

import com.youssefgamal.springbootwithwebclient.models.Comment;
import com.youssefgamal.springbootwithwebclient.models.Post;
import com.youssefgamal.springbootwithwebclient.services.PostServiceIfc;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@Slf4j
@RestController
@AllArgsConstructor
public class PostController {


    private final PostServiceIfc postServiceIfc;


    @PostMapping("/api/v1/posts")
    public ResponseEntity<Post> save(@RequestBody Post requestBody) {
        log.info("save(): {}", requestBody);
        Post post = postServiceIfc.save(requestBody);
        log.info("save(): {}", post);
        return ResponseEntity.ok(post);
    }

    @GetMapping("/api/v1/posts")
    public ResponseEntity<Collection<Post>> findAll() {
        log.info("findAll(): Request GET /api/v1/posts");
        Collection<Post> posts = postServiceIfc.findAllPosts();
        log.info("findAll(): Response GET /api/v1/posts {}", posts);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/api/v1/posts/{id}")
    public ResponseEntity<Post> findById(@PathVariable Integer id) {
        log.info("findById(): Request GET /api/v1/posts/{}", id);
        Post post = postServiceIfc.findPostById(id);
        log.info("findById(): Request GET /api/v1/posts/{} Response: {}", id, post);
        return ResponseEntity.ok(post);
    }


    @GetMapping("/api/v1/posts/{id}/comments")
    public ResponseEntity<?> findCommentsByPostId(@PathVariable Integer id) {
        log.info("findCommentsByPostId(): Request GET /api/v1/posts/{}/comments", id);
        Collection<Comment> comments = postServiceIfc.findCommentsByPostId(id);
        log.info("findCommentsByPostId(): Request GET /api/v1/posts/{}/comments, Response: {}", id, comments);
        return ResponseEntity.ok(comments);
    }

    @DeleteMapping("/api/v1/posts/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Integer id) {
        log.info("deleteById(): Request DELETE /api/v1/posts/{}", id);
        postServiceIfc.deleteById(id);
        log.info("deleteById(): Request DELETE /api/v1/posts/{}", id);
        return ResponseEntity.noContent().build();
    }
}
