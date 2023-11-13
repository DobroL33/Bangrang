package com.ssafy.bangrang.domain.rank.entity;

import com.ssafy.bangrang.domain.map.model.vo.RegionType;
import com.ssafy.bangrang.domain.member.entity.AppMember;
import com.ssafy.bangrang.global.common.entity.CommonEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ranking")
@ToString(of = {"regionType", "rank", "appMember"})
public class Ranking extends CommonEntity {

    @Id
    @GeneratedValue
    @Column(name = "ranking_idx")
    private Long idx;

    @Enumerated(EnumType.STRING)
    @Column(name = "ranking_region", nullable = false)
    private RegionType regionType;

    @Column(name = "ranking_rank", nullable = false)
    private Long rank;

    @Column(name = "member_idx", nullable = false)
    private Long appMember;

    @Builder
    public Ranking(RegionType regionType, Long rank, AppMember appMember){
        this.regionType = regionType;
        this.rank = rank;
        this.appMember = appMember.getIdx();
    }

}
