package com.pet.petmily.board.controller;

import com.pet.petmily.board.dto.ChannelDTO;
import com.pet.petmily.board.dto.PostDTO;
import com.pet.petmily.board.repository.ChannelRepository;
import com.pet.petmily.board.response.ChannelResponse;
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
import java.util.Optional;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PostController {
    private final ChannelRepository channelRepository;
    private final PostService postService;
    private final ChannelService channelService;
    private final MemberRepository memberRepository;

    @ApiOperation(value = "게시판 전체 조회", notes = "게시판 전체 조회")
    @GetMapping("/channel/{channelId}/post")
    public ChannelResponse getAllPost(@PathVariable("channelId") Long channelId) {
        log.info("게시판 전체 조회(채널별)");
        return new ChannelResponse(
                "조회 성공","채널별 전체 게시물 return",channelService.getChannelById(channelId).getChannelName()
                ,channelRepository.findById(channelId).get().getMember().getNickname()
                ,postService.getAllPost(channelId));
    }

    @ApiOperation(value = "게시판 개별 조회", notes = "해당 postId를 가지는 게시물 조회")
    @GetMapping("/channel/{channelId}/post/{id}")
    public ChannelResponse getPost(@PathVariable("channelId") Long channelId,@PathVariable("id") Long id) {
        log.info("게시판 개별 조회");

        return new ChannelResponse("조회 성공","채널별 개별 게시물 return",channelService.getChannelById(channelId).getChannelName()
                ,channelRepository.findById(channelId).get().getMember().getNickname()
                ,postService.getPost(channelId,id));
    }

    @ApiOperation(value = "채널 생성" , notes = "채널 생성")
    @PostMapping("/channel")
    public ChannelResponse createChannel(@RequestBody ChannelDTO channelDTO, Authentication authentication) {
        log.info("채널 생성");
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<Member> memberOptional = memberRepository.findByEmail(userDetails.getUsername());
        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            return new ChannelResponse("채널 생성 성공", "채널 생성 성공", channelDTO.getChannelName()
                    , member.getNickname()
                    , channelService.createChannel(channelDTO, member.getId()));

        }
        else {
            // Handle the case when member is not found
            return new ChannelResponse(
                    "채널 생성 에러",
                    "유저가 존재하지 않습니다",
                    channelDTO.getChannelName(),
                    null,null
            );
        }

    }
    @ApiOperation(value = "채널 수정" , notes = "채널 수정")
    @PutMapping("/channel/update/{id}")
    public Response updateChannel(@PathVariable("id") Long channelId, @RequestBody ChannelDTO channelDTO, Authentication authentication) {
        log.info("채널 수정");
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<Member> memberOptional = memberRepository.findByEmail(userDetails.getUsername());

        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            boolean isWriter = channelService.isWriter(channelId, member.getId());
            if(!isWriter)
                return new Response("채널 수정 에러", "당신은 채널 크리에이터가 아닙니다", null);
            else
                return new Response("채널 수정 성공", "채널 수정 성공", channelService.updateChannel(channelId, channelDTO, member.getId()));
        }
        else {
            // Handle the case when member is not found
            return new Response(
                    "채널 수정 에러",
                    "유저가 존재하지 않습니다",
                    null
            );
        }
    }
    @ApiOperation(value = "채널 삭제" , notes = "채널 삭제")
    @DeleteMapping("/channel/delete/{id}")
    public Response deleteChannel(@PathVariable("id") Long channelId, Authentication authentication) {
        log.info("채널 삭제");
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<Member> memberOptional = memberRepository.findByEmail(userDetails.getUsername());
        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            boolean isWriter = channelService.isWriter(channelId, member.getId());
            if(!isWriter)
                return new Response("채널 삭제 에러", "당신은 채널 크리에이터가 아닙니다", null);
            else
                return new Response("채널 삭제 성공", "채널 삭제 성공", channelService.deleteChannel(channelId, member.getId()));

        }
        else {
            // Handle the case when member is not found
            return new Response(
                    "채널 삭제 에러",
                    "유저가 존재하지 않습니다",
                    null
            );
        }
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
    @ApiOperation(value = "게시판 수정", notes = "해당 postId를 가진 게시물 수정")
    @PutMapping("/channel/{channelId}/post/update/{id}")
    public Response updatePost(@PathVariable("channelId") Long channelId,@PathVariable("id") Long id,@RequestBody PostDTO postDto,Authentication authentication) {
        UserDetails userDetails=(UserDetails)authentication.getPrincipal();
        Optional<Member> memberOptional = memberRepository.findByEmail(userDetails.getUsername());
        if(memberOptional.isPresent()) {
            Member member = memberOptional.get();
            boolean isWriter = postService.isWriter(id, member.getId());
            if(isWriter){
                log.info("게시판 수정 성공");
                return new Response("수정 성공","게시물 수정 성공",postService.updatePost(channelId,id,postDto));
            }
            else{
                log.info("게시판 수정 실패");
                return new Response("수정 실패","이 글의 작성자가 아닙니다",null);
            }
        }
        else {
            // 존재하지 않는 유저
            return new Response(
                    "수정 에러",
                    "유저가 존재하지 않습니다",
                    null
            );
        }



    }
    @ApiOperation(value = "게시판 삭제", notes = "해당 postId를 가진 게시물 삭제")
    @DeleteMapping("/channel/{channelId}/post/delete/{id}")
    public Response deletePost(@PathVariable("channelId") Long channelId,@PathVariable("id") Long id,Authentication authentication) {
        UserDetails userDetails=(UserDetails)authentication.getPrincipal();
        Optional<Member> memberOptional = memberRepository.findByEmail(userDetails.getUsername());
        if(memberOptional.isPresent()) {
            Member member = memberOptional.get();
            boolean isWriter = postService.isWriter(id, member.getId());
            if(isWriter){
                log.info("게시판 삭제 성공");
                return new Response("삭제 성공","게시물 삭제 성공",postService.deletePost(channelId,id));
            }
            else{
                log.info("게시판 삭제 실패");
                return new Response("삭제 실패","이 글의 작성자가 아닙니다",null);
            }
        }
        else {
            // 존재하지 않는 유저
            return new Response(
                    "삭제 에러",
                    "유저가 존재하지 않습니다",
                    null
            );
        }




        }

    @ApiOperation(value= "좋아요 클릭" ,notes = "해당 postId를 가진 게시물에 좋아요 클릭")
    @PostMapping("/channel/{channelId}/post/{postId}/like")
    public Response likePost(@PathVariable("channelId") Long channelId,@PathVariable("postId") Long postId){
        return new Response("좋아요 api 수행","아래의 메세지를 확인해주세요",postService.likePost(channelId,postId));



        }




}

