import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axiosInstance from "../axiosinstance";
import styles from "./inquiry.module.css";

interface inquiry {
  inquiryIdx: Number;
  title: string;
  event: string;
  createdAt: string;
}
interface Detailinquiry {
  inquiryIdx: Number;
  title: string;
  content: string;
  createdAt: string;
  event: string | null;
  nickname: string;
  comment: {
    commentIdx: Number;
    content: string;
    updatedAt: Date;
  } | null;
}

const Inquiry: React.FC = () => {
  const [isInquiry, setInquiry] = useState<inquiry[]>([])
  const [isDetail, setDetail] = useState<Detailinquiry|null>(null)
  const [isEdit, setEdit] = useState(false); // comment 수정 on/off
  const [isComment, setComment] = useState(""); // 수정할 comment 내용 담아두는 변수
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
      method:'get',
      url:`${process.env.REACT_APP_API}/web/inquiry`,
    }).then(res=>{
      setInquiry(res.data)
    }).catch(err=>{
      console.log(err)
    })
  };

  const detaildata = (idx: Number) => {
    axiosInstance({
      method:'get',
      url:`${process.env.REACT_APP_API}/web/inquiry/${idx}`,
    }).then(res=>{
      setDetail(res.data)
    }).catch(err=>{
      console.log(err)
    })
  };

  const commentchange = () => {
    if (isComment === "") {
      return alert("수정할 내용이 없음");
    }
    axiosInstance({
      method:'put',
      url:`${process.env.REACT_APP_API}/web/comment`,
      data:{
        commentIdx : isDetail?.comment?.commentIdx,
        content : isComment,
      }
    }).then(res=>{
      setDetail(prev => ({
        ...prev!,
        comment: {
          ...prev!.comment!,
          content: isComment,
        }
      }));
      setComment('');
      setEdit(false);
    }).catch(err=>{
      console.log(err)
      alert('수정실패')
    })
      .then((res) => {
        setDetail((prev) => ({
          ...prev!,
          comment: {
            ...prev!.comment!,
            content: isComment,
          },
        }));
        setComment("");
        setEdit(false);
      })
      .catch((err) => {
        console.log(err);
        alert("수정실패");
      });
  };

  const deletecomment = () => {
    axiosInstance({
      method:'delete',
      url:`${process.env.REACT_APP_API}/web/comment`,
      data:{
        commentIdx : isDetail?.comment?.commentIdx,
      }
    }).then(res=>{
      setDetail(prev => ({
        ...prev!,
        comment: null
      }));
    }).catch(err=>{
      console.log(err)
      alert('삭제실패')
    })
      .then((res) => {
        setDetail((prev) => ({
          ...prev!,
          comment: null,
        }));
      })
      .catch((err) => {
        console.log(err);
        alert("삭제실패");
      });
  };

  const commentregist = () => {
    if (isComment === "") {
      return;
    }
    axiosInstance({
      method:'post',
      url:`${process.env.REACT_APP_API}/web/comment`,
      data:{
        inquiryIdx : isDetail?.inquiryIdx,
        content : isComment,
      }
    }).then(res=>{
      detaildata(isDetail?.inquiryIdx!);
      setComment('');
    }).catch(err=>{
      console.log(err)
      alert('등록실패')
    })
      .then((res) => {
        detaildata(isDetail?.inquiryIdx!);
        setComment("");
      })
      .catch((err) => {
        console.log(err);
        alert("등록실패");
      });
  };

  const commentrefuse = () => {
    axiosInstance({
      method:'delete',
      url:`${process.env.REACT_APP_API}/web/inquiry`,
      data:{
        inquiryIdx : isDetail?.inquiryIdx,
      }
    }).then(res=>{
      setDetail(null);
      loaddata();
    }).catch(err=>{
      console.log(err)
      alert('답변 거절 실패')
    })
      .then((res) => {
        setDetail(null);
        loaddata();
      })
      .catch((err) => {
        console.log(err);
        alert("답변 거절 실패");
      });
  };
  return (
    <div className={styles.homebox}
      style={{height: window.innerHeight - 80}}
    >
      <h1 style={{  margin: 0 }}>
        1 : 1 문의 업무
      </h1>
      <div className={styles.funcbox}>
        <div style={{width: "30%"}}>
          <div className={styles.filterbar}>
            {/* 필터 :  */}
            문의 리스트
          </div>
          {isInquiry.length > 0 ?
          <div className={styles.databox}>
            {isInquiry.map((item, idx) => (
              <div key={idx} className={`${isDetail?.inquiryIdx===item.inquiryIdx ? styles.sel_data:''} ${styles.data}`} 
              onClick={()=>detaildata(item.inquiryIdx)}>
                <div className={styles.data_idx}>{idx+1}</div>
                <div className={styles.data_info}>
                  <span>{item.title}</span>
                  <span>{item.event}</span>
                </div>
                <div className={styles.data_date}>
                  {new Date(item.createdAt).getFullYear() === new Date().getFullYear() ? (
                    <span>
                      {new Date(item.createdAt).getMonth() + 1 < 10
                        ? "0" + new Date(item.createdAt).getMonth() + 1
                        : new Date(item.createdAt).getMonth() + 1}
                      월{" "}
                      {new Date(item.createdAt).getDate() < 10
                        ? "0" + new Date(item.createdAt).getDate()
                        : new Date(item.createdAt).getDate()}
                      일
                    </span>
                  ) : (
                    <span>
                      {new Date(item.createdAt).getFullYear()}년{" "}
                      {new Date(item.createdAt).getMonth() + 1 < 10
                        ? "0" + new Date(item.createdAt).getMonth() + 1
                        : new Date(item.createdAt).getMonth() + 1}
                      월{" "}
                      {new Date(item.createdAt).getDate() < 10
                        ? "0" + new Date(item.createdAt).getDate()
                        : new Date(item.createdAt).getDate()}
                      일
                    </span>
                  )}
                  {new Date(item.createdAt).getHours() <= 12 ? (
                    <span>
                      {new Date(item.createdAt).getHours() < 10
                        ? "0" + new Date(item.createdAt).getHours()
                        : new Date(item.createdAt).getHours()}{" "}
                      :{" "}
                      {new Date(item.createdAt).getMinutes() < 10
                        ? "0" + new Date(item.createdAt).getMinutes()
                        : new Date(item.createdAt).getMinutes()}{" "}
                      am
                    </span>
                  ) : (
                    <span>
                      {new Date(item.createdAt).getHours() % 12 < 10
                        ? "0" + (new Date(item.createdAt).getHours() % 12)
                        : new Date(item.createdAt).getHours() % 12}{" "}
                      :{" "}
                      {new Date(item.createdAt).getMinutes() < 10
                        ? "0" + new Date(item.createdAt).getMinutes()
                        : new Date(item.createdAt).getMinutes()}{" "}
                      pm
                    </span>
                  )}
                </div>
              </div>
            ))}
          </div>
          : 
          <div style={{width:'100%', height:'95%',display:'flex',alignItems:'center',justifyContent:'center'}}>
            <h1>등록된 문의가 없습니다 :)</h1>
          </div>}
        </div>
        <div className={styles.detailbox}>
          <div
            style={{
              height: "10%",
              fontSize: 40,
            }}
          >
           상세정보 조회
          </div>
          {isDetail ? (
            <div style={{ height: "90%" }}>
              <div
                style={{
                  height: "60%",
                  padding: "2%",
                  boxSizing: "border-box",
                }}>
                <p style={{textAlign:'end',margin:'0 10%'}}>
                  작성자 : {isDetail.nickname}
                </p>
              

                <div className={styles.detail_databox}>
                  <div className={styles.detail_data}>
                    <div className={styles.detail_type}
                    style={{borderTopLeftRadius:'1rem'}}>
                      문의 제목
                    </div>
                    <div className={styles.detail_content}>
                      {isDetail.title} ({isDetail.event})
                    </div>
                  </div>

                  <div className={styles.detail_data}>
                    <div className={styles.detail_type}>
                      문의 등록일
                    </div>
                    <div className={styles.detail_content}>
                      <span>
                        {new Date(isDetail.createdAt).getFullYear()}년{" "}
                        {new Date(isDetail.createdAt).getMonth() + 1 < 10
                          ? "0" + new Date(isDetail.createdAt).getMonth() + 1
                          : new Date(isDetail.createdAt).getMonth() + 1}
                        월{" "}
                        {new Date(isDetail.createdAt).getDate() < 10
                          ? "0" + new Date(isDetail.createdAt).getDate()
                          : new Date(isDetail.createdAt).getDate()}
                        일 ({days[new Date(isDetail.createdAt).getDay()]})
                      </span>
                      {new Date(isDetail.createdAt).getHours() <= 12 ? (
                        <span>
                          　
                          {new Date(isDetail.createdAt).getHours() < 10
                            ? "0" + new Date(isDetail.createdAt).getHours()
                            : new Date(isDetail.createdAt).getHours()}{" "}
                          :{" "}
                          {new Date(isDetail.createdAt).getMinutes() < 10
                            ? "0" + new Date(isDetail.createdAt).getMinutes()
                            : new Date(isDetail.createdAt).getMinutes()}{" "}
                          am
                        </span>
                      ) : (
                        <span>
                          　
                          {new Date(isDetail.createdAt).getHours() % 12 < 10
                            ? "0" + (new Date(isDetail.createdAt).getHours() % 12)
                            : new Date(isDetail.createdAt).getHours() % 12}{" "}
                          :{" "}
                          {new Date(isDetail.createdAt).getMinutes() < 10
                            ? "0" + new Date(isDetail.createdAt).getMinutes()
                            : new Date(isDetail.createdAt).getMinutes()}{" "}
                          pm
                        </span>
                      )}
                    </div>
                  </div>

                  <div
                    style={{
                      display: "flex",
                      textAlign: "start",
                      height: "70%",
                    }}
                  >
                    <div className={styles.detail_type}
                      style={{
                        border: "none",
                        alignItems:'start',
                        paddingTop: "5px",
                        borderBottomLeftRadius:'1rem'
                      }}
                    >
                      문의 내용
                    </div>
                    <div className={styles.detail_content}
                      style={{
                        borderBottom: "none",
                        alignItems:'start',
                        paddingTop: "5px"}}
                    >
                      {isDetail.content}
                    </div>
                  </div>
                </div>
              </div>
              {!isDetail.comment ? (
                <div
                  style={{
                    height: "40%",
                    padding: "2% 12%",
                    boxSizing: "border-box",
                  }}
                >
                  <div
                    style={{ display: "flex", justifyContent: "space-between" }}
                  >
                    문의 응답
                    <div>
                      <button onClick={commentregist}>등록</button>
                      <button style={{marginLeft:'3px'}} onClick={commentrefuse}>답변거절</button>
                    </div>
                  </div>
                    <textarea
                      className={styles.textbox}
                      cols={100}
                      rows={100}
                      value={isComment}
                      onChange={(e) => setComment(e.target.value)}/>
                </div>
              ) : (
                <div
                  style={{
                    height: "40%",
                    padding: "2% 12%",
                    boxSizing: "border-box",
                  }}
                >
                  <div
                    style={{ display: "flex", justifyContent: "space-between" }}
                  >
                    답변
                    {isEdit ? (
                      <div>
                        <button onClick={commentchange}>완료</button>
                        <button style={{marginLeft:'3px'}} 
                          onClick={() => {
                            setEdit(false);
                            setComment("");
                          }}
                        >
                          취소
                        </button>
                      </div>
                    ) : (
                      <div>
                        <button
                          onClick={() => {
                            setComment(isDetail.comment?.content!);
                            setEdit(true);
                          }}
                        >
                          수정
                        </button>
                        <button style={{marginLeft:'3px'}} onClick={deletecomment}>삭제</button>
                      </div>
                    )}
                  </div>
                  {isEdit ? (
                    <textarea
                    className={styles.textbox}
                    cols={100}
                    rows={100}
                    value={isComment}
                    onChange={(e) => setComment(e.target.value)}/>
                  ) : (
                    <div className={styles.detail_comment}>
                      {isDetail.comment.content}
                    </div>
                  )}
                </div>
              )}
            </div>
          ) :             
          <div>
            <br /><br />
          <h2>상세정보 조회를 하시려면<br />
          해당 문의를 클릭해주세요!</h2>
        </div>        }
        </div>
      </div>
    </div>
  );
};
export default Inquiry;
