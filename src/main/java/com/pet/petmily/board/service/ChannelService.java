package com.pet.petmily.board.service;

import com.pet.petmily.board.dto.ChannelDTO;
import com.pet.petmily.board.entity.Channel;
import com.pet.petmily.board.repository.CategoryRepository;
import com.pet.petmily.board.repository.ChannelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class ChannelService {
    private final ChannelRepository channelRepository;
    private final CategoryRepository categoryRepository;
    public ChannelDTO getChannelById(Long channelId) {
        return ChannelDTO.toDto(channelRepository.findById(channelId).orElseThrow(()->new IllegalArgumentException("해당 채널이 없습니다. id="+channelId)));

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
    public ChannelDTO createChannel(ChannelDTO channelDto){
        Channel channel=new Channel();
        channel.setChannelName(channelDto.getChannelName());
        channel.setCategory(categoryRepository.findById(channelDto.getCategoryId())
                .orElseThrow(()->new IllegalArgumentException("해당 카테고리가 없습니다. id="+channelDto.getCategoryId())));
        channelRepository.save(channel);
        return ChannelDTO.toDto(channel);
    }

}
