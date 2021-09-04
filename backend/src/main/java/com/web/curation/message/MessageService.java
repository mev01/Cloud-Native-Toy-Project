package com.web.curation.message;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.web.curation.team.Team;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MessageService {
    private MessageDao messageDao;

    public MessageDto insertMessage(MessageDto messageDto) {
        Message message = messageDao.save(MessageAdapter.dtoToEntity(messageDto));
        return MessageAdapter.entityToDto(message);
    }

    public List<MessageDto> getMessages(int teamId, int pageNum) {
        PageRequest pageRequest = PageRequest.of(pageNum, 10, Sort.by("messageId").descending());
        List<Message> messageList = messageDao.findByTeam(new Team(teamId), pageRequest);
        List<MessageDto> messageDtoList = new ArrayList<>();

        for (Message m : messageList) {
            messageDtoList.add(MessageAdapter.entityToDto(m));
        }

        return messageDtoList;
    }
}
