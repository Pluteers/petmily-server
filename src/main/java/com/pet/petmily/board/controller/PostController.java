package com.pet.petmily.board.controller;

import com.pet.petmily.board.dto.ChannelDTO;
import com.pet.petmily.board.dto.PostDTO;
import com.pet.petmily.board.entity.Category;
import com.pet.petmily.board.repository.PostRepository;
import com.pet.petmily.board.response.Response;
import com.pet.petmily.user.auth.PrincipalDetails;
import com.pet.petmily.user.entity.Member;
import com.pet.petmily.user.repository.MemberRepository;
import com.pet.petmily.user.service.LoginService;
import com.pet.petmily.user.service.MemberService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import com.pet.petmily.board.service.PostService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final MemberRepository memberRepository;

    @ApiOperation(value = "게시판 전체 조회", notes = "게시판 전체 조회")
    @GetMapping("/post")
    public Response getPost() {
        log.info("게시판 전체 조회");
        return new Response("조회 성공","전체 게시물 return",postService.getPost());
    }

    @ApiOperation(value = "게시판 개별 조회", notes = "게시판 개별 조회")
    @GetMapping("/post/{id}")
    public Response getPost(@PathVariable("id") Long id) {
        log.info("게시판 개별 조회");
        return new Response("조회 성공","개별 게시물 return",postService.getPost(id));
    }

    @ApiOperation(value = "채널 생성" , notes = "채널 생성")
    @PostMapping("/post/channel")
    public Response createChannel(@RequestBody ChannelDTO channelDTO){
        log.info("채널 생성");
        return new Response("채널 생성 성공","채널 생성 성공",postService.createChannel(channelDTO));
    }
    @ApiOperation(value = "채널 조회" , notes = "채널 조회")
    @GetMapping("/post/channel")
    public Response getChannel(){
        log.info("채널 조회");
        return new Response("채널 조회 성공","채널 조회 성공",postService.getChannel());
    }
    @ApiOperation(value = "게시판 작성", notes = "게시판 작성")
    @PostMapping("/post/write")
    public Response writePost(@RequestBody PostDTO postDto, Authentication authentication) {
        UserDetails userDetails=(UserDetails)authentication.getPrincipal();
        Member member= Member.builder()
                        .id(memberRepository.findByEmail(userDetails.getUsername()).get().getId())
                        .email(userDetails.getUsername())
                        .nickname(memberRepository.findByEmail(userDetails.getUsername()).get().getNickname())


        .build();



        log.info("작성완료\n");
        log.info("작성자 : "+member.getNickname());


        return new Response("작성 성공","게시물 작성 성공",postService.writePost(postDto,member));




    }



}
