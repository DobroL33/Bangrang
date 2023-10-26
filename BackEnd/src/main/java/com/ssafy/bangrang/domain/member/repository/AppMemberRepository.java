package com.ssafy.bangrang.domain.member.repository;

import com.ssafy.bangrang.domain.member.entity.AppMember;
import com.ssafy.bangrang.domain.member.model.vo.SocialProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AppMemberRepository extends JpaRepository<AppMember, Long> {

    @Query("SELECT u FROM AppMember u WHERE u.email = :email")
    Optional<AppMember> findByEmail(String email);

    Optional<AppMember> findByEmailAndDeletedDateIsNull(String email);

    /**
     * 소셜 타입 & 소셜 식별 값으로 추가 정보를 입력하지 않은 회원을 찾는 메서드
     * 소셜 로그인 시 추가 정보를 입력받지 않은 채 저장된 상태에서 추가 정보를 입력받아 회원 가입을 진행하기 위해 사용
     */
    Optional<AppMember> findBySocialTypeAndSocialId(SocialProvider socialProvider, String socialId);

    @Override
    Optional<AppMember> findById(Long aLong);

    @Override
    void deleteById(Long aLong);

    @Override
    <S extends AppMember> S save(S entity);
}
