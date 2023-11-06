import React,{ useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axiosInstance from "../axiosinstance";
import styles from './inquiry.module.css';

interface inquiry {
  inquiryIdx : Number;
  title : String;
  event : String;
  createdAt: Date;
  nickname : String;
}
interface Detailinquiry {
  inquiryIdx : Number;
  title : String;
  event : String;
  createdAt: Date;
  nickname : String;
}

const Inquiry: React.FC = () => {
  const [isInquiry, setInquiry] = useState<inquiry[]>([{inquiryIdx:1,title:"OO축제 운영 시간 문의",event:"<부산 불꽃놀이 축제>",createdAt:new Date(),nickname:"유저아이디"}])
  const [isDetail, setDetail] = useState<Number|null>(null)
  const [isDetailInqury, setDetailInqury] = useState<Detailinquiry>()
  const navigate = useNavigate()
  useEffect(()=>{
    const AccessToken = localStorage.getItem('AccessToken')
    if (AccessToken) {
      if (localStorage.getItem("UserName")!=='admin@bangrang') {
        navigate('/manage/inquiry')
      }
    } else {
      navigate('/login')
    }
  },[navigate])

  useEffect(()=>{
    loaddata()
  },[])

  useEffect(()=>{
    axiosInstance({
      method:'get',
      url:`${process.env.REACT_APP_API}/api/web/inquiry/${isDetail}`,
    }).then(res=>{
      setInquiry(res.data)
    }).catch(err=>{
      console.log(err)
    })
  },[isDetail])

  const loaddata = () => {
    axiosInstance({
      method:'get',
      url:`${process.env.REACT_APP_API}/api/web/inquiry`,
    }).then(res=>{
      setInquiry(res.data)
    }).catch(err=>{
      console.log(err)
    })
  }

  return (
    <div style={{width:'100%',height:(window.innerHeight-80), backgroundColor:'#E2F5FF',padding:'2%',boxSizing:"border-box"}}>
      <p style={{border:'1px solid black', margin:0,width:'100%'}}>1 : 1 문의</p>
      <div style={{width:'100%',height:'95%', border:'1px solid black', display:'flex'}}>
        <div style={{width:'30%', backgroundColor:'white',border:'1px solid black',}}>
          <div style={{width:'100%', height:'5%', backgroundColor:'red'}}>
            문의 리스트
          </div>
       
          <div style={{width:'100%', height:'95%', overflowY:'scroll'}}>
            {isInquiry.map((item, index) => (
              <div key={index} className={styles.inquiry_lst} 
              onClick={()=>setDetail(item.inquiryIdx)}>
                <div className={styles.inquiry_idx}>{index+1}</div>
                <div className={styles.inquiry_info}>
                  <span>{item.title}</span>
                  <span>{item.event}</span>
                </div>
                <div className={styles.inquiry_info2}>
                  {item.createdAt.getFullYear() === new Date().getFullYear() ?
                  <span>{item.createdAt.getMonth() + 1 < 10 ? '0'+item.createdAt.getMonth() + 1:item.createdAt.getMonth() + 1}월 {item.createdAt.getDate() < 10 ? '0'+item.createdAt.getDate():item.createdAt.getDate()}일</span>
                   : <span>{item.createdAt.getFullYear()}년 {item.createdAt.getMonth() + 1 < 10 ? '0'+item.createdAt.getMonth() + 1:item.createdAt.getMonth() + 1}월 {item.createdAt.getDate() < 10 ? '0'+item.createdAt.getDate():item.createdAt.getDate()}일</span>
                  }
                  <br />
                  {item.createdAt.getHours() <= 12 ? 
                  <span>{item.createdAt.getHours() < 10 ? '0'+(item.createdAt.getHours()):item.createdAt.getHours()} : {item.createdAt.getMinutes() < 10 ? '0'+item.createdAt.getMinutes():item.createdAt.getMinutes()} am</span>
                  :
                  <span>{item.createdAt.getHours()%12 < 10 ? '0'+(item.createdAt.getHours()%12):item.createdAt.getHours()%12} : {item.createdAt.getMinutes() < 10 ? '0'+item.createdAt.getMinutes():item.createdAt.getMinutes()} pm</span>}
                </div>
              </div>
            ))}
          </div>
        </div>
        <div style={{width:'70%', backgroundColor:'white',border:'1px solid black',}}>
            <h1>여기에 문의 내용, 답변달기 기능</h1>
        </div>
      </div>
    </div>

  );
};
export default Inquiry;
