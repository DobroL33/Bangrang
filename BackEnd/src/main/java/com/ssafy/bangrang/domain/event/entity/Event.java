package com.ssafy.bangrang.domain.event.entity;

import com.ssafy.bangrang.domain.inquiry.entity.Inquiry;
import com.ssafy.bangrang.domain.member.entity.WebMember;
import com.ssafy.bangrang.global.common.entity.CommonEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "event")
public class Event extends CommonEntity {

    @Id
    @GeneratedValue
    @Column(name = "event_idx")
    private Long idx;

    @Column(name = "evnet_title", nullable = false)
    private String title;

    @Column(name = "event_content")
    private String content;

    @Column(name = "event_start_date")
    private LocalDateTime startDate;

    @Column(name = "event_end_date")
    private LocalDateTime endDate;

    @Column(name = "evnet_address")
    private String address;

    @Column(name = "event_url")
    private String eventUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "web_member_idx")
    private WebMember webMember;

    @OneToMany(mappedBy = "inquiry_idx")
    private List<Inquiry> inquiries = new ArrayList<>();

}
