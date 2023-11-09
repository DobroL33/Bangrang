import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axiosInstance from "../axiosinstance";
import styles from "./eventlist.module.css";
import { divide } from "lodash";
import axios from "axios";
import { Container as MapDiv, NaverMap, Marker } from "react-naver-maps";
import MapNaverDefault from "./MapNaverDefault";
import Navbar from "../components/navbar";

interface EventList<T> {
  data: T[];
}

interface Event {
  eventIdx: number;
  title: string;
  subTitle: string | null;
  eventImg: string | null;
  startDate: string;
  endDate: string;
  address: string;
  eventUrl: string | null;
}

interface DetailEvent {
  eventIdx: number;
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

interface inquiry {
  inquiryIdx: Number;
  title: string;
  event: string;
  createdAt: Date;
}
interface Detailinquiry {
  inquiryIdx: Number;
  title: string;
  content: string;
  createdAt: Date;
  event: string | null;
  nickname: string;
  comment: {
    commentIdx: Number;
    content: string;
    updatedAt: Date;
  } | null;
}

const EventList: React.FC = () => {
  const [isDetail, setDetail] = useState<DetailEvent | null>({
    eventIdx: 1,
    title: "",
    subTitle: "",
    startDate: "",
    endDate: "",
    content: "",
    address: "",
    longitude: 0,
    latitude: 0,
    image: "img",
    subImage: "subImage",
    eventUrl: "url",
    likeCount: 0,
  });

  const days = ["일", "월", "화", "수", "목", "금", "토"];
  const [eventList, setEventList] = useState<Event[]>([]);

  // 이벤트 리스트 불러오기
  useEffect(() => {
    axiosInstance({
      method: "get",
      url: `${process.env.REACT_APP_API}/web/event`,
    })
      .then((res) => {
        // console.log(res);
        setEventList(res.data);
      })
      .catch((err) => console.log(err));
  }, []);

  const navigate = useNavigate();
  useEffect(() => {
    if (localStorage.getItem("AccessToken")) {
      if (localStorage.getItem("UserName") === "admin@bangrang") {
        navigate("/admin");
      }
    } else {
      navigate("/login");
    }
  }, [navigate]);

  // const loaddata = () => {
  //   axiosInstance({
  //     method: "get",
  //     url: `${process.env.REACT_APP_API}/api/web/inquiry`,
  //   })
  //     .then((res) => {
  //       setInquiry(res.data);
  //     })
  //     .catch((err) => {
  //       console.log(err);
  //     });
  // };

  const detaildata = (eventIdx: Number) => {
    axiosInstance({
      method: "get",
      url: `${process.env.REACT_APP_API}/web/event/${eventIdx}`,
    })
      .then((res) => {
        setDetail(res.data);
      })
      .catch((err) => {
        console.log(err);
      });
  };

  return (
    <div
      style={{
        width: "100%",
        height: window.innerHeight - 80,
        backgroundColor: "#E2F5FF",
        padding: "3% 10%",
        boxSizing: "border-box",
      }}
    >
      <button onClick={() => navigate("/manage/eventregist")}>
        이벤트 작성
      </button>

      <div
        style={{
          width: "100%",
          height: "95%",
          border: "1px solid black",
          display: "flex",
        }}
      >
        <div
          style={{
            width: "30%",
            backgroundColor: "white",
            border: "1px solid black",
          }}
        >
          <div
            style={{
              width: "100%",
              height: "5%",
              backgroundColor: "blue",
              fontSize: "20px",
              color: "white",
            }}
          >
            이벤트 리스트
          </div>
          {eventList.length > 0 ? (
            <div style={{ width: "100%", height: "95%", overflowY: "scroll" }}>
              {eventList.map((item, index) => (
                <div
                  key={item.eventIdx}
                  className={`${
                    isDetail?.eventIdx === item.eventIdx
                      ? styles.sel_inquiry
                      : ""
                  } ${styles.inquiry_lst}`}
                  onClick={() => detaildata(item.eventIdx)}
                >
                  <div className={styles.inquiry_idx}>{index + 1}</div>
                  <div className={styles.inquiry_info}>
                    <span>{item.title}</span>
                    <span>{item.subTitle}</span>
                  </div>
                </div>
              ))}
            </div>
          ) : (
            <div
              style={{
                width: "100%",
                height: "95%",
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
              }}
            >
              <h1>등록된 이벤트가 없습니다 :)</h1>
            </div>
          )}
        </div>
        <div
          style={{
            width: "70%",
            backgroundColor: "white",
            border: "1px solid black",
          }}
        >
          <div
            style={{
              height: "10%",
              fontSize: 40,
              borderBottom: "1px solid black",
            }}
          >
            이벤트
          </div>
          {isDetail ? (
            <div style={{ display: "flex", justifyContent: "center" }}>
              <div
                style={{
                  width: "40%",
                  height: "80%",
                  border: " 1px solid black",
                  marginTop: "5%",
                  borderRadius: "10px",
                  textAlign: "left", // Add this line to make the content left-aligned
                }}
              >
                <img
                  src={`${isDetail.image}`}
                  style={{ width: "30%", height: "20%" }}
                  alt="축제 이미지"
                />
                {isDetail.subImage ? (
                  <img
                    src={isDetail.subImage}
                    style={{ width: "30%", height: "20%" }}
                    alt="서브 축제이미지"
                  />
                ) : null}
                <div>
                  <h3 style={{ textAlign: "left", paddingLeft: "10px" }}>
                    {isDetail.title}
                  </h3>
                  <p
                    style={{
                      marginTop: "2px",
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
                      marginTop: "2px",
                      color: "#949494",
                      fontSize: "12px",
                      textAlign: "left", // Add this line to make the content left-aligned
                      paddingLeft: "10px",
                    }}
                  >
                    {isDetail.startDate} ~ {isDetail.endDate}
                  </p>
                  <p
                    style={{
                      marginTop: "5px",
                      color: "#949494",
                      fontSize: "10px",
                      textAlign: "left", // Add this line to make the content left-aligned
                      paddingLeft: "10px",
                    }}
                  >
                    {isDetail.content}
                  </p>
                </div>

                <MapNaverDefault
                  latitude={isDetail.latitude}
                  longtitude={isDetail.longitude}
                />
                <button
                  onClick={() =>
                    navigate(`/manage/eventupdate/${isDetail.eventIdx}`)
                  }
                >
                  이벤트 수정
                </button>
              </div>
            </div>
          ) : null}
        </div>
      </div>
    </div>
  );
};
export default EventList;
