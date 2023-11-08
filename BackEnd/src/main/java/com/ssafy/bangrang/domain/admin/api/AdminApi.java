package com.ssafy.bangrang.domain.admin.api;

import com.ssafy.bangrang.domain.admin.api.request.DeleteAccountRequestDto;
import com.ssafy.bangrang.domain.admin.api.request.UpdateAccountStatusRequestDto;
import com.ssafy.bangrang.domain.admin.service.AdminService;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@Slf4j
public class AdminApi {
    private final AdminService adminService;

    @ApiOperation(value = "manager 계정 리스트 조회")
    @GetMapping
    public ResponseEntity<?> getAccountList(@AuthenticationPrincipal UserDetails userDetails) throws Exception {
        return ResponseEntity.ok().body(adminService.getAccountList(userDetails));
    }

    @ApiOperation(value = "계정 상태 수정")
    @PutMapping
    public ResponseEntity<?> updateAccountStatus(@Valid @RequestBody UpdateAccountStatusRequestDto request,
                                                 @AuthenticationPrincipal UserDetails userDetails) throws Exception {
        adminService.updateAccountStatus(request.getUserIdx(),request.getStatus(),userDetails);
        return ResponseEntity.ok().body("");
    }

    @ApiOperation(value = "계정 가입신청 삭제")
    @DeleteMapping
    public ResponseEntity<?> deleteAccount(@Valid @RequestBody DeleteAccountRequestDto deleteAccountRequestDto,
                                                 @AuthenticationPrincipal UserDetails userDetails) throws Exception {
        adminService.deleteAccount(deleteAccountRequestDto.getUserIdx(),userDetails);
        return ResponseEntity.ok().body("");
    }
}
