package com.pet.petmily.board.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChannelResponse<T> {

    private String success;
    private String message;
    private String channelName;
    private T data;
}