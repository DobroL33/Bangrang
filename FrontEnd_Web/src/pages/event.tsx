import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axiosInstance from "../axiosinstance";
import styles from "./event.module.css";
import EventPhone from "../components/eventphone";
import EventDataPlus from "../components/eventdataplus";

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



const Event: React.FC = () => {

    const [eventList, setEventList] = useState<Event[]>([]);
    const [selectedEventIdx, setSelectedEventIdx] = useState<number|null>(null);
    const [isModalOpen, setModalOpen] = useState<boolean>(false);
    const [isModaloption,setModaloption] = useState<boolean>(false);
    const days = ["일", "월", "화", "수", "목", "금", "토"];
    const navigate = useNavigate();

  useEffect(() => {
    const AccessToken = localStorage.getItem("AccessToken");
    if (AccessToken) {
      if (localStorage.getItem("UserName")!=='admin@bangrang') {
        if (location.pathname.includes('/admin')) {
          navigate('/manage')
        } else {
          loaddata()
        }
      } else {
        if (location.pathname.includes('/manage')) {
          navigate('/admin')
        } else {
          loaddata()
        }
      }
    } else {
      navigate("/login");
    }
  }, [navigate]);

  const loaddata = () => {
    axiosInstance({
      method: "get",
      url: `${process.env.REACT_APP_API}/web/event`,
    }).then((res) => {
        setEventList(res.data);
        setSelectedEventIdx(null);
    }).catch((err) => console.log(err));
  };

  
  return (
    <div className={styles.homebox}
      style={{height: window.innerHeight - 80}}
    >
      <h1 style={{  margin: 0 }}>
        이벤트 등록 및 관리
      </h1>
      <div className={styles.funcbox}>
        <div style={{width: "70%"}}>
          <div className={styles.filterbar}>
            이벤트 리스트
            <button
          style={{
            background: "#1DAEFF",
            border: "none",
            borderRadius: "3px",
            width: "20%",
            padding: "6px",
            color: "white",
            fontWeight: "bold",
          }}
          onClick={()=>{setModaloption(false);setModalOpen(true);}}>
          이벤트 등록
        </button>
          </div>
          {eventList.length > 0 ?
          <div className={styles.databox}>
            {eventList.map((item, idx) => (
              <div key={idx} className={`${selectedEventIdx===item.eventIdx ? styles.sel_data:''} ${styles.data}`} 
              onClick={()=>{setSelectedEventIdx(item.eventIdx);}}>
                <div className={styles.data_idx}>{idx+1}</div>
                <div className={styles.data_info}>
                  <span>{item.title}</span>
                  <span>{item.subTitle}</span>
                </div>
                <div className={styles.data_date}>
                    <button
                        style={{
                            background: "#1DAEFF",
                            border: "none",
                            borderRadius: "3px",
                            width: "50%",
                            height: "25px",
                            marginTop: "5px",
                            color: "white",
                            fontWeight: "bold",
                        }}
                    onClick={()=>{setModaloption(true);setModalOpen(true);}}>
                    이벤트 수정
                </button>
                <button
                    style={{
                        background: "#1DAEFF",
                        border: "none",
                        borderRadius: "3px",
                        width: "50%",
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
                  {/* {new Date(item.startDate).getFullYear() === new Date().getFullYear() ? (
                    <span>
                      {new Date(item.startDate).getMonth() + 1 < 10
                        ? "0" + new Date(item.startDate).getMonth() + 1
                        : new Date(item.startDate).getMonth() + 1}
                      월{" "}
                      {new Date(item.startDate).getDate() < 10
                        ? "0" + new Date(item.startDate).getDate()
                        : new Date(item.startDate).getDate()}
                      일
                    </span>
                  ) : (
                    <span>
                      {new Date(item.startDate).getFullYear()}년{" "}
                      {new Date(item.startDate).getMonth() + 1 < 10
                        ? "0" + new Date(item.startDate).getMonth() + 1
                        : new Date(item.startDate).getMonth() + 1}
                      월{" "}
                      {new Date(item.startDate).getDate() < 10
                        ? "0" + new Date(item.startDate).getDate()
                        : new Date(item.startDate).getDate()}
                      일
                    </span>
                  )}
                  {new Date(item.startDate).getHours() <= 12 ? (
                    <span>
                      {new Date(item.startDate).getHours() < 10
                        ? "0" + new Date(item.startDate).getHours()
                        : new Date(item.startDate).getHours()}{" "}
                      :{" "}
                      {new Date(item.startDate).getMinutes() < 10
                        ? "0" + new Date(item.startDate).getMinutes()
                        : new Date(item.startDate).getMinutes()}{" "}
                      am
                    </span>
                  ) : (
                    <span>
                      {new Date(item.startDate).getHours() % 12 < 10
                        ? "0" + (new Date(item.startDate).getHours() % 12)
                        : new Date(item.startDate).getHours() % 12}{" "}
                      :{" "}
                      {new Date(item.startDate).getMinutes() < 10
                        ? "0" + new Date(item.startDate).getMinutes()
                        : new Date(item.startDate).getMinutes()}{" "}
                      pm
                    </span>
                  )} */}
                </div>
              </div>
            ))}
          </div>
          : 
          <div style={{width:'100%', height:'95%',display:'flex',alignItems:'center',justifyContent:'center'}}>
            <h1>등록된 이벤트가 없습니다 :)</h1>
          </div>}
        </div>
        <div className={styles.detailbox}>
        {selectedEventIdx ? (
            <div style={{ display: "flex", justifyContent: "center",height:'100%' }}>
            <EventPhone selEventIdx={selectedEventIdx}/>
            </div>
          ) :         
          <div>
            <div
            style={{
              height: "10%",
              fontSize: 40,
            }}
          >
           상세정보 조회
          </div>
            <br /><br />
          <h2>상세정보 조회를 하시려면<br />
          해당 이벤트를 클릭해주세요!</h2>
        </div>}
        </div>
      </div>
      {isModalOpen && <EventDataPlus closeModal={()=>setModalOpen(false)} isEventIdx={selectedEventIdx} modaltype={isModaloption} reload={()=>loaddata()} />}
    </div>
  );
};
export default Event;
