package com.pet.petmily.comment.dto;

import java.time.LocalDateTime;


import com.pet.petmily.comment.entity.Comment;
import com.pet.petmily.user.entity.BaseTimeEntity;
import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty(value = "댓글", example = "댓글내용")
    private String content;
    @ApiModelProperty(value = "좋아요", example = "좋아요 수")
    private int commentLike;
    @ApiModelProperty(value = "생성일자", example = "댓글 작성 시간")
    private LocalDateTime createDate;
    @ApiModelProperty(value = "수정일자", example = "댓글 수정 시간")
    private LocalDateTime lastModifiedDate;
    @ApiModelProperty(value = "유저 닉네임", example = "유저 닉네임")
    private String nickname;
    @ApiModelProperty(value = "게시글 아이디", example = "게시글 아이디")
    private Long postId;
    @ApiModelProperty(value = "유저 아이디", example = "유저 아이디")
    private Long memberId;

    public static CommentDTO toDto(Comment comment) {
        log.info("CommentDTO toDto 메소드");

        return new CommentDTO(
                comment.getCommentId(),
                comment.getContent(),
                comment.getCommentLike(),
                comment.getCreateDate(),
                comment.getLastModifiedDate(),
                comment.getMember().getNickname(),

                comment.getPost().getPostId(),
                comment.getMember().getId()
        );
    }

}
