package com.pet.petmily.board.dto;

import com.pet.petmily.board.entity.Channel;
import com.pet.petmily.board.entity.Post;
import com.pet.petmily.user.entity.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO extends BaseTimeEntity {
    private long id;
    private String title;
    private String content;
    private int likePost;
    private long hit;
    private String imagePath;
    private String nickname;

    private Long channelId;
    private String channelName;
    private LocalDateTime createDate;
    private LocalDateTime lastModifiedDate;
    public static PostDTO toDto(Post post) {
        
        log.info("postDTO todto메소드 호출");
        return new PostDTO(
                
                post.getPostId(),
                post.getTitle(),
                post.getContent(),
                post.getLikePost(),
                post.getHit(),
                post.getImagePath(),
                post.getMember().getNickname(),

                post.getChannel().getChannelId(),
                post.getChannel().getChannelName(),
                post.getCreateDate(),
                post.getLastModifiedDate());
    }


}
