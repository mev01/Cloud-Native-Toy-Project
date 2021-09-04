package com.web.curation.recommendation;

import com.web.curation.error.CustomException;
import com.web.curation.member.Member;
import com.web.curation.member.MemberDao;
import com.web.curation.team.Team;
import com.web.curation.team.TeamDao;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.web.curation.error.ErrorCode.MBTI_NOT_FOUND;
import static com.web.curation.error.ErrorCode.MEMBER_NOT_FOUND;

@Service
@AllArgsConstructor
public class RecommendationService {
    private MemberDao memberDao;
    private TeamDao teamDao;
    private MbtiDao mbtiDao;

    private final int[][] mbtiTable = {
            {4, 4, 4, 5, 4, 5, 4, 4, 1, 1, 1, 1, 1, 1, 1, 1},   // INFP
            {4, 4, 5, 4, 5, 4, 4, 4, 1, 1, 1, 1, 1, 1, 1, 1},   // ENFP
            {4, 5, 4, 4, 4, 4, 4, 5, 1, 1, 1, 1, 1, 1, 1, 1},   // INFJ
            {5, 4, 4, 4, 4, 4, 4, 4, 5, 1, 1, 1, 1, 1, 1, 1},   // ENFJ
            {4, 5, 4, 4, 4, 4, 4, 5, 3, 3, 3, 3, 2, 2, 2, 2},   // INTJ
            {5, 4, 4, 4, 4, 4, 5, 4, 3, 3, 3, 3, 3, 3, 3, 3},   // ENTJ
            {4, 4, 4, 4, 4, 5, 4, 4, 3, 3, 3, 3, 2, 2, 2, 5},   // INTP
            {4, 4, 5, 4, 5, 4, 4, 4, 3, 3, 3, 3, 2, 2, 2, 2},   // ENTP
            {1, 1, 1, 5, 3, 3, 3, 3, 2, 2, 2, 2, 3, 5, 3, 5},   // ISFP
            {1, 1, 1, 1, 3, 3, 3, 3, 2, 2, 2, 2, 5, 3, 5, 3},   // ESFP
            {1, 1, 1, 1, 3, 3, 3, 3, 2, 2, 2, 2, 3, 5, 3, 5},   // ISTP
            {1, 1, 1, 1, 3, 3, 3, 3, 2, 2, 2, 2, 5, 3, 5, 3},   // ESTP
            {1, 1, 1, 1, 2, 3, 2, 2, 3, 5, 3, 5, 4, 4, 4, 4},   // ISFJ
            {1, 1, 1, 1, 2, 3, 2, 2, 5, 3, 5, 3, 4, 4, 4, 4},   // ESFJ
            {1, 1, 1, 1, 2, 3, 2, 2, 3, 5, 3, 5, 4, 4, 4, 4},   // ISTJ
            {1, 1, 1, 1, 2, 3, 5, 2, 5, 3, 5, 3, 4, 4, 4, 4}   // ESTJ
    };

    public List<RecommendationDto> getRecommendations(int memberId) {
        Member member = memberDao.findById(memberId)
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
        List<Team> teamList = teamDao.findAll();
        List<RecommendationDto> dtos = new ArrayList<>();
        for (Team team : teamList) {
            Mbti mbti = mbtiDao.findById(team.getTeamId())
                    .orElseThrow(() -> new CustomException(MBTI_NOT_FOUND));
            int myMbti = mbtiToNum(member.getMbti());
            int leaderMbti = mbtiToNum(team.getMember().getMbti());
            if (myMbti == -1 || leaderMbti == -1) {
                throw new CustomException(MBTI_NOT_FOUND);
            }
            float memberScore = (float) calScore(mbtiToNum(member.getMbti()), mbti) / team.getMemberCount();
            float leaderScore = mbtiTable[myMbti][leaderMbti];
            float totalScore = (memberScore * 7 + leaderScore * 3) / 10;
            RecommendationDto recommendationDto = RecommendationAdapter.teamToRecommendation(team);
            recommendationDto.setScore(totalScore);
            dtos.add(recommendationDto);
        }
        Collections.sort(dtos, (o1, o2) -> Float.compare(o2.getScore(), o1.getScore()));
        return dtos;
    }

    private int mbtiToNum(String mbti) {
        switch (mbti) {
            case "infp":
                return 0;
            case "enfp":
                return 1;
            case "infj":
                return 2;
            case "enfj":
                return 3;
            case "intj":
                return 4;
            case "entj":
                return 5;
            case "intp":
                return 6;
            case "entp":
                return 7;
            case "isfp":
                return 8;
            case "esfp":
                return 9;
            case "istp":
                return 10;
            case "estp":
                return 11;
            case "isfj":
                return 12;
            case "esfj":
                return 13;
            case "istj":
                return 14;
            case "estj":
                return 15;
            default:
                return -1;
        }
    }

    private int calScore(int myMbti, Mbti mbti) {

        int memberScore = 0;
        memberScore += mbtiTable[myMbti][0] * mbti.getInfp();
        memberScore += mbtiTable[myMbti][1] * mbti.getEnfp();
        memberScore += mbtiTable[myMbti][2] * mbti.getInfj();
        memberScore += mbtiTable[myMbti][3] * mbti.getEnfj();
        memberScore += mbtiTable[myMbti][4] * mbti.getIntj();
        memberScore += mbtiTable[myMbti][5] * mbti.getEntj();
        memberScore += mbtiTable[myMbti][6] * mbti.getIntp();
        memberScore += mbtiTable[myMbti][7] * mbti.getEntp();
        memberScore += mbtiTable[myMbti][8] * mbti.getIsfp();
        memberScore += mbtiTable[myMbti][9] * mbti.getEsfp();
        memberScore += mbtiTable[myMbti][10] * mbti.getIstp();
        memberScore += mbtiTable[myMbti][11] * mbti.getEstp();
        memberScore += mbtiTable[myMbti][12] * mbti.getIsfj();
        memberScore += mbtiTable[myMbti][13] * mbti.getEsfj();
        memberScore += mbtiTable[myMbti][14] * mbti.getIstj();
        memberScore += mbtiTable[myMbti][15] * mbti.getEstj();

        return memberScore;
    }
}
