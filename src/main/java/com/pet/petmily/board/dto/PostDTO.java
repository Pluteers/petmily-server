package com.pet.petmily.board.dto;

import com.pet.petmily.board.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
    private long id;
    private String title;
    private String content;

    private int likePost;
    private long hit;
    private String imagePath;
    private String nickname;
    private String categoryName;
    public static PostDTO toDto(Post post) {
        return new PostDTO(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getLikePost(),
                post.getHit(),
                post.getImagePath(),
                post.getMember().getNickname(),
                post.getCategory().getCategoryName());
    }


}
