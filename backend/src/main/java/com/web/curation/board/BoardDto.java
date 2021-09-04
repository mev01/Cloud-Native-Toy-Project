package com.web.curation.board;

import com.web.curation.team.Team;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardDto {
    private int boardId;
    @NotEmpty
    private String title;
    @NotEmpty
    private String contents;
    private LocalDateTime writeDate;
    private String writer;
    private int teamId;
    private Team team;

    public Board toEntity() {
        return Board.builder()
                .title(this.title)
                .contents(this.contents)
                .writer(this.writer)
                .writeDate(this.writeDate)
                .team(this.team)
                .build();
    }
}
