import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axiosInstance from "../axiosinstance";
import styles from "./registmanager.module.css";

interface manager {
  idx:number;
  id:string;
  organizationName:string;
  authFile:string;
  status:string;
}

const RegistManager: React.FC = () => {
  const [isManagers, setManagers] = useState<manager[]>([{idx:1,id:'asdf1',organizationName:'asdfds',authFile:'asdf',status:'WATING'},{idx:2,id:'asdf',organizationName:'asdfds',authFile:'asdf',status:'WATING'},{idx:1,id:'asdf',organizationName:'asdfds',authFile:'asdf',status:'WATING'},{idx:1,id:'asdf',organizationName:'asdfds',authFile:'asdf',status:'WATING'},{idx:1,id:'asdf',organizationName:'asdfds',authFile:'asdf',status:'WATING'},{idx:1,id:'asdf',organizationName:'asdfds',authFile:'asdf',status:'WATING'},{idx:1,id:'asdf',organizationName:'asdfds',authFile:'asdf',status:'WATING'},{idx:1,id:'asdf',organizationName:'asdfds',authFile:'asdf',status:'WATING'},{idx:1,id:'asdf',organizationName:'asdfds',authFile:'asdf',status:'WATING'},{idx:1,id:'asdf',organizationName:'asdfds',authFile:'asdf',status:'WATING'},{idx:1,id:'asdf',organizationName:'asdfds',authFile:'asdf',status:'WATING'},{idx:1,id:'asdf',organizationName:'asdfds',authFile:'asdf',status:'WATING'},{idx:1,id:'asdf',organizationName:'asdfds',authFile:'asdf',status:'WATING'},{idx:1,id:'asdf',organizationName:'asdfds',authFile:'asdf',status:'WATING'},{idx:1,id:'asdf',organizationName:'asdfds',authFile:'asdf',status:'WATING'},{idx:1,id:'asdf',organizationName:'asdfds',authFile:'asdf',status:'WATING'},{idx:1,id:'asdf',organizationName:'asdfds',authFile:'asdf',status:'WATING'},{idx:1,id:'asdf',organizationName:'asdfds',authFile:'asdf',status:'WATING'},{idx:1,id:'asdf',organizationName:'asdfds',authFile:'asdf',status:'WATING'},{idx:1,id:'asdf',organizationName:'asdfds',authFile:'asdf',status:'WATING'},{idx:1,id:'asdf',organizationName:'asdfds',authFile:'asdf',status:'WATING'},{idx:1,id:'asdf',organizationName:'asdfds',authFile:'asdf',status:'WATING'},{idx:1,id:'asdf',organizationName:'asdfds',authFile:'asdf',status:'WATING'},{idx:1,id:'asdf',organizationName:'asdfds',authFile:'asdf',status:'WATING'},{idx:1,id:'asdf',organizationName:'asdfds',authFile:'asdf',status:'WATING'},{idx:1,id:'asdf',organizationName:'asdfds',authFile:'asdf',status:'WATING'},{idx:1,id:'asdf',organizationName:'asdfds',authFile:'asdf',status:'WATING'},{idx:1,id:'asdf',organizationName:'asdfds',authFile:'asdf',status:'WATING'},{idx:1,id:'asdf',organizationName:'asdfds',authFile:'asdf',status:'WATING'},{idx:1,id:'asdf',organizationName:'asdfds',authFile:'asdf',status:'WATING'},{idx:1,id:'asdf',organizationName:'asdfds',authFile:'asdf',status:'WATING'},{idx:1,id:'asdf',organizationName:'asdfds',authFile:'asdf',status:'WATING'},{idx:1,id:'asdf',organizationName:'asdfds',authFile:'asdf',status:'WATING'},{idx:1,id:'asdf',organizationName:'asdfds',authFile:'asdf',status:'WATING'},{idx:1,id:'asdf',organizationName:'asdfds',authFile:'asdf',status:'WATING'},{idx:1,id:'asdf',organizationName:'asdfds',authFile:'asdf',status:'WATING'},{idx:1,id:'asdf',organizationName:'asdfds',authFile:'asdf',status:'WATING'},{idx:1,id:'asdf',organizationName:'asdfds',authFile:'asdf',status:'WATING'},{idx:1,id:'asdf',organizationName:'asdfds',authFile:'asdf',status:'WATING'},{idx:1,id:'asdf',organizationName:'asdfds',authFile:'asdf',status:'WATING'}])
  const [isDetail, setDetail] = useState<number|null>(null)
  const [isFilter, setFilter] = useState('ALL');
  const changeStatus = ['WATING', 'DECLINED', 'ACCEPTED']
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

  const updateStatus = (statusNum:number) => {
    // 0 WATING  1 DECLINED   2 ACCEPTED
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
    <div
      style={{
        width: "100%",
        height: window.innerHeight - 80,
        backgroundColor: "#E2F5FF",
        padding: "3% 10%",
        boxSizing: "border-box",}}> 
      <h1 style={{ border: "1px solid black", margin: 0, width: "100%" }}>
        관리자 승인 업무
      </h1>
      <div style={{
          width: "100%",
          height:"95%",
          backgroundColor: "white",
          border: "1px solid black",}}>

        {isManagers.length > 0 ?
        <div style={{width:'100%', height:'100%',display:'flex'}}>
          <div style={{height:'100%', width:'70%', backgroundColor:'olive'}}>
            <div style={{height:'5%',width:'96%',alignItems:'center',display:'flex', justifyContent:'space-between', margin:'0 2%'}}>
              <div>
                <span>필터 : </span>
                <label>
                  <input type="radio" checked={isFilter === 'ALL'} onClick={()=>setFilter('ALL')}/>
                  전체
                </label>
                <label>
                  <input type="radio" checked={isFilter === 'WATING'} onClick={()=>setFilter('WATING')} />
                  대기중
                </label>
                <label>
                  <input type="radio" checked={isFilter === 'ACCEPTED'} onClick={()=>setFilter('ACCEPTED')}/>
                  승인
                </label>
                <label>
                  <input type="radio" checked={isFilter === 'DECLINED'} onClick={()=>setFilter('DECLINED')}/>
                  거절
                </label>
              </div>
              <span onClick={()=>{loaddata();setDetail(null);}}>
                🔁 새로고침
              </span>
            </div>

            <div style={{height:'95%',width:'100%', backgroundColor:'bisque', overflowY:'auto', display:'flex',flexWrap: 'wrap'}}>
              {isManagers.map((manager, idx) => (
                (isFilter === 'ALL' || isFilter === manager.status) &&
                (
                <div key={idx} style={{backgroundColor:'white', display:'flex', height:'10%', width:'27%', justifyContent:'space-between',alignItems:'center', margin:'2% 3%',
              borderRadius:'1rem'}}
                onClick={()=>setDetail(idx)}>
                  <div style={{width:'15%',}}>{manager.idx}</div>
                  <div style={{display:'flex', flexDirection:'column', width:'55%', textAlign:'start'}}>
                    <span style={{fontSize:'12px'}}>{manager.id}</span>
                    <span style={{}}>{manager.organizationName}</span>
                  </div>
                  <div style={{backgroundColor:'gray', width:'25%',height:'90%',marginRight:'2%',borderRadius:'30%', justifyContent:'center',alignItems:'center',
                display:'flex', fontSize:'10px'}}>
                    {manager.status}
                  </div>
                </div>
              )))}
          </div>
          </div>
          <div style={{height:'100%', width:'30%',borderLeft:'1px solid black'}}>
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
              {isManagers[isDetail].status === 'WATING' ?
                <div>
                  <p>이 계정은 승인 대기중 입니다.</p>
                  <button onClick={()=>updateStatus(2)}>승인</button>
                  <button onClick={()=>updateStatus(1)}>거절</button>
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
                  <button onClick={()=>updateStatus(1)}>계정 정지</button>
                </div>
              }
            </div> 
            : <h1>유저 상세정보 조회</h1>
            }
          </div>
        
        </div>
        : 
        <div style={{width:'100%', height:'95%',display:'flex',alignItems:'center',justifyContent:'center'}}>
          <h1>유저가 존재하지 않습니다 ㅠ.ㅠ</h1>
        </div>}
      </div>
    </div>
  );
};
export default RegistManager;
