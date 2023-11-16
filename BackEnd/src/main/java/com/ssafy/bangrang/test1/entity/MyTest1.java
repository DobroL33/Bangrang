package com.ssafy.bangrang.test1.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;
import org.locationtech.jts.geom.Point;
import org.springframework.data.redis.core.index.GeoIndexed;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"title"})
public class MyTest1 {
    @Id
    @GeneratedValue
    Long id;

    String title;

    @Column(columnDefinition = "geometry(Point, 4326)")
    private Point position;

    @Builder
    public MyTest1(String title, Point position){
        this.title = title;
        this.position = position;
    }

}
