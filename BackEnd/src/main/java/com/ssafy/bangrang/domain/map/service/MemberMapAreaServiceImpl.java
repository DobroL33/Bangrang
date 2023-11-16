package com.ssafy.bangrang.domain.map.service;

import com.ssafy.bangrang.domain.map.api.request.AddMarkersRequestDto;
import com.ssafy.bangrang.domain.map.api.response.GeometryBorderCoordinate;
import com.ssafy.bangrang.domain.map.api.response.MarkerResponseDto;
import com.ssafy.bangrang.domain.map.entity.MemberMapArea;
import com.ssafy.bangrang.domain.map.model.vo.RegionType;
import com.ssafy.bangrang.domain.map.repository.MemberMapAreaRepository;
import com.ssafy.bangrang.domain.member.entity.AppMember;
import com.ssafy.bangrang.domain.member.repository.AppMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.operation.union.CascadedPolygonUnion;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    public MarkerResponseDto addMeberMapArea(UserDetails userDetails, List<AddMarkersRequestDto> addMarkersRequestDtoList){
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
        log.info("현재 client로부터 받은 객체가 Empty인지 확인"+curUnionResult.isEmpty());

        // 제일 최신의 MemberMapArea을 DB로부터 불러오는 로직
        // 만약 최신 MemberMapArea가 오늘 날짜(yyyymmdd)면 update, 아니면 create
        // 가장 최근에 생성된 엔티티를 불러오는 메서드
        List<MemberMapArea> recent = memberMapAreaRepository.findTopByAppMemberIdxOrderByCreatedAtDesc(appMember.getIdx(), RegionType.KOREA, PageRequest.of(0, 1));
        if(curUnionResult.isEmpty()){
            if(recent.size() >= 1) {
                List<List<GeometryBorderCoordinate>> list = getBorderPointListOuter(recent.get(0).getShape());
                return MarkerResponseDto
                        .builder()
                        .space(curUnionResult.getArea())
                        .list(list)
                        .build();
            }else{
                return MarkerResponseDto.builder().build();
            }
        }

        if(recent.size() >= 1){
            MemberMapArea befoMemberMapArea = recent.get(0);
            Geometry befoPolygon = befoMemberMapArea.getShape();
            log.info("db로부터 가져온 객체가 null인지 확인"+befoPolygon.isEmpty());

            LocalDate currentDate = LocalDate.now(); // 현재 날짜
            LocalDate createdAtDate = befoMemberMapArea.getCreatedAt().toLocalDate(); // 'createdAt'의 날짜 부분을 추출합니다.

            // union 객체
            Geometry unionResult = CascadedPolygonUnion.union(Arrays.asList(new Geometry[] {curUnionResult, befoPolygon}));

            // 오늘 이미 만든 객체가 있다면 값만 변경
            if(createdAtDate.isEqual(currentDate)){
                befoMemberMapArea.changeShapeAndDimension(unionResult, unionResult.getArea());
                List<List<GeometryBorderCoordinate>> list = getBorderPointListOuter(unionResult);
                Double space = 0.0;
                if(!unionResult.contains(befoPolygon)) {
                    Geometry intersection = curUnionResult.difference(unionResult);
                    space = intersection.getArea()* 111319.49079327357 * 111319.49079327357;
                }

                return MarkerResponseDto
                        .builder()
                        .space(space)
                        .list(list)
                        .build();
            // 어제 객체라면 어제 객체 + 현재 객체값을 가진 새 데이터 생성
            }else{
                saveMemberMapArea(RegionType.KOREA, unionResult, appMember);
                List<List<GeometryBorderCoordinate>> list = getBorderPointListOuter(unionResult);

                Double space = 0.0;
                if(!unionResult.contains(befoPolygon)) {
                    Geometry intersection = curUnionResult.difference(unionResult);
                    space = intersection.getArea()* 111319.49079327357 * 111319.49079327357;
                }

                return MarkerResponseDto
                        .builder()
                        .space(space)
                        .list(list)
                        .build();
            }
        }else{
            // 새 데이터를 넣는 경우
            saveMemberMapArea(RegionType.KOREA, curUnionResult, appMember);
//            return getBorderPointListOuter(curUnionResult);
            List<List<GeometryBorderCoordinate>> list = getBorderPointListOuter(curUnionResult);
            return MarkerResponseDto
                    .builder()
                    .space(curUnionResult.getArea())
                    .list(list)
                    .build();
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

    // saveMemberMapArea 에서 error가 생기면 이 메서드를 사용하는 모든 메서드 rollback
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
