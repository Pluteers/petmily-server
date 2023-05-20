package com.pet.petmily.comment.service;

import com.pet.petmily.board.entity.Post;
import com.pet.petmily.comment.entity.Comment;
import com.pet.petmily.user.entity.Member;
import com.pet.petmily.comment.dto.CommentDTO;
import com.pet.petmily.user.repository.MemberRepository;
import com.pet.petmily.comment.repository.CommentRepository;
import com.pet.petmily.board.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;


    public CommentDTO addComment(Long postId, Long memberId, String content) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다." + postId));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 회원입니다. " + memberId));
        Comment comment = new Comment();
        comment.setPost(post);
        comment.setStatus(true);
        comment.setMember(member);
        comment.setContent(content);
        commentRepository.save(comment);
        return CommentDTO.toDto(comment);
    }

    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("없는 댓글입니다." + commentId));
        commentRepository.delete(comment);
    }

    public CommentDTO updateComment(Long commentId, String content) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("없는 댓글입니다." + commentId));
        comment.setContent(content);
        commentRepository.save(comment);
        return CommentDTO.toDto(comment);
    }

    public List<CommentDTO> getCommentListByPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("없는 댓글입니다." + postId));
        List<Comment> comments = commentRepository.findByPostOrderByCreateDateAsc(post);
        return comments.stream().map(CommentDTO::toDto).collect(Collectors.toList());
    }
    public void likeComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("없는 댓글입니다." + commentId));
        comment.setCommentLike(comment.getCommentLike() + 1);
        commentRepository.save(comment);
    }
}
