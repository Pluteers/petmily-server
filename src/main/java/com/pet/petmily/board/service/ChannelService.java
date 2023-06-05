package com.pet.petmily.board.service;

import com.pet.petmily.board.dto.ChannelDTO;
import com.pet.petmily.board.entity.Channel;
import com.pet.petmily.board.entity.Post;
import com.pet.petmily.board.repository.CategoryRepository;
import com.pet.petmily.board.repository.ChannelRepository;
import com.pet.petmily.board.repository.PostRepository;
import com.pet.petmily.user.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class ChannelService {
    private final MemberRepository memberRepository;
    private final ChannelRepository channelRepository;
    private final CategoryRepository categoryRepository;
    private final PostRepository postRepository;
    public ChannelDTO getChannelById(Long channelId) {
        return ChannelDTO.toDto(channelRepository.findById(channelId).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"채널이 없습니다.")));

    }
    //채널 조회
    public List<ChannelDTO> getChannel(){
        List<Channel> channels=channelRepository.findAll();
        List<ChannelDTO> channelDtos= new ArrayList<>();
        channels.forEach(s->channelDtos.add(ChannelDTO.toDto(s)));
        return channelDtos;
    }
    //채널 생성
    @Transactional
    public ChannelDTO createChannel(ChannelDTO channelDto,Long memberId){
        Channel channel=new Channel();
        channel.setChannelName(channelDto.getChannelName());
        channel.setCategory(categoryRepository.findById(channelDto.getCategoryId())
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"해당 카테고리가 없습니다: "+channelDto.getCategoryId())));
        channel.setMember(memberRepository.findById(memberId)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"해당 유저가 없습니다.: "+memberId)));
        channelRepository.save(channel);
        return ChannelDTO.toDto(channel);
    }

    //채널 삭제
    public Object deleteChannel(Long channelId, long memberId) {
        Optional<Channel> channelOptional = channelRepository.findById(channelId);
        if (channelOptional.isPresent()) {
            Channel channel = channelOptional.get();

            // 채널을 작성한 유저인지 체크
            if (channel.getMember().getId()==memberId) {
                // 채널에 있는 모든 게시글을 가져온다
                List<Post> posts = postRepository.findAllByChannel_ChannelId(channelId);

                // 채널에 있는 모든 게시글을 삭제한다
                postRepository.deleteAll(posts);

                // 채널을 삭제한다
                channelRepository.deleteById(channelId);

                return "채널 삭제 성공";
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,"당신은 채널 크리에이터가 아닙니다");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"채널을 찾을 수 없습니다. channelId=" + channelId);
        }
    }


    public Object updateChannel(Long channelId, ChannelDTO channelDTO, long id) {
        Optional<Channel> channelOptional = channelRepository.findById(channelId);
        if (channelOptional.isPresent()) {
            Channel channel = channelOptional.get();

            // 채널을 작성한 유저인지 체크
            if (channel.getMember().getId()==id) {
                channel.setChannelName(channelDTO.getChannelName());
                channel.setCategory(categoryRepository.findById(channelDTO.getCategoryId())
                        .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"해당 카테고리가 없습니다: "+channelDTO.getCategoryId())));
                channelRepository.save(channel);

                return ChannelDTO.toDto(channel);
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,"당신은 채널 크리에이터가 아닙니다");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"채널을 찾을 수 없습니다. channelId=" + channelId);
        }
    }

    public boolean isWriter(Long channelId, long id) {
        Optional<Channel> channelOptional = channelRepository.findById(channelId);
        if (channelOptional.isPresent()) {
            Channel channel = channelOptional.get();

            // 채널을 작성한 유저인지 체크
            if (channel.getMember().getId()==id) {
                return true;
            } else {
                return false;
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"채널을 찾을 수 없습니다. channelId=" + channelId);
        }
    }
}

