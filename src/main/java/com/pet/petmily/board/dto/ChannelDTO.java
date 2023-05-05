package com.pet.petmily.board.dto;


import com.pet.petmily.board.entity.Channel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j

public class ChannelDTO  {

    @ApiModelProperty(value = "채널 아이디", example = "채널 아이디입니다.")
    private long channelId;
    @ApiModelProperty(value = "채널 이름", example = "채널 이름입니다.")
    private String channelName;
    @ApiModelProperty(value = "카테고리 이름", example = "카테고리 이름입니다.")
    private String categoryName;
    @ApiModelProperty(value = "카테고리 아이디", example = "카테고리 아이디입니다.")
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
