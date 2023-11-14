package com.ssafy.bangrang.domain.map.api.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddMarkersRequestDto {
    private Double longitude;
    private Double latitude;
}
