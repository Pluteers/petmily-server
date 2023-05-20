package com.pet.petmily.comment.dto;

import java.time.LocalDateTime;


import com.pet.petmily.comment.entity.Comment;
import com.pet.petmily.user.entity.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j

public class CommentDTO extends BaseTimeEntity {
    private Long commentId;
    private String content;
    private int commentLike;
    private LocalDateTime createDate;
    private LocalDateTime lastModifiedDate;
    private Long postId;
    private Long memberId;

    public static CommentDTO toDto(Comment comment) {
        log.info("CommentDTO toDto 메소드");

        return new CommentDTO(
                comment.getCommentId(),
                comment.getContent(),
                comment.getCommentLike(),
                comment.getCreateDate(),
                comment.getLastModifiedDate(),

                comment.getPost().getPostId(),
                comment.getMember().getId()
        );
    }

}
