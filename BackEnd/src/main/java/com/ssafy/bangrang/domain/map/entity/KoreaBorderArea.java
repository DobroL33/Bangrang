package com.ssafy.bangrang.domain.map.entity;

import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.MultiPolygon;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "korea_border_area")
@ToString(of = {"idx", "bjcd", "name"})
public class KoreaBorderArea {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "korea_border_area_idx")
    private Long idx;

    @Column(columnDefinition = "geometry(MultiPolygon, 4326)", name = "korea_border_area_shape")
    private MultiPolygon shape;

    // 시도법정동코드
    @Column(name = "korea_border_area_bjcd")
    private String bjcd;

    // 시도명
    @Column(name = "korea_border_area_name")
    private String name;

}
