import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axiosInstance from "../axiosinstance";
import styles from "./registmanager.module.css";
import axios from "axios";

interface manager {
  idx:number;
  id:string;
  organizationName:string;
  authFile:string;
  status:string;
}

const RegistManager: React.FC = () => {
  const [isManagers, setManagers] = useState<manager[]>([])  
  const [isDetail, setDetail] = useState<number|null>(null)
  const [isFilter, setFilter] = useState('ALL');
  const changeStatus = ['WAITING',  'ACCEPTED', 'DECLINED']
  const navigate = useNavigate();

  useEffect(() => {
    const AccessToken = localStorage.getItem("AccessToken");
    if (AccessToken) {
      if (localStorage.getItem("UserName")!=='admin@bangrang') {
        navigate('/manage')
      } else {
        loaddata();
      }
    } else {
      navigate("/login");
    }
  }, [navigate]);

  const loaddata = () => {
    axiosInstance({
      method:'get',
      url:`${process.env.REACT_APP_API}/admin`,
    }).then(res=>{
      setManagers(res.data)
    }).catch(err=>{
      console.log(err)
    })
  };

  const filedownload = (url:string) => {
    const fileName = url.split('/').pop(); // URL을 '/'로 분할하고 마지막 요소를 추출합니다.
    axios({
      method:'get',
      url:url,
      responseType:'blob'
    }).then(res=>{
      const url = window.URL.createObjectURL(new Blob([res.data]));
      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', fileName || 'ss'); // 파일명을 지정할 수 있음
      document.body.appendChild(link);
      link.click();
    }).catch(err=>{
      console.log(err)
    })
  }
  const updateStatus = (statusNum:number) => {
    // 0 WAITING  1 ACCEPTED   2 DECLINED
    axiosInstance({
      method:'put',
      url:`${process.env.REACT_APP_API}/admin`,
      data:{
        userIdx: isManagers[isDetail!].idx,
        status: statusNum
      }
    }).then(res=>{
      const updatedManagers = [...isManagers]; // isManagers 배열 복사
      updatedManagers[isDetail!] = { ...updatedManagers[isDetail!], status: changeStatus[statusNum] }; 
      setManagers(updatedManagers);
    }).catch(err=>{
      console.log(err)
    }) 
  }

  const deleteAccount = () => {
    // 완전 삭제
    axiosInstance({
      method:'delete',
      url:`${process.env.REACT_APP_API}/admin`,
      data:{
        userIdx: isManagers[isDetail!].idx,
      }
    }).then(res=>{
      const updatedManagers = isManagers.filter((_, index) => index !== isDetail); 
      setManagers(updatedManagers); 
      setDetail(null)
    }).catch(err=>{
      console.log(err)
    }) 
  }

  return (
    <div className={styles.homebox} style={{height: window.innerHeight - 80}}> 
      <h1 style={{  margin: 0 }}>
        관리자 승인 업무
      </h1>
      <div className={styles.funcbox}>
        {isManagers.length > 0 ?
        <div style={{width:'100%', height:'100%',display:'flex'}}>
          <div style={{height:'100%', width:'73%'}}>
            <div className={styles.filterbar}>
              <div>
                <span>필터 : </span>
                <label className={styles.cursor}>
                  <input type="radio" checked={isFilter === 'ALL'} onClick={()=>setFilter('ALL')}/>
                  전체
                </label>
                <label className={styles.cursor}>
                  <input type="radio" checked={isFilter === 'WAITING'} onClick={()=>setFilter('WAITING')} />
                  대기중
                </label>
                <label className={styles.cursor}>
                  <input type="radio" checked={isFilter === 'ACCEPTED'} onClick={()=>setFilter('ACCEPTED')}/>
                  승인
                </label>
                <label className={styles.cursor}>
                  <input type="radio" checked={isFilter === 'DECLINED'} onClick={()=>setFilter('DECLINED')}/>
                  거절
                </label>
              </div>
              <span onClick={()=>{loaddata();setDetail(null);}}
              className={styles.cursor}>
                🔁 새로고침
              </span>
            </div>

            <div className={styles.databox}>
              {isManagers.map((manager, idx) => (
                (isFilter === 'ALL' || isFilter === manager.status) &&
                (
                <div key={idx} className={styles.data} onClick={()=>setDetail(idx)}>
                  <div className={styles.data_pk} >{manager.idx}</div>
                  <div className={styles.data_info}>
                    <span style={{fontSize:'12px'}}>{manager.id}</span>
                    <span>{manager.organizationName}</span>
                  </div>
                  <div className={styles.data_status} 
                  style={manager.status === 'ACCEPTED' ? {backgroundColor: "rgb(7, 199, 7)"}
                  : manager.status === 'DECLINED' ?{backgroundColor:'red'}:{}} >
                    {manager.status}
                  </div>
                </div>
              )))}
            </div>
          </div>
          <div className={styles.detailframe}>
            {isDetail!==null ? 
            <div>
              <h1>유저 상세정보 조회</h1>
              유저 pk : {isManagers[isDetail].idx}
              <br />
              유저 아이디 : {isManagers[isDetail].id}
              <br />
              유저 조직 : {isManagers[isDetail].organizationName}
              <br />
              유저 상태 : {isManagers[isDetail].status}
              <br />
              유저 인증파일 : {isManagers[isDetail].authFile}
              <br />  
              <button onClick={()=>filedownload(isManagers[isDetail].authFile)}>다운로드</button>
              <br />
              {isManagers[isDetail].status === 'WAITING' ?
                <div>
                  <p>이 계정은 승인 대기중 입니다.</p>
                  <button onClick={()=>updateStatus(1)}>승인</button>
                  <button onClick={()=>updateStatus(2)}>거절</button>
                </div>
                :
                isManagers[isDetail].status === 'DECLINED' ?
                <div>
                  <p>이 계정은 거절되었습니다.</p>
                  <button onClick={()=>updateStatus(0)}>계정 복구</button>
                  <button onClick={()=>deleteAccount()}>계정 데이터 삭제</button>
                </div>
                :
                <div>
                  <p>이 계정은 승인되었습니다.</p>
                  <button onClick={()=>updateStatus(2)}>계정 정지</button>
                </div>
              }
            </div> 
            : 
            <div>
              <h1>유저 상세정보 조회</h1>
              <p>상세정보 조회를 하시려면<br />
              해당 유저를 클릭해주세요!</p>
            </div>
            
            }
          </div>
        </div>
        : <h1>유저가 존재하지 않습니다 ㅠㅠ <br /><br />일해라 일!!</h1>}
      </div>
    </div>
  );
};
export default RegistManager;
