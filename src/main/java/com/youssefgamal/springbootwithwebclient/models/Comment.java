package com.youssefgamal.springbootwithwebclient.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    private Integer id;
    private Integer postId;
    private String name;
    private String email;
    private String body;
}
