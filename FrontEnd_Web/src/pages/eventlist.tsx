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

const EventList: React.FC = () => {
  const [selectedEventIdx, setSelectedEventIdx] = useState(0);
  const [nowStartDate, setNowStartDate] = useState("");
  const [nowEndDate, setNowEndDate] = useState("");
  const [isDetail, setDetail] = useState<DetailEvent | null>({
    // eventIdx: 0,
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
    loaddata();
  }, []);

  const loaddata = () => {
    axiosInstance({
      method: "get",
      url: `${process.env.REACT_APP_API}/web/event`,
    })
      .then((res) => {
        // console.log(res);
        setEventList(res.data);
        setDetail(null);
      })
      .catch((err) => console.log(err));
  };

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

  const detaildata = (eventIdx: Number) => {
    axiosInstance({
      method: "get",
      url: `${process.env.REACT_APP_API}/web/event/${eventIdx}`,
    })
      .then((res) => {
        console.log(res);
        setDetail(res.data);
        const [datePart, timePart] = res.data.startDate.split("T");
        setNowStartDate(datePart + " " + timePart);
        const [edatePart, etimePart] = res.data.endDate.split("T");
        setNowEndDate(edatePart + " " + etimePart);
      })
      .catch((err) => {
        console.log(err);
      });
  };

  return (
    <div
      style={{
        width: "100%",
        // height: window.innerHeight - 80,
        backgroundColor: "#E2F5FF",
        padding: "3% 10%",
        boxSizing: "border-box",
      }}
    >
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
              height: "30px",
              backgroundColor: "blue",
              fontSize: "20px",
              color: "white",
            }}
          >
            이벤트 리스트
          </div>
          {eventList.length > 0 ? (
            <div style={{ width: "100%", height: "95%", overflowY: "auto" }}>
              {eventList.map((item, idx) => (
                <div
                  key={idx}
                  onClick={() => {
                    detaildata(item.eventIdx);
                    setSelectedEventIdx(item.eventIdx);
                  }}
                  className={`${
                    selectedEventIdx === item.eventIdx ? styles.sel_inquiry : ""
                  } ${styles.inquiry_lst}`}
                >
                  <div className={styles.inquiry_idx}>{idx + 1}</div>
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
              height: "50px",
              fontSize: 40,
              borderBottom: "1px solid black",
            }}
          >
            이벤트
          </div>
          <div>
            {isDetail ? (
              <div style={{ display: "flex", justifyContent: "center" }}>
                <div
                  style={{
                    padding: "5px",
                    width: "40%",
                    height: "90%",
                    border: "2px solid black",
                    // marginTop: "2%",
                    borderRadius: "10px",
                    textAlign: "left",
                  }}
                >
                  <img
                    src={`${isDetail.image}`}
                    style={{ width: "100%", height: "20vh" }}
                    alt="축제 이미지"
                  />
                  {/* {isDetail.subImage ? (
                    <img
                      src={isDetail.subImage}
                      style={{ width: "30%", height: "20%" }}
                      alt="서브 축제이미지"
                    />
                  ) : null} */}
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
                    <button
                      style={{
                        background: "#1DAEFF",
                        border: "none",
                        borderRadius: "3px",
                        width: "30%",
                        height: "25px",
                        marginTop: "5px",
                        color: "white",
                        fontWeight: "bold",
                      }}
                      onClick={() => {
                        navigate(`/manage/eventupdate/${selectedEventIdx}`);
                      }}
                    >
                      이벤트 수정
                    </button>
                    <button
                      style={{
                        background: "#1DAEFF",
                        border: "none",
                        borderRadius: "3px",
                        width: "30%",
                        marginTop: "5px",
                        color: "white",
                        fontWeight: "bold",
                      }}
                      onClick={() => {
                        axiosInstance
                          .delete(
                            `${process.env.REACT_APP_API}/web/event/${selectedEventIdx}`
                          )
                          .then((res) => loaddata())
                          .catch((err) => console.log(err));
                      }}
                    >
                      이벤트 삭제
                    </button>
                  </div>
                </div>
              </div>
            ) : null}
          </div>
        </div>
      </div>
      <br />
      <div style={{ position: "absolute", right: "9.5%", width: "10%" }}>
        <button
          style={{
            background: "#1DAEFF",
            border: "none",
            borderRadius: "3px",
            width: "100%",
            padding: "6px",
            color: "white",
            fontWeight: "bold",
          }}
          onClick={() => navigate("/manage/eventregist")}
        >
          이벤트 작성
        </button>
      </div>
    </div>
  );
};
export default EventList;
