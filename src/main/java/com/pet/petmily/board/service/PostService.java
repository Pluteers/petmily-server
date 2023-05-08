package com.pet.petmily.board.service;

import com.pet.petmily.board.dto.ChannelDTO;
import com.pet.petmily.board.dto.PostDTO;
import com.pet.petmily.board.entity.Category;
import com.pet.petmily.board.entity.Channel;
import com.pet.petmily.board.entity.Post;
import com.pet.petmily.board.repository.CategoryRepository;
import com.pet.petmily.board.repository.ChannelRepository;
import com.pet.petmily.board.repository.PostRepository;
import com.pet.petmily.user.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final ChannelRepository channelRepository;

    //게시판 전체 조회
    @Transactional(readOnly = true)
    public List<PostDTO> getAllPost(Long channelId){
    List<Post> posts=postRepository.findAllByChannel_ChannelId(channelId);
    List<PostDTO> postDtos=new ArrayList<>();
    posts.forEach(s->postDtos.add(PostDTO.toDto(s)));


    return postDtos;

    }
    //개별 게시글 조회
    @Transactional(readOnly = true)
    public PostDTO getPost(Long id,Long channelId){
        Post post=postRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("해당 게시글이 없습니다. id="+id));
        PostDTO postDto=PostDTO.toDto(post);
        return PostDTO.toDto(post);
    }


    //게시글 작성
    @Transactional
    public PostDTO writePost(PostDTO postDto, Member member,ChannelDTO channelDTO){
        Post post =new Post();
        Channel channel=channelRepository.findById(channelDTO.getChannelId())
                .orElseThrow(()->new IllegalArgumentException("해당 채널이 없습니다. id="+channelDTO.getChannelId()));
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setMember(member);
        post.setImagePath(postDto.getImagePath());
        post.setStatus(true);

        post.setChannel(channel);
        post.setLikePost(0);
        post.setHit(1);
        postRepository.save(post);
        return postDto.toDto(post);
    }


}
