package com.web.curation.member;

import java.io.*;
import java.util.Optional;

import com.web.curation.amazonS3.S3Uploader;
import com.web.curation.error.CustomException;
import com.web.curation.files.Photo;
import com.web.curation.files.PhotoAndDtoAdapter;
import com.web.curation.files.PhotoDao;
import com.web.curation.files.PhotoDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import static com.web.curation.error.ErrorCode.MEMBER_NOT_FOUND;
import static com.web.curation.error.ErrorCode.PHOTO_NOT_FOUND;

@Slf4j
@AllArgsConstructor
@Service
public class MemberService {

    private MemberDao memberDao;
    private S3Uploader s3Uploader;
    private PhotoDao photoDao;

    public boolean hasSameEmail(String email) {
        Optional<Member> member = memberDao.getMemberByEmail(email);
        return member.isPresent();
    }

    public MemberDto registerMember(MemberDto memberDto) {
        memberDto.setPhoto(photoDao.findById(1)
                .orElseThrow(() -> new CustomException(PHOTO_NOT_FOUND))
        );
        Member member = memberDao.save(MemberAdapter.dtoToEntity(memberDto));
        return MemberAdapter.entityToDto(member);
    }


//    public Optional<MemberDto> getUser(String email) {
//        Optional<Member> member = memberDao.getMemberByEmail(email);
//        Optional<MemberDto> memberDto = Optional.ofNullable(null);
//        if (member.isPresent()) {
//            memberDto = Optional.of(MemberAdapter.entityToDto(member.get()));
//            return memberDto;
//        }
//        return memberDto;
//    }

    public Optional<MemberDto> getUser(String email, String password) {
        Optional<Member> member = memberDao.getMemberByEmailAndPassword(email, password);
        Optional<MemberDto> memberDto = Optional.ofNullable(null);
        if (member.isPresent()) {
            memberDto = Optional.of(MemberAdapter.entityToDto(member.get()));
            return memberDto;
        }
        return memberDto;
    }

    public void certifyMemberByEmail(String email) {
        memberDao.updateAuthentication(email);
    }

    public Optional<MemberDto> getMemberByEmail(String email) {
        Optional<Member> member = memberDao.getMemberByEmail(email);
        Optional<MemberDto> memberDto = Optional.ofNullable(null);
        if (member.isPresent()) {
            memberDto = Optional.of(MemberAdapter.entityToDto(member.get()));
            return memberDto;
        }
        return memberDto;
    }

    public MemberDto updateMember(MemberDto memberDto) throws IOException {
        log.info(memberDto.toString());
        Member member = memberDao.getMemberByEmail(memberDto.getEmail())
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

        memberDto.setMemberId(member.getMemberId());
        memberDto.setCreateDate(member.getCreateDate());
        if (memberDto.getImage() != null) {
            Photo prevPhoto = member.getPhoto();
            memberDto.setPhoto(insertImage(memberDto.getImage()));
            memberDao.save(MemberAdapter.dtoToEntity(memberDto));
            if (prevPhoto.getPhotoId() > 2) {
                s3Uploader.deleteFile(prevPhoto.getImageName() + "." + prevPhoto.getImageExtension());
                photoDao.delete(prevPhoto);
            }
        } else {
            memberDto.setPhoto(member.getPhoto());
            memberDao.save(MemberAdapter.dtoToEntity(memberDto));
        }
        memberDto.setImage(null);

        return memberDto;
    }


    public void updateMemberPassword(String email, String password) throws IOException {
        log.info(email);
        memberDao.getMemberByEmail(email)
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

        memberDao.updatePassword(email, password);
    }


    private Photo insertImage(MultipartFile image) throws IOException {
        PhotoDto uploadPhoto = s3Uploader.upload(image, "static");
        Photo photo = PhotoAndDtoAdapter.dtoToEntity(uploadPhoto);
        return photoDao.save(photo);
    }


    public void updateMemberAuthenticate(String email) {
        memberDao.updateAuthentication(email);
    }

    public MemberDto getMemberInfo(int memberId) {
        Member chkMember = memberDao.findById(memberId).orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
        return MemberAdapter.entityToDto(chkMember);
    }

}
