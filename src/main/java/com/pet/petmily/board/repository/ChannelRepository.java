package com.pet.petmily.board.repository;

import com.pet.petmily.board.entity.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, Long> {
    Channel findByChannelName(String channelName);
}
