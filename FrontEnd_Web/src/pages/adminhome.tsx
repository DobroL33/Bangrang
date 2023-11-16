import React, { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import styles from './adminhome.module.css'
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
    <div className={styles.homebox} style={{height:(window.innerHeight-80)}}>
      <h1>ADMIN 업무</h1>
      <div className={styles.funcbox}>
        <div className={styles.func} 
        onClick={()=>navigate('/admin/inquiry')}>
          <h1>1:1 문의</h1>
        </div>
        <div className={styles.func} 
        onClick={()=>navigate('/admin/registmanager')}>
          <h1>관리자 등록 요청 관리</h1>
        </div>
      </div>
    </div>
  );
};
export default AdminHome;
