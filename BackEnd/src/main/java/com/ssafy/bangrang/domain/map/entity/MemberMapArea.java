package com.ssafy.bangrang.domain.map.entity;

import com.ssafy.bangrang.domain.map.model.vo.RegionType;
import com.ssafy.bangrang.domain.member.entity.AppMember;
import com.ssafy.bangrang.global.common.entity.CommonEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member_map_area")
public class MemberMapArea extends CommonEntity {

    @Id
    @GeneratedValue
    @Column(name = "member_map_area_idx")
    private Long idx;

    @Enumerated(EnumType.STRING)
    @Column(name = "member_map_area_region")
    private RegionType regionType;

    @Column(name = "member_map_area_dimension")
    private Double dimension;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_idx")
    private AppMember appMember;
}
