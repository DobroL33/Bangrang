package com.ssafy.bangrang.domain.inquiry.entity;

import com.ssafy.bangrang.domain.member.entity.WebMember;
import com.ssafy.bangrang.global.common.entity.CommonEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "comment")
public class Comment extends CommonEntity {

    @Id
    @GeneratedValue
    @Column(name = "comment_idx")
    private Long idx;

    @Column(name = "comment_content")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_idx")
    private WebMember webMember;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inquiry_idx")
    private Inquiry inquiry;

}
