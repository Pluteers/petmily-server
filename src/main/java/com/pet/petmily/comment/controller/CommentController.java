package com.pet.petmily.comment.controller;

import com.pet.petmily.board.entity.Post;
import com.pet.petmily.comment.dto.CommentDTO;
import com.pet.petmily.comment.entity.Comment;
import com.pet.petmily.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.ApiOperation;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    @ApiOperation(value = "댓글 등록")
    @PostMapping("/{postId}/{memberId}")
    public ResponseEntity<CommentDTO> addComment(@PathVariable Long postId, @PathVariable Long memberId, @RequestBody CommentDTO commentDTO) {
        try {
            CommentDTO createdComment = commentService.addComment(postId, memberId, commentDTO.getContent());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
        } catch (Exception e) {
            log.error("댓글 등록 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @ApiOperation(value = "댓글 삭제")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId) {
        try {
            commentService.deleteComment(commentId);
            return ResponseEntity.ok("댓글이 삭제되었습니다.");
        } catch (Exception e) {
            log.error("댓글 삭제 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("댓글 삭제 중 오류가 발생했습니다.");
        }
    }

    @ApiOperation(value = "댓글 수정")
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable Long commentId, @RequestBody CommentDTO commentDTO) {
        try {
            CommentDTO updatedComment = commentService.updateComment(commentId, commentDTO.getContent());
            return ResponseEntity.ok(updatedComment);
        } catch (Exception e) {
            log.error("댓글 수정 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @ApiOperation(value = "댓글 목록 조회")
    @GetMapping("/{postId}")
    public ResponseEntity<List<CommentDTO>> getCommentListByPost(@PathVariable Long postId) {
        try {
            List<CommentDTO> commentDTOList = commentService.getCommentListByPost(postId);
            return ResponseEntity.ok(commentDTOList);
        } catch (Exception e) {
            log.error("댓글 목록 조회 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @ApiOperation(value = "댓글 좋아요")
    @PostMapping("/{commentId}/like")
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