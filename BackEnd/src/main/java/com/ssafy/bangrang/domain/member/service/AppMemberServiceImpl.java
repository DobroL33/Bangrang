package com.ssafy.bangrang.domain.member.service;

import com.ssafy.bangrang.domain.event.repository.EventRepository;
import com.ssafy.bangrang.domain.inquiry.api.response.GetInquiryAllResponseDto;
import com.ssafy.bangrang.domain.inquiry.entity.Comment;
import com.ssafy.bangrang.domain.member.api.request.UpdateFirebaseRequestDto;
import com.ssafy.bangrang.domain.member.entity.AppMember;
import com.ssafy.bangrang.domain.member.repository.AppMemberRepository;
import com.ssafy.bangrang.domain.stamp.repository.StampRepository;
import com.ssafy.bangrang.global.s3service.S3Service;
import com.ssafy.bangrang.global.security.redis.RedisAccessTokenService;
import com.ssafy.bangrang.global.security.redis.RedisRefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AppMemberServiceImpl implements AppMemberService {

    private final AppMemberRepository appMemberRepository;

    private final PasswordEncoder passwordEncoder;

    private final RedisRefreshTokenService redisRefreshTokenService;

    private final RedisAccessTokenService redisAccessTokenService;

    private final S3Service s3Service;

    private final DateTimeFormatter dateTimeFormatter;

    /**
     * 소셜 로그인 & 회원 가입
     */
    @Override
    @Transactional
    public Long sociallogin(String id, String ImgUrl) throws Exception {

        AppMember appMember = AppMember.builder()
                .id(id)
                .imgUrl(ImgUrl)
                .password("social")
                .build();

        appMember.passwordEncode(passwordEncoder);
        AppMember saveUser = appMemberRepository.save(appMember);


        // 생성한 계정의 Idx 번호 리턴
        return saveUser.getIdx();
    }


    /* 닉네임 중복 확인 */
    @Override
    public void nicknameUsefulCheck(String nickname) throws Exception {

        if(appMemberRepository.findByNickname(nickname).isPresent())
            throw new Exception("이미 존재하는 닉네임입니다.");
    }
    /**
     * 첫 로그인 시 닉네임 추가입력 받기
     */
    @Transactional
    @Override
    public void nicknamePlus(String nickname, UserDetails userDetails) throws Exception {
        // 내 계정정보 불러오기
        AppMember user = appMemberRepository.findById(userDetails.getUsername())
                .orElseThrow(() -> new EmptyResultDataAccessException("해당 유저는 존재하지 않습니다.", 1));

        if (user.getNickname() != null)
            throw new Exception("닉네임이 이미 존재합니다.");

        // 닉네임 중복 체크
        nicknameUsefulCheck(nickname);
        // 닉네임 업데이트
        user.updateNickname(nickname);
        // 그리고 저장
        appMemberRepository.save(user);
    }
    /**
     * 닉네임 업데이트
     */
    @Transactional
    @Override
    public void nicknameUpdate(String nickname, UserDetails userDetails) throws Exception {
        // 내 계정정보 불러오기
        AppMember user = appMemberRepository.findById(userDetails.getUsername())
                .orElseThrow(() -> new EmptyResultDataAccessException("해당 유저는 존재하지 않습니다.", 1));

        // 닉네임 중복 체크
        nicknameUsefulCheck(nickname);
        // 닉네임 업데이트
        user.updateNickname(nickname);
        // 그리고 저장
        appMemberRepository.save(user);
    }


    // 로그아웃
    @Override
    public Long logout(String accessToken, UserDetails userDetails) {

        AppMember user = appMemberRepository.findById(userDetails.getUsername())
                .orElseThrow(() -> new EmptyResultDataAccessException("해당 유저는 존재하지 않습니다.", 1));

        redisRefreshTokenService.deleteRefreshToken(user.getId());
        redisAccessTokenService.setRedisAccessToken(accessToken.replace("Bearer ", ""), "LOGOUT");

        return user.getIdx();
    }

    /**
     * 회원탈퇴
     */
    @Override
    @Transactional
    public Long withdraw(String accessToken, UserDetails userDetails) {
        AppMember user = appMemberRepository.findById(userDetails.getUsername())
                .orElseThrow(() -> new EmptyResultDataAccessException("해당 유저는 존재하지 않습니다.", 1));

        user.updateDeletedDate();
        appMemberRepository.save(user);

        redisRefreshTokenService.deleteRefreshToken(user.getId());
        redisAccessTokenService.setRedisAccessToken(accessToken.replace("Bearer ", ""), "QUIT");

        return user.getIdx();
    }
    
    /**
     * firebase token 업데이트
     * */
    @Override
    @Transactional
    public void updateFirebase(UpdateFirebaseRequestDto firebaseRequestDto, 
                               UserDetails userDetails) {
        AppMember user = appMemberRepository.findById(userDetails.getUsername())
                .orElseThrow(() -> new EmptyResultDataAccessException("해당 유저는 존재하지 않습니다.", 1));
        
        user.updateFirebase(firebaseRequestDto.getToken());
        appMemberRepository.save(user);
    }

    @Override
    @Transactional
    public String profileImgUpdate(MultipartFile multipartFile, UserDetails userDetails) throws Exception {
        AppMember user = appMemberRepository.findById(userDetails.getUsername())
                .orElseThrow(() -> new EmptyResultDataAccessException("해당 유저는 존재하지 않습니다.", 1));

        // 저장할 경로, 이름 설정
        String fileName =  s3Service.generateImgFileName(multipartFile,user.getId());

        byte[] fileBytes = multipartFile.getBytes();

        // S3에 업로드하고 그 url 가져옴
        String imgPath = s3Service.uploadToS3(fileName, fileBytes, multipartFile.getContentType());

        user.updateProfileImg(imgPath);

        return imgPath;
    }


    @Override
    public Long findIdxByNickname(String nickname){
        return appMemberRepository.findIdxByNickname(nickname);
    }

    @Override
    public Optional<AppMember> findById(String id){
        return appMemberRepository.findById(id);
    }

    @Override
    public List<GetInquiryAllResponseDto> findInquiryById(UserDetails userDetails){

        AppMember appMember = appMemberRepository.findById(userDetails.getUsername())
                .orElseThrow(() -> new EmptyResultDataAccessException("해당 유저는 존재하지 않습니다.", 1));


        // member의 inquiry를 불러옴
        List<GetInquiryAllResponseDto> inquiryList = appMember.getInquiries()
                .stream()
                .map(inquiry -> GetInquiryAllResponseDto
                        .builder()
                        .inquiryIdx(inquiry.getIdx())
                        .type(inquiry.getType())
                        .eventName(inquiry.getEvent().getTitle())
                        .title(inquiry.getTitle())
                        .content(inquiry.getContent())
                        .answer(Optional.ofNullable(inquiry.getComment())
                                .map(Comment::getContent)
                                .orElse(null))
                        .resisteDate(inquiry.getCreatedAt().format(dateTimeFormatter))
                        .build())
                .collect(Collectors.toList());

        return inquiryList;
    }

}
