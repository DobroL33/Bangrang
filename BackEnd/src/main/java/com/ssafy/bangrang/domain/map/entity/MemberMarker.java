package com.ssafy.bangrang.domain.map.entity;

import com.ssafy.bangrang.domain.member.entity.AppMember;
import com.ssafy.bangrang.global.common.entity.CommonEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member_marker")
public class MemberMarker extends CommonEntity {

    @Id
    @GeneratedValue
    @Column(name = "member_marker_idx")
    private Long idx;

    @Column(name = "member_marker_latitude")
    private Double latitude;

    @Column(name = "member_marker_longitude")
    private Double longitude;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_idx")
    private AppMember appMember;


}
