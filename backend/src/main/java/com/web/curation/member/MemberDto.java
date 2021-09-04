package com.web.curation.member;

import java.time.LocalDateTime;

import com.web.curation.files.Photo;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@Getter
@Setter
@ToString
@Builder
@Valid
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {
    private int memberId;
    private String name;
    private String email;
    private String phone;
    private String password;
    private String address;
    private String addressDetail;
    private String zonecode;
    private String sex;
    private int point;
    private String mbti;
    private LocalDateTime createDate;
    private Boolean authenticated;
    private MultipartFile image;
    private Photo photo;
}
