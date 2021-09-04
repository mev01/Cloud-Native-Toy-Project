package com.web.curation.board;

import com.web.curation.error.CustomException;
import com.web.curation.error.ErrorCode;
import com.web.curation.error.NotFoundDataException;
import com.web.curation.team.Team;
import com.web.curation.team.TeamDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardDao boardDao;
    private final TeamDao teamDao;

    public List<Board> getBoardlist(int teamId) {
        Team chkTeam = teamDao.findById(teamId).orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND));
        return boardDao.findByTeam(chkTeam);
    }

    public BoardDto getBoardOne(int boardId) {
        Board board = boardDao.findById(boardId).orElseThrow(NotFoundDataException::new);
        return BoardAndDtoAdapter.entityToDto(board);
    }

    public boolean getBoardExist(int boardId) {
        return boardDao.existsById(boardId);
    }

    public BoardDto writeBoard(BoardDto boardDto) {
        Team team = teamDao.findById(boardDto.getTeamId()).orElseThrow(NotFoundDataException::new);
        boardDto.setTeam(team);
        return BoardAndDtoAdapter.entityToDto(boardDao.save(boardDto.toEntity()));
    }

    public boolean deleteBoard(int boardId) {
        boardDao.findById(boardId).orElseThrow(NotFoundDataException::new);
        boardDao.deleteById(boardId);
        return true;
    }

    public BoardDto modifyBoard(BoardDto boardDto) {
        boardDao.findById(boardDto.getBoardId()).orElseThrow(NotFoundDataException::new);
        Team team = teamDao.findById(boardDto.getTeamId())
                .orElseThrow(NotFoundDataException::new);
        boardDto.setTeam(team);
        return BoardAndDtoAdapter.entityToDto(boardDao.save(BoardAndDtoAdapter.dtoToEntity(boardDto)));
    }
}
