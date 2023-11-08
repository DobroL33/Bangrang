import React, { useEffect } from "react";
import { useNavigate } from "react-router-dom";

const AdminHome: React.FC = () => {
    const navigate = useNavigate()
    useEffect(()=>{
      const AccessToken = localStorage.getItem('AccessToken')
      if (AccessToken) {
        if (localStorage.getItem("UserName")!=='admin@bangrang') {
          navigate('/manage')
        }
      } else {
        navigate('/login')
      }
    },[navigate])
  return (
    <div style={{width:'100%',height:(window.innerHeight-80), display:'flex', backgroundColor:'#E2F5FF',padding:'5% 0', justifyContent:'space-evenly', boxSizing:"border-box"}}>
      <div style={{width:'40%', backgroundColor:'red'}} onClick={()=>navigate('/admin/inquiry')}>
        <h1>1:1 문의</h1>
      </div>
      <div style={{width:'40%', backgroundColor:'red'}} onClick={()=>navigate('/admin/registmanager')}>
        <h1>관리자 등록 요청 관리</h1>
      </div>
    </div>
  );
};
export default AdminHome;
