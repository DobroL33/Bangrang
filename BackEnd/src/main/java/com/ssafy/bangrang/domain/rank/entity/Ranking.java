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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ranking_idx")
    private Long idx;

    @Enumerated(EnumType.STRING)
    @Column(name = "ranking_region", nullable = false)
    private RegionType regionType;

    @Column(name = "ranking_rank", nullable = false)
    private Long rank;

    @Column(name = "ranking_rating", nullable = false)
    private Double rating; // 상위퍼센트

    @Column(name = "ranking_percent", nullable = false)
    private Double percent; //정복율

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_idx")
    private AppMember appMember;



    @Builder
    public Ranking(RegionType regionType, Long rank, Double rating, Double percent, AppMember appMember){
        this.regionType = regionType;
        this.rank = rank;
        this.rating = rating;
        this.percent = percent;
        this.changeAppMember(appMember);
    }

    private void changeAppMember(AppMember appMember) {
        this.appMember = appMember;
    }

}
