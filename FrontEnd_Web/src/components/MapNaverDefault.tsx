import React, { useEffect, useRef } from "react";

interface Props{
  latitude:number;
  longtitude:number;
}
const MapNaverDefault: React.FC<Props> = ({latitude,longtitude }) => {
  const mapElement = useRef(null);
  const { naver } = window;

  useEffect(() => {
    if (!mapElement.current || !naver) return;

    // 지도에 표시할 위치의 위도와 경도 좌표를 파라미터로 넣어줍니다.
    const location = new window.naver.maps.LatLng(latitude, longtitude);
    const mapOptions = {
      center: location,
      zoom: 15,
      zoomControl: true,
    };

    const map = new naver.maps.Map(mapElement.current, mapOptions);
    new naver.maps.Marker({
      position: location,
      map,
    });
  }, [latitude]);

  return (
    <>
      {/* <h1>Naver Map - Default</h1> */}
      <div ref={mapElement} style={{ minHeight: "250px" }} />
    </>
  );
};

export default MapNaverDefault;
