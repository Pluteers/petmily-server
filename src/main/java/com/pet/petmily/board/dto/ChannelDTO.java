package com.pet.petmily.board.dto;


import com.pet.petmily.board.entity.Channel;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j

public class ChannelDTO  {
    private long channelId;
    private String channelName;
    private String categoryName;
    private long categoryId;

    public static ChannelDTO toDto(Channel channel) {
        log.info("channelDTO todto메소드 호출");
        return new ChannelDTO(
                channel.getChannelId(),
                channel.getChannelName(),
                channel.getCategory().getCategoryName(),
                channel.getCategory().getCategoryId());
    }


}
