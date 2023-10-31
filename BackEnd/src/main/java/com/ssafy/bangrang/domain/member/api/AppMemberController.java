package com.ssafy.bangrang.domain.member.api;

import com.ssafy.bangrang.domain.member.api.request.WebMemberSignUpRequest;
import com.ssafy.bangrang.domain.member.service.AppMemberService;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/app")
public class AppMemberController {

    private final AppMemberService appMemberService;

}
