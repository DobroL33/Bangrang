package com.ssafy.bangrang;

import com.ssafy.bangrang.test1.entity.MyTest1;
import com.ssafy.bangrang.test1.repository.MyTest1Repository;
import org.junit.Ignore;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class PostGisTest {

    @Autowired
    private MyTest1Repository myTest1Repository;

    @Autowired
    private GeometryFactory geometryFactory;

    @Test
    @Disabled
    void pointTest(){
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        Point point = geometryFactory.createPoint(new Coordinate(37.541, 126.986));
        System.out.println("point = " + point);
    }

    @Test
    @Ignore
    void lineStringTest(){
        List<CoordinateDto> coordinateDtos = new ArrayList<>();
        coordinateDtos.add(new CoordinateDto(37.541, 126.986));
        coordinateDtos.add(new CoordinateDto(37.541, 126.986));

        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        Coordinate[] coordinates = new Coordinate[coordinateDtos.size()];
        for (int i = 0; i < coordinateDtos.size(); i++) {
            coordinates[i] = new Coordinate(coordinateDtos.get(i).latitude, coordinateDtos.get(i).longitude);
        }
        LineString lineString = geometryFactory.createLineString(coordinates);
        System.out.println("lineString = " + lineString);
    }

    public class CoordinateDto {
        private double latitude;
        private double longitude;

        public CoordinateDto(double latitude, double longitude){
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }

    @Test
    @Ignore
    void polygonTest(){
        //이 밖에 GeometryFactory는 createPolygon(도형) 등 다양한 공간 데이터를 생성할 수 있다.
    }

    @Test
    void testTest(){
        Point point = geometryFactory.createPoint(new Coordinate(37.541, 126.986));
        System.out.println("point = " + point);
        MyTest1 saved = myTest1Repository.save(MyTest1.builder()
                .title("첫번째 point")
                .position(point)
                .build());

        Optional<MyTest1> found = myTest1Repository.findById(saved.getId());
        System.out.println("found = " + found);
        System.out.println("found.get().getPosition() = " + found.get().getPosition());
        
    }
}
