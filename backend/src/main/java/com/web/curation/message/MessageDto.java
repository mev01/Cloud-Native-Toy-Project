package com.web.curation.message;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MessageDto {
    private int messageId;
    private int teamId;
    private int memberId;
    private String contents;
    private LocalDateTime writeDate;
    private String writer;
}