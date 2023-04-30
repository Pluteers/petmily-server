package com.pet.petmily.board.controller;

import com.pet.petmily.board.repository.PostRepository;
import com.pet.petmily.board.response.Response;
import com.pet.petmily.user.repository.MemberRepository;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import com.pet.petmily.board.service.PostService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final MemberRepository memberRepository;

    @ApiOperation(value = "게시판 전체 조회", notes = "게시판 전체 조회")
    @GetMapping("/post")
    public Response getPost() {
        return new Response("조회 성공","전체 게시물 return",postService.getPost());
    }



}
