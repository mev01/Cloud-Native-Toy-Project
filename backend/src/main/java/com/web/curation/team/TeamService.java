package com.web.curation.team;


import com.web.curation.amazonS3.S3Uploader;
import com.web.curation.error.CustomException;
import com.web.curation.error.NotFoundDataException;
import com.web.curation.files.PhotoAndDtoAdapter;
import com.web.curation.files.PhotoDao;
import com.web.curation.files.PhotoDto;
import com.web.curation.member.Member;
import com.web.curation.member.MemberDao;
import com.web.curation.recommendation.Mbti;
import com.web.curation.recommendation.MbtiDao;
import com.web.curation.team.join.JoinTeam;
import com.web.curation.team.join.JoinTeamAdapter;
import com.web.curation.team.join.JoinTeamDao;
import com.web.curation.team.join.JoinTeamDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.web.curation.error.ErrorCode.*;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class TeamService {

    private final TeamDao teamDao;
    private final JoinTeamDao joinTeamDao;
    private final MemberDao memberDao;
    private final S3Uploader s3Uploader;
    private final PhotoDao photoDao;
    private static final int TEAM_DEFAULT_IMAGE = 2;
    private final MbtiDao mbtiDao;


    List<TeamDto> getTeamlist() {
        List<Team> teamList = teamDao.findAll();
        List<TeamDto> responseTeams = new ArrayList<>();
        for (Team team : teamList) {
            TeamDto teamDto = TeamAndDtoAdapter.entityToDto(team);
            if (team.getPhoto() != null) {
                teamDto.setPhotoDto(PhotoAndDtoAdapter.entityToDto(team.getPhoto()));
            }
            responseTeams.add(teamDto);
        }
        return responseTeams;
    }

    public boolean checkNameDuplicate(String name) {
        return teamDao.existsByName(name);
    }


    public List<TeamDto> getMyTeamList(int memberId) {
        List<TeamDto> teamList = new ArrayList<>();
        Member member = memberDao.findById(memberId)
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
        List<JoinTeam> joinTeams = joinTeamDao.findJoinTeamByMember(member);

        for (JoinTeam joinTeam : joinTeams) {
            JoinTeamDto joinTeamDto = JoinTeamAdapter.entityToDto(joinTeam);
            teamList.add(joinTeamDto.getTeam());
        }
        return teamList;
    }

    public TeamDto registerTeam(TeamDto teamDto) throws IOException {
        PhotoDto savedPhoto = new PhotoDto();
        if (teamDto.getMultipartFile() != null) {
            PhotoDto uploadPhoto = s3Uploader.upload(teamDto.getMultipartFile(), "static");
            savedPhoto = PhotoAndDtoAdapter.entityToDto(photoDao.save(PhotoAndDtoAdapter.dtoToEntity(uploadPhoto)));
        }
        Team team = null;
        teamDto.setMemberCount(1);
        if (savedPhoto.getPhotoId() != null) {
            teamDto.setPhotoId(savedPhoto.getPhotoId());
            teamDto.setPhotoDto(savedPhoto);
            team = TeamAndDtoAdapter.dtoToEntityPhoto(teamDto);
        } else {
            teamDto.setPhotoId(TEAM_DEFAULT_IMAGE);
            teamDto.setPhotoDto(new PhotoDto(TEAM_DEFAULT_IMAGE));
            team = TeamAndDtoAdapter.dtoToEntityPhoto(teamDto);
        }
        teamDto.setMemberCount(1);
        TeamDto resultTeamDto = TeamAndDtoAdapter.entityToDto(teamDao.save(team));

        Team chkTeam = teamDao.findById(resultTeamDto.getTeamId())
                .orElseThrow(NotFoundDataException::new);
        Member chkMember = memberDao.findById(resultTeamDto.getMemberId())
                .orElseThrow(NotFoundDataException::new);
        JoinTeam jointeam = JoinTeam.builder()
                .team(chkTeam)
                .member(chkMember)
                .build();
        joinTeamDao.save(jointeam);

        Mbti mbti = new Mbti(chkTeam.getTeamId(), chkMember.getMbti());
        mbtiDao.save(mbti);

        return resultTeamDto;
    }

    public boolean changeTeamLeader(int teamId, int memberId) {
        Member member = memberDao.findById(memberId)
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
        Team team = teamDao.findById(teamId)
                .orElseThrow(() -> new CustomException(TEAM_NOT_FOUND));

        TeamDto teamDto = TeamAndDtoAdapter.entityToDto(team);
        teamDto.setSportId(teamDto.getSportDto().getSportId());
        teamDto.setMemberId(member.getMemberId());
        teamDto.setLeader(member.getName());

        teamDao.save(TeamAndDtoAdapter.dtoToEntityPhoto(teamDto));
        return true;
    }

    public boolean updateTeam(TeamDto teamDto) {
        Team team = teamDao.findById(teamDto.getTeamId())
                .orElseThrow(() -> new CustomException(TEAM_NOT_FOUND));
        teamDto.setCreateDate(team.getCreateDate());

        PhotoDto savedPhoto = new PhotoDto();
        if (teamDto.getMultipartFile() != null) {
            PhotoDto uploadPhoto;
            try {
                uploadPhoto = s3Uploader.upload(teamDto.getMultipartFile(), "static");
                savedPhoto = PhotoAndDtoAdapter.entityToDto(photoDao.save(PhotoAndDtoAdapter.dtoToEntity(uploadPhoto)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (savedPhoto.getPhotoId() != null) {
            teamDto.setPhotoId(savedPhoto.getPhotoId());
            teamDto.setPhotoDto(savedPhoto);
            team = TeamAndDtoAdapter.dtoToEntityPhoto(teamDto);
        } else {
            teamDto.setPhotoId(null);
            teamDto.setPhotoDto(PhotoAndDtoAdapter.entityToDto(team.getPhoto()));
            team = TeamAndDtoAdapter.dtoToEntityPhoto(teamDto);
        }
        teamDao.save(team);

        return true;
    }

    public TeamDto getMyManagingTeam(int memberId) {
        Member member = memberDao.findById(memberId)
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
        Optional<Team> team = teamDao.findTeamByMember(member);

        if (team.isPresent()) {
            return TeamAndDtoAdapter.entityToDto(team.get());
        }

        return TeamDto.builder().teamId(-1).build();
    }

    public boolean checkLeader(int memberId) {
        Member chkMember = memberDao.findById(memberId)
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
        Optional<Team> chkTeam = teamDao.findTeamByMember(chkMember);
        return !chkTeam.isPresent();
    }
}
