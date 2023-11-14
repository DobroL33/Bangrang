package com.ssafy.bangrang.domain.map.entity;

import com.ssafy.bangrang.domain.member.entity.AppMember;
import com.ssafy.bangrang.global.common.entity.CommonEntity;
import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.Point;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member_marker")
@ToString(of = {"idx"})
public class MemberMarker extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "member_marker_idx")
    private Long idx;

    @Column(columnDefinition = "geometry(Point, 4326)", name = "member_marker_location")
    private Point location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_idx")
    private AppMember appMember;

    @Builder
    public MemberMarker(Point location, AppMember appMember){
        this.location = location;
        this.changeAppMember(appMember);
    }

    private void changeAppMember(AppMember appMember) {
        this.appMember = appMember;
        appMember.getMemberMarkers().add(this);
    }

}
