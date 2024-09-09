package com.youssefgamal.springbootwithwebclient.services;

import com.youssefgamal.springbootwithwebclient.models.Comment;
import com.youssefgamal.springbootwithwebclient.models.Post;

import java.util.Collection;

public interface PostServiceIfc {

    Collection<Post> findAllPosts();
    Post findPostById(Integer id);
    Collection<Comment> findCommentsByPostId(Integer id);
    void deleteById(Integer id);
    Post save(Post post);
}
