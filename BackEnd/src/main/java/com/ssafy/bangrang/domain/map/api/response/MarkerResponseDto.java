package com.ssafy.bangrang.domain.map.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarkerResponseDto {
    private Double space;
    private List<List<GeometryBorderCoordinate>> list;
}
