package com.ssafy.bangrang.global.fcm.entity;

import com.ssafy.bangrang.domain.member.entity.AppMember;
import com.ssafy.bangrang.global.common.entity.CommonEntity;
import com.ssafy.bangrang.global.fcm.model.vo.AlarmType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "alarm")
public class Alarm extends CommonEntity {

    @Id
    @GeneratedValue
    @Column(name = "alarm_idx")
    private Long idx;

    @Enumerated(EnumType.STRING)
    @Column(name = "alarm_type")
    private AlarmType type;

    @Column(name = "alarm_content")
    private String content;

    @Column(name = "alarm_status")
    private int status;

    @Column(name = "alarm_createdDate")
    private LocalDateTime createdDate;

    @Column(name = "alarm_eventIdx")
    private Long eventIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_idx")
    private AppMember appMember;

    @Builder
    public Alarm(AlarmType type,String content, Long eventIdx, LocalDateTime createdDate, int status, AppMember appMember){
        this.type = type;
        this.content = content;
        this.eventIdx = eventIdx;
        this.createdDate = createdDate;
        this.status = 0;
        this.appMember = appMember;

    }

    /**
     * 알람 상태 변경
     */
    public void updateStatus(int status) {
        this.status = status;
    }
}
