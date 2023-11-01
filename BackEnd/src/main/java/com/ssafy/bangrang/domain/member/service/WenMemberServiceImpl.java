package com.ssafy.bangrang.domain.member.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.ssafy.bangrang.domain.member.api.request.WebMemberSignUpRequestDto;
import com.ssafy.bangrang.domain.member.entity.WebMember;
import com.ssafy.bangrang.domain.member.repository.WebMemberRepository;
import com.ssafy.bangrang.global.security.redis.RedisAccessTokenService;
import com.ssafy.bangrang.global.security.redis.RedisRefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class WenMemberServiceImpl implements WebMemberService {

    private final WebMemberRepository webMemberRepository;

    private final PasswordEncoder passwordEncoder;

    private final RedisRefreshTokenService redisRefreshTokenService;

    private final RedisAccessTokenService redisAccessTokenService;

    @Value("${cloud.aws.s3.bucket}")
    private String S3Bucket;

    @Autowired
    AmazonS3Client amazonS3Client;

    /**
     * 일반 회원 가입
     */
    @Override
    @Transactional
    public Long signup(WebMemberSignUpRequestDto webMemberSignUpRequestDto, MultipartFile multipartFile) throws Exception {

        // 문서 인증파일 필수!
        if (multipartFile.isEmpty()) {
            throw new IllegalArgumentException("파일이 없으면 회원가입할 수 없습니다.");
        }

        // 아이디 중복 여부
        if(webMemberRepository.findById(webMemberSignUpRequestDto.getId()).isPresent())
            throw new Exception("이미 존재하는 아이디입니다.");

        // 아이디 유효성 검사  (영문,숫자 5자 이상)
        if (!Pattern.matches("^[a-zA-Z0-9]{5,}$", webMemberSignUpRequestDto.getId())) {
            throw new IllegalStateException("이메일 형식을 다시 맞춰주세요.");
        }

        // 비밀번호 유효성 검사
        if (!Pattern.matches("^.*(?=^.{9,15}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$", webMemberSignUpRequestDto.getPassword())) {
            throw new IllegalStateException("비밀번호 형식이 맞지않습니다.");
        }

        String fileName = webMemberSignUpRequestDto.getId() + "_auth_file";
        byte[] fileBytes = multipartFile.getBytes();

        // S3에 업로드하고 그 url 가져옴
        String authfilePath = uploadToS3(fileName, fileBytes, multipartFile.getContentType());

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

    /**
     * S3 업로드 메서드
     */
    private String uploadToS3(String fileName, byte[] fileBytes, String contentType) {
        ObjectMetadata objectMetaData = new ObjectMetadata();
        objectMetaData.setContentType(contentType);
        objectMetaData.setContentLength(fileBytes.length);

        try {
            // S3에 업로드
            amazonS3Client.putObject(
                    new PutObjectRequest(S3Bucket, fileName, new ByteArrayInputStream(fileBytes), objectMetaData)
                            .withCannedAcl(CannedAccessControlList.PublicRead)
            );

            String filePath = amazonS3Client.getUrl(S3Bucket, fileName).toString(); // 접근 가능한 URL 가져오기

            if (filePath == null) {
                throw new IllegalArgumentException("이미지 경로를 가져오지 못하였습니다.");
            }

            return filePath;
        } catch (AmazonClientException e) {
            throw new RuntimeException("S3에 이미지를 업로드하는데 실패했습니다.", e);
        }
    }

}
