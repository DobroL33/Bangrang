package com.ssafy.bangrang.domain.map.service;

import com.ssafy.bangrang.domain.map.api.request.AddMarkersRequestDto;
import com.ssafy.bangrang.domain.map.api.response.GeometryBorderCoordinate;
import com.ssafy.bangrang.domain.map.entity.MemberMapArea;
import com.ssafy.bangrang.domain.map.model.vo.RegionType;
import com.ssafy.bangrang.domain.map.repository.MemberMapAreaRepository;
import com.ssafy.bangrang.domain.member.entity.AppMember;
import com.ssafy.bangrang.domain.member.repository.AppMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.operation.union.CascadedPolygonUnion;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MemberMapAreaServiceImpl implements MemberMapAreaService{

    private final MemberMapAreaRepository memberMapAreaRepository;
    private final AppMemberRepository appMemberRepository;
    private final GeometryFactory geometryFactory;

    // 정사각형 한 변의 길이
    private final double sideLength = 0.002;

    @Transactional
    @Override
    public List<List<GeometryBorderCoordinate>> addMeberMapArea(UserDetails userDetails, List<AddMarkersRequestDto> addMarkersRequestDtoList){
        AppMember appMember = appMemberRepository.findById(userDetails.getUsername()).orElseThrow();

        List<Geometry> geometryList = addMarkersRequestDtoList.stream().map(res -> {
            // 정사각형의 좌표 배열 (시계방향)
            Coordinate[] coordinates = new Coordinate[5];
            coordinates[0] = new Coordinate(res.getLongitude() - sideLength / 2, res.getLatitude() - sideLength / 2);
            coordinates[1] = new Coordinate(res.getLongitude() + sideLength / 2, res.getLatitude() - sideLength / 2);
            coordinates[2] = new Coordinate(res.getLongitude() + sideLength / 2, res.getLatitude() + sideLength / 2);
            coordinates[3] = new Coordinate(res.getLongitude() - sideLength / 2, res.getLatitude() + sideLength / 2);
            coordinates[4] = coordinates[0];

            // Polygon 생성
            Polygon polygon = geometryFactory.createPolygon(coordinates);

            return polygon;
        }).collect(Collectors.toList());

        // Union 연산
        Geometry curUnionResult = CascadedPolygonUnion.union(geometryList);

        // 제일 최신의 MemberMapArea을 DB로부터 불러오는 로직
        // 만약 최신 MemberMapArea가 오늘 날짜(yyyymmdd)면 update, 아니면 create
        // 가장 최근에 생성된 엔티티를 불러오는 메서드
        Optional<MemberMapArea> recent = memberMapAreaRepository.findTopByAppMemberIdxOrderByCreatedAtDesc(appMember.getIdx());
        if(recent.isPresent()){
            MemberMapArea befoMemberMapArea = recent.get();

            LocalDate currentDate = LocalDate.now(); // 현재 날짜를 가져옵니다.
            LocalDate createdAtDate = befoMemberMapArea.getCreatedAt().toLocalDate(); // 'createdAt'의 날짜 부분을 추출합니다.

            if (createdAtDate.isEqual(currentDate)) {
                // 'createdAt'의 날짜가 현재 날짜와 같은 경우, 객체 union
                Geometry befoPolygon = befoMemberMapArea.getShape();

                GeometryCollection geometryCollection = new GeometryCollection(new Geometry[] {curUnionResult, befoPolygon}, new GeometryFactory());
                Geometry unionResult = geometryCollection.union();

                befoMemberMapArea.changeShapeAndDimension(unionResult, unionResult.getArea());

                return getBorderPointListOuter(unionResult);

            } else {
                // 'createdAt'의 날짜가 현재 날짜와 다른 경우, 객체 생성
                saveMemberMapArea(RegionType.KOREA, curUnionResult, appMember);
                return getBorderPointListOuter(curUnionResult);
            }
        }else{
            // 새 데이터를 넣는 경우
            saveMemberMapArea(RegionType.KOREA, curUnionResult, appMember);
            return getBorderPointListOuter(curUnionResult);
        }

    }
    private List<List<GeometryBorderCoordinate>> getBorderPointListOuter(Geometry geometry){
        List<List<GeometryBorderCoordinate>> result = new ArrayList<>();
        // 경계 객체를 얻는다.(LineString or MultiLineString)
        Geometry boundary = geometry.getBoundary();
//        getCoordinates <- LineString

        if(boundary instanceof LineString){
            result.add(getBorderPointList((LineString) boundary));
        }else if(boundary instanceof MultiLineString){
            result = getBorderPointList((MultiLineString) boundary);
        }

        return result;
    }

    private List<GeometryBorderCoordinate> getBorderPointList(LineString lineString){
        List<GeometryBorderCoordinate> result = new ArrayList<>();

        CoordinateSequence coordinateSequence = lineString.getCoordinateSequence();
        for (int i = 0; i < coordinateSequence.size(); i++) {
            Coordinate coord = coordinateSequence.getCoordinate(i);
            result.add(GeometryBorderCoordinate
                    .builder()
                            .longitude(coord.x)
                            .latitude(coord.y)
                    .build());
        }

        return result;
    }

    private List<List<GeometryBorderCoordinate>> getBorderPointList(MultiLineString multiLineString){
        List<List<GeometryBorderCoordinate>> result = new ArrayList<>();
        int numLineStrings = multiLineString.getNumGeometries();

        for (int i = 0; i < numLineStrings; i++) {
            Geometry geometry = multiLineString.getGeometryN(i);
            if (geometry instanceof LineString) {
                result.add(getBorderPointList((LineString) geometry));
            }
        }

        return result;
    }

    private List<List<GeometryBorderCoordinate>> getBorderPointList(MultiPolygon multiPolygon) {
        List<List<GeometryBorderCoordinate>> result = new ArrayList<>();

        for (int i = 0; i < multiPolygon.getNumGeometries(); i++) {
            Polygon polygon = (Polygon) multiPolygon.getGeometryN(i);
            result.add(this.getBorderPointList(polygon));
        }

        return result;
    }

    private List<GeometryBorderCoordinate> getBorderPointList(Polygon polygon){
        List<GeometryBorderCoordinate> result = new ArrayList<>();
        for (Coordinate coordinate : polygon.getCoordinates()) {
            result.add(GeometryBorderCoordinate.builder()
                            .longitude(coordinate.getX())
                            .latitude(coordinate.getY())
                    .build());
        }
        return result;
    }

    // saveMemberMapArea에서 error가 생기면 이 메서드를 사용하는 모든 메서드 rollback
    @Transactional(propagation = Propagation.REQUIRED)
    MemberMapArea saveMemberMapArea(RegionType regionType, Geometry shape, AppMember appMember){
        MemberMapArea newMemberMapArea = MemberMapArea.builder()
                .regionType(regionType)
                .shape(shape)
                .dimension(shape.getArea())
                .appMember(appMember)
                .build();

        return memberMapAreaRepository.save(newMemberMapArea);
    }




}
