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
import java.util.Optional;

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
    public PostDTO getPost(Long channelId,Long id){

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다=" + id));
        if(post.getChannel().getChannelId()!=channelId){
            throw new IllegalArgumentException("해당 게시글이 없습니다=" + id);
        }
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


    public boolean isWriter(Long postId, long memberId) {
        Optional<Post> postOptional=postRepository.findById(postId);
        if(postOptional.isPresent()){
            Post post=postOptional.get();
            return post.getMember().getId()==memberId;
            }
        // Handle the case when the post is not found
        throw new IllegalArgumentException("Post not found. postId=" + postId);


    }

    public PostDTO updatePost(Long channelId, Long id, PostDTO postDto) {
        Optional<Post>postOptional=postRepository.findById(id);
        if(postOptional.isPresent()){
            Post post=postOptional.get();
            if(post.getChannel().getChannelId()!=channelId){
                throw new IllegalArgumentException("해당 게시글이 없습니다=" + id);
            }
            post.setTitle(postDto.getTitle());
            post.setContent(postDto.getContent());
            post.setImagePath(postDto.getImagePath());
            postRepository.save(post);
            return postDto.toDto(post);
        }
        else{
            throw new IllegalArgumentException("해당 게시글이 없습니다=" + id);
        }

    }
}
