package com.ssafy.bangrang.domain.member.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "DTYPE")
@Table(name = "member")
@ToString(of = {"idx", "id"})
public abstract class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "member_idx")
    Long idx;

    @Column(name = "member_id", unique = true)
    protected String id;

    @Column(name = "member_password")
    protected String password;

    /**
     * 비밀번호 암호화
     */
    public void passwordEncode(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }
}
