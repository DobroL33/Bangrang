package com.ssafy.bangrang.domain.admin.service;

import com.ssafy.bangrang.domain.admin.api.response.GetAccountListResponseDto;
import com.ssafy.bangrang.domain.member.entity.WebMember;
import com.ssafy.bangrang.domain.member.model.vo.WebMemberStatus;
import com.ssafy.bangrang.domain.member.repository.WebMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AdminServiceImpl implements AdminService {

    private final WebMemberRepository webMemberRepository;

    @Override
    public List<GetAccountListResponseDto> getAccountList(UserDetails userDetails) throws Exception{

        WebMember admin = webMemberRepository.findById(userDetails.getUsername())
                .orElseThrow(() -> new EmptyResultDataAccessException("해당 유저는 존재하지 않습니다.", 1));

        if (!admin.getOrganizationName().equals("admin@bangrang"))
            throw new Exception("관리자 계정이 아닙니다.");

        List<WebMember> webMemberList = webMemberRepository.findAll();

        List<GetAccountListResponseDto> result = new ArrayList<>();

        for (WebMember webMember : webMemberList) {
            if (!webMember.getOrganizationName().equals("admin@bangrang")) {
                GetAccountListResponseDto getAccountListResponseDto = GetAccountListResponseDto.builder()
                        .idx(webMember.getIdx())
                        .id(webMember.getId())
                        .organizationName(webMember.getOrganizationName())
                        .authFile(webMember.getAuthFile())
                        .status(webMember.getWebMemberStatus())
                        .build();
                result.add(getAccountListResponseDto);
            }
        }
        return result;
    }
    @Override
    @Transactional
    public void updateAccountStatus(Long userIdx, int status, UserDetails userDetails) throws Exception {

        WebMember admin = webMemberRepository.findById(userDetails.getUsername())
                .orElseThrow(() -> new EmptyResultDataAccessException("해당 유저는 존재하지 않습니다.", 1));

        if (!admin.getOrganizationName().equals("admin@bangrang"))
            throw new Exception("관리자 계정이 아닙니다.");

        WebMember user = webMemberRepository.findByIdx(userIdx)
                .orElseThrow(() -> new EmptyResultDataAccessException("해당 유저는 존재하지 않습니다.", 1));

        if (status == 0)
            user.changeWebMemberStatus(WebMemberStatus.WAITING);
        else if (status == 1)
            user.changeWebMemberStatus(WebMemberStatus.ACCEPTED);
        else if (status == 2)
            user.changeWebMemberStatus(WebMemberStatus.DECLINED);
        else
            throw new Exception("올바르지 않은 status 변수입니다.");

    }

    @Override
    @Transactional
    public void deleteAccount(Long userIdx, UserDetails userDetails) throws Exception {

        WebMember admin = webMemberRepository.findById(userDetails.getUsername())
                .orElseThrow(() -> new EmptyResultDataAccessException("해당 유저는 존재하지 않습니다.", 1));

        if (!admin.getOrganizationName().equals("admin@bangrang"))
            throw new Exception("관리자 계정이 아닙니다.");

        WebMember user = webMemberRepository.findByIdx(userIdx)
                .orElseThrow(() -> new EmptyResultDataAccessException("해당 유저는 존재하지 않습니다.", 1));

        webMemberRepository.delete(user);

    }

}
