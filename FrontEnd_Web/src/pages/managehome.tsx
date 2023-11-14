import React, { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import styles from './managehome.module.css';

const ManageHome: React.FC = () => {
  const navigate = useNavigate();
  useEffect(() => {
    const AccessToken = localStorage.getItem("AccessToken");
    if (AccessToken) {
      if (localStorage.getItem("UserName") === "admin@bangrang") {
        navigate("/admin");
      }
    } else {
      navigate("/login");
    }
  }, [navigate]);

  return (
    <div className={styles.homebox} style={{height:(window.innerHeight-80)}}>
      <h1>MANAGE 업무</h1>
      <div className={styles.funcbox}>
        <div className={styles.func} 
        onClick={()=>navigate("/manage/inquiry")}>
          <h1>1:1 문의</h1>
        </div>
        <div className={styles.func} 
        onClick={()=>navigate("/manage/event")}>
          <h1>이벤트 등록 및 관리</h1>
        </div>
      </div>
    </div>
  );
};
export default ManageHome;
