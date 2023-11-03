import React, { useEffect } from "react";
import { useNavigate } from "react-router-dom";

const AdminHome: React.FC = () => {
    const navigate = useNavigate()
    useEffect(()=>{
      const AccessToken = localStorage.getItem('AccessToken')
      if (AccessToken) {
        if (localStorage.getItem("UserName")==='admin@bangrang') {
          navigate('/admin')
        } else {
          navigate('/manage')
        }
      } else {
        navigate('')
      }
    },[navigate])
  return (
    <div>
        <h1>ADMIN HOME</h1>
      <div style={{width:'100vw', display:'flex', backgroundColor:'#E2F5FF',padding:'5% 0',justifyContent:'space-evenly'}}>
        <div style={{backgroundColor:'red',width:'30%',height:'50vh'}}>
        </div>
        <div style={{backgroundColor:'red',width:'30%',height:'50vh'}}>
        </div>

      </div>
    </div>
  );
};
export default AdminHome;
