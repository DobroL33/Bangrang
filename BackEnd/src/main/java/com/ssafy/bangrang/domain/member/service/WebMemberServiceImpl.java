package com.ssafy.bangrang.domain.member.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.ssafy.bangrang.domain.inquiry.api.response.GetWebInquiryAllResponseDto;
import com.ssafy.bangrang.domain.inquiry.entity.Inquiry;
import com.ssafy.bangrang.domain.member.api.request.WebMemberSignUpRequestDto;
import com.ssafy.bangrang.domain.member.entity.WebMember;
import com.ssafy.bangrang.domain.member.repository.WebMemberRepository;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class WebMemberServiceImpl implements WebMemberService {

    private final WebMemberRepository webMemberRepository;

    private final PasswordEncoder passwordEncoder;

    private final RedisRefreshTokenService redisRefreshTokenService;

    private final RedisAccessTokenService redisAccessTokenService;

    private final S3Service s3Service;


    /**
     * 아이디 중복 검사
     */
    @Override
    public void idUsefulCheck(String id) throws Exception {
        if(webMemberRepository.findById(id).isPresent())
            throw new Exception("이미 존재하는 닉네임입니다.");
    }


    /**
     * 일반 회원 가입
     */
    @Override
    @Transactional
    public Long signup(WebMemberSignUpRequestDto webMemberSignUpRequestDto, MultipartFile multipartFile) throws Exception {
        // auth file 체크
        if (multipartFile.isEmpty())
            throw new Exception("authfile는 필수 파일입니다.");
        // 아이디 중복 여부
        if(webMemberRepository.findById(webMemberSignUpRequestDto.getId()).isPresent())
            throw new Exception("이미 존재하는 아이디입니다.");

        // 아이디 유효성 검사  (영문,숫자 5자 이상)
        if (!Pattern.matches("^[a-zA-Z0-9_]{5,}$", webMemberSignUpRequestDto.getId())) {
            throw new IllegalStateException("아이디 형식을 다시 맞춰주세요.");
        }

        // 비밀번호 유효성 검사
        if (!Pattern.matches("^.*(?=^.{9,15}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$", webMemberSignUpRequestDto.getPassword())) {
            throw new IllegalStateException("비밀번호 형식이 맞지않습니다.");
        }

        // 저장할 경로, 이름 설정
        String fileName =  s3Service.generateAuthFileName(multipartFile, webMemberSignUpRequestDto.getId());

        byte[] fileBytes = multipartFile.getBytes();

        // S3에 업로드하고 그 url 가져옴
        String authfilePath = s3Service.uploadToS3(fileName, fileBytes, multipartFile.getContentType());

        // 계정 엔티티 생성
        WebMember webMember = webMemberSignUpRequestDto.toEntity(authfilePath);

        // 패스워드 암호화
        webMember.passwordEncode(passwordEncoder);
        // 그리고 저장
        WebMember saveUser = webMemberRepository.save(webMember);
        // 생성한 계정의 Idx 번호 리턴
        return saveUser.getIdx();
    }

    // 로그아웃
    @Override
    public Long logout(String accessToken, UserDetails userDetails) {

        WebMember user = webMemberRepository.findById(userDetails.getUsername())
                .orElseThrow(() -> new EmptyResultDataAccessException("해당 유저는 존재하지 않습니다.", 1));

        redisRefreshTokenService.deleteRefreshToken(user.getId());
        redisAccessTokenService.setRedisAccessToken(accessToken.replace("Bearer ", ""), "LOGOUT");

        return user.getIdx();
    }

    @Override
    public List<GetWebInquiryAllResponseDto> findInquiryById(String username) {

        WebMember webMember = webMemberRepository.findById(username).orElseThrow();

        List<GetWebInquiryAllResponseDto> inquiryList = new ArrayList<>();
        webMember.getEvents().stream().forEach(event -> {
            List<Inquiry> inquiries = event.getInquiries();
            inquiries.stream().forEach(inquiry -> {
               inquiryList.add(GetWebInquiryAllResponseDto.builder()
                               .inquiryIdx(inquiry.getIdx())
                               .title(inquiry.getTitle())
                               .event(event.getTitle())
                               .createdAt(inquiry.getCreatedAt())
                               .comment(!Objects.isNull(inquiry.getComment()))
                       .build());
            });
        });

        return inquiryList;
    }

}
