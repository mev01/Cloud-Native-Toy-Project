package com.web.curation.request;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.web.curation.error.CustomException;
import com.web.curation.recommendation.Mbti;
import com.web.curation.recommendation.MbtiDao;
import com.web.curation.team.join.JoinTeam;
import com.web.curation.team.join.JoinTeamDao;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import com.web.curation.member.Member;
import com.web.curation.member.MemberDao;
import com.web.curation.team.Team;
import com.web.curation.team.TeamDao;
import org.springframework.transaction.annotation.Transactional;

import static com.web.curation.error.ErrorCode.*;

@Service
@AllArgsConstructor
public class RequestService {
    RequestDao requestDao;
    MemberDao memberdao;
    TeamDao teamDao;
    JoinTeamDao joinTeamDao;
    MbtiDao mbtiDao;

    public void makeRequest(RequestDto requestDto) {
        Team team = teamDao.findById(requestDto.getTeam().getTeamId())
                .orElseThrow(() -> new CustomException(TEAM_NOT_FOUND));
        Member member = memberdao.findById(requestDto.getMember().getMemberId())
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
        requestDao.getRequestByTeamAndMember(team, member)
                .ifPresent(request1 -> {
                    throw new CustomException(REQUEST_DUPLICATE_RESOURCE);
                });

        Request request = RequestAdapter.createDtoToEntity(requestDto);
        requestDao.save(request);
    }

    public List<RequestDto> getRequestList(int teamId) {
        Team team = teamDao.findById(teamId)
                .orElseThrow(() -> new CustomException(TEAM_NOT_FOUND));

        List<Request> entityList = requestDao.findAllByTeam(team);
        if (entityList == null) {
            return Collections.emptyList();
        }

        List<RequestDto> dtoList = new ArrayList<>();
        for (Request request : entityList) {
            dtoList.add(RequestAdapter.entityToDto(request));
        }

        return dtoList;
    }

    @Transactional
    public RequestDto acceptRequest(int requestId) {
        Request request = requestDao.findById(requestId)
                .orElseThrow(() -> new CustomException(REQUEST_NOT_FOUND));
        if (request.getStatus() != Status.WAITING) {
            throw new CustomException(REQUEST_PROCESSED_RESOURCE);
        }

        RequestDto requestDto = RequestAdapter.entityToDto(request);
        requestDto.setStatus(Status.ACCEPTED);
        requestDao.save(RequestAdapter.updateDtoToEntity(requestDto));
        increaseMemberCount(requestDto.getTeam().getTeamId());
        increaseTeamMbti(requestDto.getTeam().getTeamId(), requestDto.getMember().getMbti());
        makeJoinTeam(requestDto);

        return requestDto;
    }

    private void increaseMemberCount(int teamId) {
        Team team = teamDao.findById(teamId)
                .orElseThrow(() -> new CustomException(TEAM_NOT_FOUND));
        team.updateMemberCount(true);
        teamDao.save(team);
    }

    private void increaseTeamMbti(int teamId, String myMbti) {
        Mbti mbti = mbtiDao.findById(teamId)
                .orElseThrow(() -> new CustomException(MBTI_NOT_FOUND));

        switch (myMbti) {
            case "infp":
                mbti.setInfp(mbti.getInfp() + 1);
                break;
            case "enfp":
                mbti.setEnfp(mbti.getEnfp() + 1);
                break;
            case "infj":
                mbti.setInfj(mbti.getInfj() + 1);
                break;
            case "enfj":
                mbti.setEnfj(mbti.getEnfj() + 1);
                break;
            case "intj":
                mbti.setIntj(mbti.getIntj() + 1);
                break;
            case "entj":
                mbti.setEntj(mbti.getEntj() + 1);
                break;
            case "intp":
                mbti.setIntp(mbti.getIntp() + 1);
                break;
            case "entp":
                mbti.setEntp(mbti.getEntp() + 1);
                break;
            case "isfp":
                mbti.setIsfp(mbti.getIsfp() + 1);
                break;
            case "esfp":
                mbti.setEsfp(mbti.getEsfp() + 1);
                break;
            case "istp":
                mbti.setIstp(mbti.getIstp() + 1);
                break;
            case "estp":
                mbti.setEstp(mbti.getEstp() + 1);
                break;
            case "isfj":
                mbti.setIsfj(mbti.getIsfj() + 1);
                break;
            case "esfj":
                mbti.setEsfj(mbti.getEsfj() + 1);
                break;
            case "istj":
                mbti.setIstj(mbti.getIstj() + 1);
                break;
            case "estj":
                mbti.setEstj(mbti.getEstj() + 1);
                break;
            default:
                break;
        }
    }

    private void makeJoinTeam(RequestDto requestDto) {
        Member member = memberdao.findById(requestDto.getMember().getMemberId())
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
        Team team = teamDao.findById(requestDto.getTeam().getTeamId())
                .orElseThrow(() -> new CustomException(TEAM_NOT_FOUND));

        joinTeamDao.findByMemberAndTeam(member, team)
                .ifPresent(joinTeam -> {
                    throw new CustomException(JOIN_TEAM_PROCESSED_RESOURCE);
                });
        JoinTeam joinTeam = JoinTeam.builder()
                .member(member)
                .team(team)
                .build();
        joinTeamDao.save(joinTeam);
    }

    public void rejectRequest(int requestId) {
        Request request = requestDao.findById(requestId)
                .orElseThrow(() -> new CustomException(REQUEST_NOT_FOUND));
        if (request.getStatus() != Status.WAITING) {
            throw new CustomException(REQUEST_PROCESSED_RESOURCE);
        }
        requestDao.delete(request);
    }
}
