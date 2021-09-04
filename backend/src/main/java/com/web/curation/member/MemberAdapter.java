package com.web.curation.member;

public class MemberAdapter {
    public static MemberDto entityToDto(Member member) {
        return new MemberDto.MemberDtoBuilder()
                .memberId(member.getMemberId())
                .name(member.getName())
                .email(member.getEmail())
                .phone(member.getPhone())
                .password(member.getPassword())
                .address(member.getAddress())
                .addressDetail(member.getAddressDetail())
                .zonecode(member.getZonecode())
                .sex(member.getSex())
                .point(member.getPoint())
                .mbti(member.getMbti())
                .authenticated(member.getAuthenticated())
                .photo(member.getPhoto())
                .build();
    }

    public static Member dtoToEntity(MemberDto memberDto) {
        return new Member.MemberBuilder()
                .memberId(memberDto.getMemberId())
                .name(memberDto.getName())
                .email(memberDto.getEmail())
                .phone(memberDto.getPhone())
                .password(memberDto.getPassword())
                .address(memberDto.getAddress())
                .addressDetail(memberDto.getAddressDetail())
                .zonecode(memberDto.getZonecode())
                .sex(memberDto.getSex())
                .point(memberDto.getPoint())
                .mbti(memberDto.getMbti())
                .createDate(memberDto.getCreateDate())
                .authenticated(memberDto.getAuthenticated())
                .photo(memberDto.getPhoto())
                .build();
    }
}
