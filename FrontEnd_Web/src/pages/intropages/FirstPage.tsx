import React from "react";
import styles from "./FirstPage.module.css";
import { Link, useLocation, useNavigate } from "react-router-dom";

const FirstPage: React.FC = () => {
  return (
    <div className={styles.Page}>
      <div className={styles.btnContainer}>
        <Link className={` ${styles.down}`} to="/login">
          관리자 페이지
        </Link>
        <Link className={`${styles.down}`} to="https://play.google.com/store">
          다운로드
        </Link>
      </div>
    </div>
  );
};

export default FirstPage;
