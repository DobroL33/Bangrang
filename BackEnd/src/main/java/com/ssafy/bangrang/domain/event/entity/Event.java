package com.ssafy.bangrang.domain.event.entity;

import com.ssafy.bangrang.domain.inquiry.entity.Inquiry;
import com.ssafy.bangrang.domain.member.entity.WebMember;
import com.ssafy.bangrang.global.common.entity.CommonEntity;
import jakarta.persistence.*;
import lombok.*;

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

    @Column(name = "event_title", nullable = false)
    private String title;

    @Column(name = "event_sub_title")
    private String subTitle;

    @Column(name = "event_content")
    private String content;

    @Column(name = "event_image")
    private String image;

    @Column(name = "event_subImage")
    private String subImage;

    @Column(name = "event_start_date")
    private LocalDateTime startDate;

    @Column(name = "event_end_date")
    private LocalDateTime endDate;

    @Column(name = "event_address")
    private String address;

    @Column(name = "event_latitude")
    private Double latitude;

    @Column(name = "event_longitude")
    private Double longitude;

    @Column(name = "event_url")
    private String eventUrl;

    // 행사 등록한 사람
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_idx")
    private WebMember webMember;

    // 행사에 대한 문의사항
    @OneToMany(mappedBy = "event")
    private List<Inquiry> inquiries = new ArrayList<>();

    // 좋아요 수
    @OneToMany(mappedBy = "event")
    private List<Likes> likes = new ArrayList<>();

    @Builder
    public Event(String title, String subTitle, String content, String image, String subImage, LocalDateTime startDate, LocalDateTime endDate, String address, Double latitude, Double longitude, String eventUrl, WebMember webMember){
        this.title = title;
        this.subTitle = subTitle;
        this.content = content;
        this.image = image;
        this.subImage = subImage;
        this.startDate = startDate;
        this.endDate = endDate;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.eventUrl = eventUrl;
        this.changeWebMember(webMember);
    }

    public void changeWebMember(WebMember webMember) {
        this.webMember = webMember;
        webMember.getEvents().add(this);
    }


}