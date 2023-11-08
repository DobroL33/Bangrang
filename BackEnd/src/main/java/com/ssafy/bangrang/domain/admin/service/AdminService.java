package com.ssafy.bangrang.domain.admin.service;

import com.ssafy.bangrang.domain.admin.api.response.GetAccountListResponseDto;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface AdminService {
    List<GetAccountListResponseDto> getAccountList(UserDetails userDetails) throws Exception;

    void updateAccountStatus(Long userIdx, int status,UserDetails userDetails) throws Exception;
    void deleteAccount(Long userIdx,UserDetails userDetails) throws Exception;

}
