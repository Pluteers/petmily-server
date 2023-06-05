package com.pet.petmily.comment.repository;

import com.pet.petmily.board.entity.Post;
import com.pet.petmily.comment.entity.Comment;
import com.pet.petmily.user.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostOrderByCreateDateAsc(Post post);

    List<Comment> findByMemberOrderByCreateDateAsc(Member member);
}
