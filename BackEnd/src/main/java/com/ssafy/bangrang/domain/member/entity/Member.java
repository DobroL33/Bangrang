package com.ssafy.bangrang.domain.member.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "DTYPE")
@Table(name = "member")
@Getter
public abstract class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_idx")
    Long idx;

    @Column(name = "member_id", unique = true)
    protected String id;

    @Column(name = "member_password")
    String password;

    /**
     * 비밀번호 암호화
     */
    public void passwordEncode(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }
}
