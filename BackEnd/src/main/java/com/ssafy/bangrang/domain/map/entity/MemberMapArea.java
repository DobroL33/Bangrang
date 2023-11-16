package com.ssafy.bangrang.domain.map.entity;

import com.ssafy.bangrang.domain.map.model.vo.RegionType;
import com.ssafy.bangrang.domain.member.entity.AppMember;
import com.ssafy.bangrang.global.common.entity.CommonEntity;
import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.MultiPolygon;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member_map_area")
@ToString(of = {"idx", "regionType", "dimension"})
public class MemberMapArea extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "member_map_area_idx")
    private Long idx;

    @Enumerated(EnumType.STRING)
    @Column(name = "member_map_area_region")
    private RegionType regionType;

    @Column(columnDefinition = "geometry(Geometry, 4326)")
    private Geometry shape;

    @Column(name = "member_map_area_dimension")
    private Double dimension;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_idx")
    private AppMember appMember;

    @Builder
    public MemberMapArea(RegionType regionType, Geometry shape, Double dimension, AppMember appMember, LocalDateTime customDate){
        this.regionType = regionType;
        this.shape = shape;
        this.dimension = dimension;
        this.changeAppMember(appMember);
        super.setCustomCreationDate(customDate);
    }

    private void changeAppMember(AppMember appMember) {
        this.appMember = appMember;
        appMember.getMemberMapAreas().add(this);
    }

    public void changeShapeAndDimension(Geometry shape, Double dimension){
        this.shape = shape;
        this.dimension = dimension;
    }
}
