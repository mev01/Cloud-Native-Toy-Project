package com.web.curation.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;

@Slf4j
public class MyChannelInterceptor implements ChannelInterceptor {

    @Override
    public Message preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        StompCommand command = accessor.getCommand();
        if (command != null && command.compareTo(StompCommand.SUBSCRIBE) == 0) {
            String destination = accessor.getDestination();
            log.info("구독 주소 : " + destination);
            log.info(message.toString());
        } else if (command.compareTo(StompCommand.CONNECT) == 0) {
            log.info("사용자 연결");
        } else if (command.compareTo(StompCommand.DISCONNECT) == 0) {
            log.info("사용자 연결 해제");
        }
        return message;
    }
}
