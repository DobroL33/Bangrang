package com.ssafy.bangrang;

import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.operation.buffer.BufferOp;
import org.locationtech.jts.operation.buffer.BufferParameters;
import org.locationtech.jts.operation.union.CascadedPolygonUnion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class SpatialTest {

    @Autowired
    private GeometryFactory geometryFactory;

    @Test
    void test(){
        List<Geometry> geometryList = new ArrayList<>();

        // 클라이언트로부터 받은 점의 좌표
        double x = 10.0; // 클라이언트로부터 받은 x 좌표
        double y = 20.0; // 클라이언트로부터 받은 y 좌표
        double sideLength = 0.15; // 정사각형 한 변의 길이 (15cm를 메터로 변환)

        // 정사각형의 좌표 배열 (시계방향)
        Coordinate[] coordinates = new Coordinate[5];
        coordinates[0] = new Coordinate(x - sideLength / 2, y - sideLength / 2);
        coordinates[1] = new Coordinate(x + sideLength / 2, y - sideLength / 2);
        coordinates[2] = new Coordinate(x + sideLength / 2, y + sideLength / 2);
        coordinates[3] = new Coordinate(x - sideLength / 2, y + sideLength / 2);
        coordinates[4] = coordinates[0]; // 다시 첫 번째 점으로 돌아감

        // Polygon 생성
        Polygon polygon1 = geometryFactory.createPolygon(coordinates);
        Polygon polygon2 = geometryFactory.createPolygon(coordinates);

        geometryList.add(polygon1);
        geometryList.add(polygon2);
        
        // Polygon 경계값
        LineString exteriorRing = polygon1.getExteriorRing();
        Coordinate[] exteriorRingCoordinates = exteriorRing.getCoordinates();

        for (Coordinate coordinate : exteriorRingCoordinates) {
            double Cx = coordinate.x; // x 좌표
            System.out.println("Cx = " + Cx);
            double Cy = coordinate.y; // y 좌표
            System.out.println("Cy = " + Cy);
        }


        // Union 연산
        Geometry unionResult = CascadedPolygonUnion.union(geometryList);

        System.out.println("unionResult = " + unionResult);

        // 경계값 얻기
        BufferParameters bufferParams = new BufferParameters();
        bufferParams.setEndCapStyle(BufferParameters.CAP_ROUND);
        bufferParams.setJoinStyle(BufferParameters.JOIN_ROUND);
        BufferOp bufferOp = new BufferOp(unionResult, bufferParams);
        Geometry boundary = bufferOp.getResultGeometry(0);
        System.out.println("boundary = " + boundary);


        double polygonArea = polygon1.getArea();
        System.out.println("Polygon의 면적 = " + polygonArea);

        double area = unionResult.getArea();
        System.out.println("면적 = " + area);

    }

}
