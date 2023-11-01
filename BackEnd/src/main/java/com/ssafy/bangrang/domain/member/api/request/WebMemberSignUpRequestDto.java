package com.ssafy.bangrang.domain.member.api.request;

import com.ssafy.bangrang.domain.member.entity.WebMember;
import com.ssafy.bangrang.domain.member.model.vo.WebMemberStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WebMemberSignUpRequestDto {

    @NotBlank(message = "id는 빈값이 올 수 없습니다")
    private String id;
    @NotBlank(message = "password는 빈값이 올 수 없습니다")
    private String password;
    @NotBlank(message = "organizationName는 빈값이 올 수 없습니다")
    private String organizationName;

    public WebMember toEntity(String authFile) {

        return WebMember.builder()
                .id(this.id)
                .password(this.password)
                .organizationName(this.organizationName)
                .webMemberStatus(WebMemberStatus.WATING)
                .authFile(authFile)
                .build();
    }
}
