package com.ssafy.bangrang.domain.event.entity;

import com.ssafy.bangrang.domain.event.api.request.EventUpdateDto;
import com.ssafy.bangrang.domain.event.api.request.UpdateEventRequestDto;
import com.ssafy.bangrang.domain.inquiry.entity.Inquiry;
import com.ssafy.bangrang.domain.stamp.entity.Stamp;
import com.ssafy.bangrang.domain.member.entity.WebMember;
import com.ssafy.bangrang.global.common.entity.CommonEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "event")
@ToString(of = {"idx", "title"})
public class Event extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "event_idx")
    private Long idx;

    @Column(name = "event_title", nullable = false, columnDefinition = "TEXT")
    private String title;

    @Column(name = "event_sub_title", columnDefinition = "TEXT")
    private String subTitle;

    @Column(name = "event_content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "event_image", columnDefinition = "TEXT")
    private String image;
    // 이미지 1

    @Column(name = "event_subImage", columnDefinition = "TEXT")
    private String subImage;
    // 이미지 2

    @Column(name = "event_start_date")
    private LocalDateTime startDate;

    @Column(name = "event_end_date")
    private LocalDateTime endDate;

    @Column(name = "event_address", columnDefinition = "TEXT")
    private String address;

    @Column(name = "event_latitude")
    private Double latitude;

    @Column(name = "event_longitude")
    private Double longitude;

    @Column(name = "event_url", columnDefinition = "TEXT")
    private String eventUrl;
    // 행사 메인 페이지

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

    @OneToOne(mappedBy = "event", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private Stamp stamp;

    @Builder
    public Event(Long idx,String title, String subTitle, String content, String image, String subImage, LocalDateTime startDate, LocalDateTime endDate, String address, Double latitude, Double longitude, String eventUrl, WebMember webMember){
        this.idx = idx;
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

    public Event update(EventUpdateDto eventPutDto, String eventUrl) {
        this.title = eventPutDto.getTitle();
        this.content = eventPutDto.getTitle();
        this.subTitle = eventPutDto.getSubTitle();
        this.latitude = eventPutDto.getLatitude();
        this.longitude = eventPutDto.getLongitude();
        this.startDate = LocalDateTime.parse(eventPutDto.getStartDate());
        this.endDate = LocalDateTime.parse(eventPutDto.getEndDate());
        this.address = eventPutDto.getAddress();
        this.eventUrl = eventUrl;
        return this;
    }
    /**
     * 이벤트 상태 변경
     */
    public void updateEvent(UpdateEventRequestDto updateEventRequestDto, double changelati, double changelong) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        this.title=updateEventRequestDto.getTitle();
        this.subTitle=updateEventRequestDto.getSubTitle();
        this.content=updateEventRequestDto.getContent();
        this.startDate=LocalDateTime.parse(updateEventRequestDto.getStartDate(),formatter);
        this.endDate=LocalDateTime.parse(updateEventRequestDto.getEndDate());
        this.address=updateEventRequestDto.getAddress();
        this.latitude=changelati;
        this.longitude=changelong;
        this.eventUrl=updateEventRequestDto.getEventUrl();
    }
    public void updateEventImg(String ImgUrl) {
        this.image=ImgUrl;
    }
    public void updateEventSubImg(String ImgUrl) {
        this.subImage=ImgUrl;
    }
}
