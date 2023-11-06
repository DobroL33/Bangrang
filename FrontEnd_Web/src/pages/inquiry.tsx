import React,{ useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axiosInstance from "../axiosinstance";
import styles from './inquiry.module.css';

interface inquiry {
  inquiryIdx : Number;
  title : string;
  event : string;
  createdAt: Date;
}
interface Detailinquiry {
  inquiryIdx : Number;
  title : string;
  content : string;
  createdAt: Date;
  event : string|null;
  nickname : string;
  comment : {
    commentIdx : Number;
    content : string;
    updatedAt : Date;
  }|null;
}

const Inquiry: React.FC = () => {
  const [isInquiry, setInquiry] = useState<inquiry[]>([{inquiryIdx:1,title:"OO축제 운영 시간 문의",event:"<부산 불꽃놀이 축제>",createdAt:new Date()},{inquiryIdx:2,title:"OO축제 운영 시간 문의",event:"<부산 불꽃놀이 축제>",createdAt:new Date()},{inquiryIdx:3,title:"OO축제 운영 시간 문의ddddddddddddddddddddddddddddddddd",event:"<부산 불꽃놀이 축제>",createdAt:new Date()}])
  const [isDetail, setDetail] = useState<Detailinquiry|null>(
    {inquiryIdx:1, title:"OO축제 운영 시간 문의",event:"<부산 불꽃놀이 축제>",createdAt:new Date(),content:"asdfads",nickname:"casdf",
  comment:null}
  )
  const [isEdit, setEdit] = useState(false); // comment 수정 on/off
  const [isComment, setComment] = useState('');  // 수정할 comment 내용 담아두는 변수
  const days = ['일', '월', '화', '수', '목', '금', '토'];
  const navigate = useNavigate();

  useEffect(()=>{
    const AccessToken = localStorage.getItem('AccessToken')
    if (AccessToken) {
      if (localStorage.getItem("UserName")!=='admin@bangrang') {
        if (location.pathname.includes('/admin')) {
          navigate('/manage')
        }
      } else {
        if (location.pathname.includes('/manage')) {
          navigate('/admin')
        }
      }
    } else {
      navigate('/login')
    }
  },[navigate])

  useEffect(()=>{
    loaddata()
  },[])

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

  const detaildata = (idx : Number) => {
    axiosInstance({
      method:'get',
      url:`${process.env.REACT_APP_API}/api/web/inquiry/${idx}`,
    }).then(res=>{
      setDetail(res.data)
    }).catch(err=>{
      console.log(err)
    })
  }

  const commentchange = () => {
    if (isComment==='') {
      return alert('수정할 내용이 없음')
    }
    axiosInstance({
      method:'put',
      url:`${process.env.REACT_APP_API}/api/web/comment`,
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
  }

  const deletecomment = () => {
    axiosInstance({
      method:'delete',
      url:`${process.env.REACT_APP_API}/api/web/comment`,
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
  }

  const commentregist = () => {
    if (isComment === '') {
      return 
    }
    axiosInstance({
      method:'post',
      url:`${process.env.REACT_APP_API}/api/web/comment`,
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
  }

  const commentrefuse = () => {
    axiosInstance({
      method:'delete',
      url:`${process.env.REACT_APP_API}/api/web/inquiry`,
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
  }
  return (
    <div style={{width:'100%',height:(window.innerHeight-80), backgroundColor:'#E2F5FF',padding:'3% 10%',boxSizing:"border-box"}}>
      <p style={{border:'1px solid black', margin:0,width:'100%'}}>1 : 1 문의</p>
      <div style={{width:'100%',height:'95%', border:'1px solid black', display:'flex'}}>
        <div style={{width:'30%', backgroundColor:'white',border:'1px solid black',}}>
          <div style={{width:'100%', height:'5%', backgroundColor:'red'}}>
            문의 리스트
          </div>
       
          <div style={{width:'100%', height:'95%', overflowY:'scroll'}}>
            {isInquiry.map((item, index) => (
              <div key={index} className={`${isDetail?.inquiryIdx===item.inquiryIdx ? styles.sel_inquiry:''} ${styles.inquiry_lst}`} 
              onClick={()=>detaildata(item.inquiryIdx)}>
                <div className={styles.inquiry_idx}>{index+1}</div>
                <div className={styles.inquiry_info}>
                  <span>{item.title}</span>
                  <span>{item.event}</span>
                </div>
                <div className={styles.inquiry_date}>
                  {item.createdAt.getFullYear() === new Date().getFullYear() ?
                  <span>{item.createdAt.getMonth() + 1 < 10 ? '0'+item.createdAt.getMonth() + 1:item.createdAt.getMonth() + 1}월 {item.createdAt.getDate() < 10 ? '0'+item.createdAt.getDate():item.createdAt.getDate()}일</span>
                   : <span>{item.createdAt.getFullYear()}년 {item.createdAt.getMonth() + 1 < 10 ? '0'+item.createdAt.getMonth() + 1:item.createdAt.getMonth() + 1}월 {item.createdAt.getDate() < 10 ? '0'+item.createdAt.getDate():item.createdAt.getDate()}일</span>
                  }
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
          <div style={{height:'10%',fontSize:40, borderBottom:'1px solid black'}}>
            문의사항
          </div>
          {isDetail ? 
          <div style={{height:'90%'}}>
            <div style={{height:'60%', padding:'2%',boxSizing:'border-box'}}>
              
              <div style={{display:'flex',justifyContent:'space-between',height:'10%'}}>
                <span onClick={()=>{setDetail(null)}}>닫기</span>
                <span>작성자 : {isDetail.nickname}</span>
              </div>

              <div style={{margin:'2% 10%',height:'90%'}}>
                <div style={{display:'flex', textAlign:'start',height:'15%'}}>
                  <div style={{width:'20%', border: '1px solid black',display:'flex', alignItems:'center',paddingLeft:10,boxSizing:'border-box'}}>
                    문의 제목
                  </div>
                  <div style={{width:'80%', border: '1px solid black',display:'flex', alignItems:'center',paddingLeft:10,boxSizing:'border-box'}}>
                    {isDetail.title} {isDetail.event}
                  </div>
                </div>

                <div style={{display:'flex', textAlign:'start',height:'15%'}}>
                  <div style={{width:'20%', border: '1px solid black',display:'flex', alignItems:'center',paddingLeft:10,boxSizing:'border-box'}}>
                    문의 등록일
                  </div>
                  <div style={{width:'80%', border: '1px solid black',display:'flex', alignItems:'center',paddingLeft:10,boxSizing:'border-box'}}>
                    <span>
                      {isDetail.createdAt.getFullYear()}년 {isDetail.createdAt.getMonth() + 1 < 10 ? '0'+isDetail.createdAt.getMonth() + 1:isDetail.createdAt.getMonth() + 1}월 {isDetail.createdAt.getDate() < 10 ? '0'+isDetail.createdAt.getDate():isDetail.createdAt.getDate()}일 ({days[isDetail.createdAt.getDay()]})
                    </span>
                    {isDetail.createdAt.getHours() <= 12 ? 
                  <span>　{isDetail.createdAt.getHours() < 10 ? '0'+(isDetail.createdAt.getHours()):isDetail.createdAt.getHours()} : {isDetail.createdAt.getMinutes() < 10 ? '0'+isDetail.createdAt.getMinutes():isDetail.createdAt.getMinutes()} am</span>
                  :
                  <span>　{isDetail.createdAt.getHours()%12 < 10 ? '0'+(isDetail.createdAt.getHours()%12):isDetail.createdAt.getHours()%12} : {isDetail.createdAt.getMinutes() < 10 ? '0'+isDetail.createdAt.getMinutes():isDetail.createdAt.getMinutes()} pm</span>}
                  </div>
                </div>

                <div style={{display:'flex', textAlign:'start',height:'70%'}}>
                  <div style={{width:'20%', border: '1px solid black',display:'flex',padding:'5px 10px',boxSizing:'border-box'}}>
                    문의 내용
                  </div>
                  <div style={{width:'80%', border: '1px solid black',display:'flex',padding:'5px 10px',boxSizing:'border-box'}}>
                    {isDetail.content}
                  </div>
                </div>
              </div>
          
            </div>
            {!isDetail.comment ?
            <div style={{height:'40%',padding:'2% 12%', boxSizing:'border-box'}}>
              <div style={{display:'flex',justifyContent:'space-between'}}>
                답변
                <div>
                <button onClick={commentregist}>등록</button>
                <button onClick={commentrefuse}>답변거절</button>
                </div>
              </div>
              <div style={{width:'100%', height:'85%', display:'flex',boxSizing:'border-box'}}>
                <textarea style={{resize:'none'}} name="" id="" cols={100} rows={100} value={isComment} onChange={(e)=>setComment(e.target.value)}></textarea>
              </div>
            </div>
            :            
            <div style={{height:'40%',padding:'2% 12%', boxSizing:'border-box'}}>
              <div style={{display:'flex',justifyContent:'space-between'}}>
                답변
                {isEdit ? 
                <div>
                  <button onClick={commentchange}>완료</button>
                  <button onClick={()=>{setEdit(false);setComment('');}}>취소</button>
                </div>
                :
                <div>
                  <button onClick={()=>{setComment(isDetail.comment?.content!);setEdit(true)}}>수정</button>
                  <button onClick={deletecomment}>삭제</button>
                </div>}
              </div>
              {isEdit ? 
              <div style={{width:'100%', height:'85%',display:'flex',boxSizing:'border-box'}}>
                <textarea style={{resize:'none'}} name="" id="" cols={100} rows={100} value={isComment} onChange={(e)=>{setComment(e.target.value)}} />         
              </div>:
              <div style={{width:'100%', height:'85%', border:'1px solid black', textAlign:'start',padding:'1%',boxSizing:'border-box'}}>
                {isDetail.comment.content}
              </div>}
            </div>}
          </div>
          :null}
        </div>
      </div>
    </div>

  );
};
export default Inquiry;
