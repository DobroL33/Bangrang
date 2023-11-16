package com.ssafy.bangrang.domain.inquiry.entity;

import com.ssafy.bangrang.domain.event.entity.Event;
import com.ssafy.bangrang.domain.member.entity.AppMember;
import com.ssafy.bangrang.domain.member.entity.WebMember;
import com.ssafy.bangrang.global.common.entity.CommonEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "inquiry")
@ToString(of = {"idx", "title", "content", "type"})
public class Inquiry extends CommonEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "inquiry_idx")
    private Long idx;

    @Column(name = "inquiry_title")
    private String title;

    @Column(name = "inquiry_content")
    private String content;

    @Column(name = "inquiry_type")
    private String type;

    // 문의사항 등록한 사람
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_idx")
    private AppMember appMember;

    // 어떤 행사에 대한 문의인지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_idx")
    private Event event;

    // 문의에 대한 답변
    @OneToOne(mappedBy = "inquiry")
    private Comment comment;

    @Builder
    public Inquiry(String title, String content, String type, AppMember appMember, Event event){
        this.title = title;
        this.content = content;
        this.type = type;
        this.changeAppMember(appMember);
        this.changeEvent(event);
    }

    public void changeComment(Comment comment) {
        this.comment = comment;
        comment.changeInquiry(this);
    }

    public void changeEvent(Event event) {
        this.event = event;
        event.getInquiries().add(this);
    }

    public void changeAppMember(AppMember appMember) {
        this.appMember = appMember;
        appMember.getInquiries().add(this);
    }

}
