package com.ssafy.bangrang.domain.map.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeometryBorderCoordinate {
    private Double longitude;
    private Double latitude;
}
