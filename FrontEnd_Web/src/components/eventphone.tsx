import React, { useEffect, useState } from "react";
import axiosInstance from "../axiosinstance";
import MapNaverDefault from "./MapNaverDefault";
import styles from "./eventphone.module.css";

interface DetailEvent {
    // eventIdx: number;
    title: string;
    subTitle: string | null;
    content: string;
    startDate: string;
    endDate: string;
    address: string;
    longitude: number;
    latitude: number;
    image: string | null;
    subImage: string | null;
    eventUrl: string | null;
    likeCount: number;
}

interface Props {
    selEventIdx : number;
}
const EventPhone: React.FC<Props> = ({selEventIdx}) => {
    const [isDetail, setDetail] = useState<DetailEvent|null>(null);

    const [nowStartDate, setNowStartDate] = useState("");
    const [nowEndDate, setNowEndDate] = useState("");

    useEffect(()=>{
        detaildata(selEventIdx);
    },[selEventIdx])

    const detaildata = (eventIdx: Number) => {
        axiosInstance({
          method: "get",
          url: `${process.env.REACT_APP_API}/web/event/${eventIdx}`,
        }).then((res) => {
            console.log(res);
            setDetail(res.data);
            const [datePart, timePart] = res.data.startDate.split("T");
            setNowStartDate(datePart + " " + timePart);
            const [edatePart, etimePart] = res.data.endDate.split("T");
            setNowEndDate(edatePart + " " + etimePart);
        }).catch((err) => {
            console.log(err);
        });
      };

    return (
    <div className={styles.phonebox}>
        {isDetail ? 
        <div>
            <img
            src={`${isDetail.image}`}
            style={{ width: "100%", height: "250px" }}
            alt="축제 이미지"
            />
        <div>
        <h3
            style={{
                textAlign: "left",
                paddingLeft: "10px",
                margin: 0,
            }}
            >
            {isDetail.title}
        </h3>
        <p
            style={{
                margin: 0,
                color: "#949494",
                fontSize: "12px",
                textAlign: "left", // Add this line to make the content left-aligned
                paddingLeft: "10px",
            }}
            >
            {isDetail.address}
        </p>
        <p
            style={{
                marginTop: "10px",
                marginBottom: 0,
                color: "#949494",
                fontSize: "10px",
                textAlign: "left", // Add this line to make the content left-aligned
                paddingLeft: "10px",
            }}
            >
            시작 날짜 : {nowStartDate}
        </p>
        <p
            style={{
                margin: 0,
                color: "#949494",
                fontSize: "10px",
                textAlign: "left", // Add this line to make the content left-aligned
                paddingLeft: "10px",
            }}
            >
            종료 날짜 : {nowEndDate}
        </p>
        <p
            style={{
                color: "#949494",
                fontSize: "10px",
                textAlign: "left", // Add this line to make the content left-aligned
                paddingLeft: "10px",
            }}
            >
            {isDetail.content}
        </p>
        {isDetail.subImage ? (
            <img
            src={isDetail.subImage}
            style={{ width: "100%", height: "auto" }}
            alt="서브 축제이미지"
            />
            ) : null}
        </div>

        <MapNaverDefault
        latitude={isDetail.latitude}
        longtitude={isDetail.longitude}
        />
        <div
        style={{
            display: "flex",
            justifyContent: "space-between",
        }}
        >
        </div>
        </div>
        :
        <div>
            <h1>이벤트 데이터 로딩실패 <br />잠시 후, 다시시도해주세요</h1>
        </div> }
    </div>
  )
};
export default EventPhone;
