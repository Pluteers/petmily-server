package com.pet.petmily.board.controller;

import com.pet.petmily.board.dto.ChannelDTO;
import com.pet.petmily.board.dto.PostDTO;
import com.pet.petmily.board.response.Response;
import com.pet.petmily.board.service.ChannelService;
import com.pet.petmily.user.entity.Member;
import com.pet.petmily.user.repository.MemberRepository;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import com.pet.petmily.board.service.PostService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final ChannelService channelService;
    private final MemberRepository memberRepository;

    @ApiOperation(value = "게시판 전체 조회", notes = "게시판 전체 조회")
    @GetMapping("/channel/{channelId}/post")
    public Response getAllPost(@PathVariable("channelId") Long channelId) {
        log.info("게시판 전체 조회(채널별)");
        return new Response("조회 성공","채널별 전체 게시물 return",postService.getAllPost(channelId));
    }

    @ApiOperation(value = "게시판 개별 조회", notes = "해당 postId를 가지는 게시물 조회")
    @GetMapping("/channel/{channelId}/post/{id}")
    public Response getPost(@PathVariable("channelId") Long channelId,@PathVariable("id") Long id) {
        log.info("게시판 개별 조회");
        return new Response("조회 성공","채널별 개별 게시물 return",postService.getPost(channelId,id));
    }

    @ApiOperation(value = "채널 생성" , notes = "채널 생성")
    @PostMapping("/channel")
    public Response createChannel(@RequestBody ChannelDTO channelDTO){
        log.info("채널 생성");
        return new Response("채널 생성 성공","채널 생성 성공",channelService.createChannel(channelDTO));
    }
    @ApiOperation(value = "채널 조회" , notes = "채널 조회")
    @GetMapping("/channel")
    public Response getChannel(){
        log.info("채널 조회");
        return new Response("채널 조회 성공","채널 조회 성공",channelService.getChannel());
    }
    @ApiOperation(value = "게시판 작성", notes = "해당 channelId를 가진 채널에 게시물 작성")
    @PostMapping("/channel/{id}/post/write")
    public Response writePost(@PathVariable("id") Long channelId,@RequestBody PostDTO postDto, Authentication authentication) {
        UserDetails userDetails=(UserDetails)authentication.getPrincipal();
        Member member= Member.builder()
                        .id(memberRepository.findByEmail(userDetails.getUsername()).get().getId())
                        .email(userDetails.getUsername())
                        .nickname(memberRepository.findByEmail(userDetails.getUsername()).get().getNickname())
        .build();

        ChannelDTO channelDTO=channelService.getChannelById(channelId);



        log.info("작성완료\n");
        log.info("작성자 : "+member.getNickname());


        return new Response("작성 성공","게시물 작성 성공",postService.writePost(postDto,member,channelDTO));




    }



}
