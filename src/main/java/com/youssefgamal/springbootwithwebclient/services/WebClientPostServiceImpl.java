package com.youssefgamal.springbootwithwebclient.services;

import com.youssefgamal.springbootwithwebclient.models.Comment;
import com.youssefgamal.springbootwithwebclient.models.Post;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.Collection;
import java.util.NoSuchElementException;

@Slf4j
@Service
public class WebClientPostServiceImpl implements PostServiceIfc {


    @Value("${integrations.backend-services.json-placeholder.base-url}")
    private String JSON_PLACEHOLDER_BASE_URL;

    @Value("${integrations.backend-services.json-placeholder.services.posts}")
    private String POSTS_RESOURCE;

    @Value("${integrations.backend-services.json-placeholder.services.comments}")
    private String COMMENTS_RESOURCE;

    private WebClient webClient;


    @PostConstruct
    public void init() {
        this.webClient = WebClient.builder()
                .baseUrl(JSON_PLACEHOLDER_BASE_URL)
                .build();
    }


    @Override
    public Collection<Post> findAllPosts() {
        log.info("findAllPosts()");
        Flux<Post> postsFlux = webClient.get()
                .uri(POSTS_RESOURCE)
                .retrieve()
                .bodyToFlux(Post.class);

        return postsFlux.collectList().block();
    }

    @Override
    public Post findPostById(Integer id) {
        log.info("findPostById({})", id);
        Mono<Post> postMono = webClient.get()
                .uri(POSTS_RESOURCE + "/{id}", id)
                .retrieve()
                .onStatus(httpStatusCode -> httpStatusCode.value() == HttpStatus.NOT_FOUND.value(), clientResponse -> {
                    String exceptionMsg = String.format("No resource found at %s/%d", POSTS_RESOURCE, id);
                    log.error(exceptionMsg);
                    return Mono.error(new NoSuchElementException(exceptionMsg));
                })
                .bodyToMono(Post.class)
                .onErrorResume(Exception.class, e -> Mono.empty()); // Return an empty collection on error;

        Post post = postMono.block();
        if(post == null)
            throw new NoSuchElementException(String.format("No resource found at %s/%d", POSTS_RESOURCE, id));
        return post;
    }

    @Override
    public Collection<Comment> findCommentsByPostId(Integer id) {
        log.info("findCommentsByPostId({})", id);

        return webClient.get()
                .uri(POSTS_RESOURCE + "/{id}" + COMMENTS_RESOURCE, id)
                .retrieve()
                .onStatus(httpStatusCode -> httpStatusCode == HttpStatus.NOT_FOUND, clientResponse -> {
                    // Handle 404 Not Found
                    String exceptionMsg = String.format("Post with id %d not found.", id);
                    log.error(exceptionMsg);
                    return Mono.error(new NoSuchElementException(exceptionMsg));
                })
                .bodyToFlux(Comment.class)
                .collectList() // Convert the Flux<Comment> to a List<Comment>
                .block(); // Blocking here since your return type is not reactive
    }

    @Override
    public void deleteById(Integer id) {
        log.info("start of deleteById({})", id);

        Mono<Post> deletedPost = webClient.delete()
                .uri(POSTS_RESOURCE + "/{id}", id)
                .retrieve()
                .onStatus(httpStatusCode -> httpStatusCode == HttpStatus.NOT_FOUND, clientResponse -> {
                    // Handle 404 Not Found
                    String exceptionMsg = String.format("Post with id %d not found.", id);
                    log.error(exceptionMsg);
                    return Mono.error(new NoSuchElementException(exceptionMsg));
                })
                .bodyToMono(Post.class);
        log.info("response of deleteById({}): {}", id, deletedPost.block());
        log.info("end of deleteById({})", id);
    }

    @Override
    public Post save(Post post) {
        log.info("start of save({})", post);

        Mono<Post> savedPost = webClient.post()
                .uri(POSTS_RESOURCE)
                .body(Mono.just(post), Post.class)
                .retrieve()
                .bodyToMono(Post.class);
        Post responsePost = savedPost.block();
        log.info("response of save(): {}", responsePost);
        return responsePost;
    }


}
