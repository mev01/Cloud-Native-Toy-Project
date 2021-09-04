package com.web.curation.message;

import com.web.curation.member.Member;
import com.web.curation.team.Team;

public class MessageAdapter {
    public static Message dtoToEntity(MessageDto messageDto) {
        return new Message.MessageBuilder()
                .messageId(messageDto.getMessageId())
                .team(new Team(messageDto.getTeamId()))
                .member(new Member(messageDto.getMemberId()))
                .contents(messageDto.getContents())
                .writeDate(messageDto.getWriteDate())
                .writer(messageDto.getWriter())
                .build();
    }

    public static MessageDto entityToDto(Message message) {
        return new MessageDto.MessageDtoBuilder()
                .messageId(message.getMessageId())
                .teamId(message.getTeam().getTeamId())
                .memberId(message.getMember().getMemberId())
                .contents(message.getContents())
                .writeDate(message.getWriteDate())
                .writer(message.getWriter())
                .build();
    }
}
