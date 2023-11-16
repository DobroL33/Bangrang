package com.ssafy.bangrang.domain.inquiry.entity;

import com.ssafy.bangrang.domain.member.entity.WebMember;
import com.ssafy.bangrang.global.common.entity.CommonEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "comment")
@ToString(of = {"idx", "content"})
public class Comment extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    @Builder
    public Comment(String content, WebMember webMember, Inquiry inquiry){
        this.content = content;
        this.changeWebMember(webMember);
        this.changeInquiry(inquiry);
    }

    public void changeContent(String content){
        this.content = content;
    }

    public void changeWebMember(WebMember webMember){
        this.webMember = webMember;
        webMember.getComments().add(this);
    }

    public void changeInquiry(Inquiry inquiry){
        this.inquiry = inquiry;
    }

    public void update(String content) {
        this.content =content;
    }
}
