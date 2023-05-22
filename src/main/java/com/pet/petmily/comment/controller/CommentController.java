package com.pet.petmily.comment.controller;

import com.pet.petmily.board.entity.Post;
import com.pet.petmily.comment.dto.CommentDTO;
import com.pet.petmily.comment.entity.Comment;
import com.pet.petmily.comment.service.CommentService;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @ApiOperation(value = "댓글 목록 조회", notes = "게시물의 댓글 목록 조회")
    @GetMapping("/post/{postId}/comment")
    public ResponseEntity<List<CommentDTO>> getCommentListByPost(@PathVariable Long postId) {
        try {
            List<CommentDTO> commentDTOList = commentService.getCommentListByPost(postId);
            log.info("댓글 목록 조회 성공");
            return ResponseEntity.ok()
                    .body(commentDTOList);
        } catch (Exception e) {
            log.error("댓글 목록 조회 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }


    @ApiOperation(value = "댓글 등록", notes = "게시물에 댓글 등록")
    @PostMapping("/post/{postId}/comment/add")
    public ResponseEntity<String> addComment(@PathVariable("postId") Long postId, @RequestBody CommentDTO commentDTO) {
        try {
            CommentDTO createdComment = commentService.addComment(postId, commentDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("댓글을 등록하였습니다.");
        } catch (Exception e) {
            log.error("댓글 등록 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("댓글 등록에 실패했습니다.");
        }
    }

    @ApiOperation(value = "댓글 삭제", notes = "댓글 삭제")
    @DeleteMapping("comment/{commentId}/delete")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId) {
        try {
            commentService.deleteComment(commentId);
            return ResponseEntity.ok("댓글이 삭제되었습니다.");
        } catch (IllegalArgumentException e) {
            log.error("댓글 삭제 실패", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("없는 댓글입니다.");
        } catch (Exception e) {
            log.error("댓글 삭제를 실패했습니다.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("댓글 삭제 중 오류가 발생했습니다.");
        }
    }

    @ApiOperation(value = "댓글 수정", notes = "댓글 수정")
    @PutMapping("comment/{commentId}/update")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable Long commentId, @RequestBody CommentDTO commentDTO) {
        try {
            CommentDTO updatedComment = commentService.updateComment(commentId, commentDTO.getContent());
            return ResponseEntity.ok(updatedComment);
        } catch (Exception e) {
            log.error("댓글 수정을 실패했습니다.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @ApiOperation(value = "댓글 좋아요", notes = "댓글 좋아요 추가")
    @PostMapping("comment/{commentId}/like")
    public ResponseEntity<String> likeComment(@PathVariable Long commentId) {
        try {
            commentService.likeComment(commentId);
            return ResponseEntity.ok("댓글에 좋아요를 표시했습니다.");
        } catch (Exception e) {
            log.error("댓글 좋아요 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("댓글 좋아요 표시 중 오류가 발생했습니다.");
        }
    }
}
