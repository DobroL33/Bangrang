import React, { useEffect, useState } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";

import styles from "./navbar.module.css";
import axiosInstance from "../axiosinstance";

const Navbar: React.FC = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const [IsMobile, setIsMobile] = useState(window.innerWidth <= 800);
  const [isDropDown, setDropDown] = useState(false);

  useEffect(() => {
    const handleResize = () => {
      setIsMobile(window.innerWidth <= 800);
      if (window.innerWidth > 800) {
        setDropDown(false);
      }
    };

    window.addEventListener("resize", handleResize);

    return () => {
      window.removeEventListener("resize", handleResize);
    };
  }, []);

  const logout = () => {
    axiosInstance({
      method: "get",
      url: `${process.env.REACT_APP_API}/web/logout`,
    })
      .then((res) => {
        console.log(res);
        navigate("/");
        localStorage.clear();
      })
      .catch((err) => {
        console.log(err);
        navigate("/");
        localStorage.clear();
      });
  };
  return (
    <>
      {location.pathname === "/" ? (
        <header className="navbar">
          <Link to="/">
            <img className={styles.logo} src="assets/logo.png" alt="" />
          </Link>
          {!IsMobile ? (
            <div className={styles.navbtn_lst}>
              <Link className={styles.navbtn} to="/">
                서비스 소개
              </Link>
              <Link className={styles.navbtn} to="/login">
                관리자 페이지
              </Link>
              <button className={`${styles.navbtn} ${styles.appdown}`} onClick={()=>{window.open("https://drive.google.com/file/d/19-hTUug92FdA_HqgOl28bpzcjTZeUVlF/view", '_blank')}}>다운로드</button>
              {/* <Link
                className={`${styles.navbtn} ${styles.appdown}`}
                to="https://play.google.com/store">
                다운로드
              </Link> */}
            </div>
          ) : (
            <div className={styles.dropdown}>
              <img
                src="/assets/menu.png"
                onClick={() => setDropDown(!isDropDown)}
              />
              {isDropDown && (
                <div className={styles.dropdown_content}>
                  <Link to="/">서비스 소개</Link>
                  <Link to="/login">관리자 페이지</Link>
                  {/* <Link to="https://play.google.com/store">다운로드</Link> */}
                  <a href="https://drive.google.com/file/d/19-hTUug92FdA_HqgOl28bpzcjTZeUVlF/view">다운로드</a>
                </div>
              )}
            </div>
          )}
        </header>
      ) : location.pathname === "/manage" ||
        location.pathname === "/manage/inquiry" ||
        location.pathname === "/manage/event" ? (
        <header className="navbar" style={{backgroundColor:'#E2F5FF'}}>
          <Link to="/manage">
            <img className={styles.logo} src="assets/logo.png" alt="" />
          </Link>
          <div className={styles.navbtn}>
            {!IsMobile && (
              <p>안녕하세요.{localStorage.getItem("UserName")} 님</p>
            )}
            <button onClick={logout}>로그아웃</button>
          </div>
        </header>
      ) : location.pathname === "/admin" ||
        location.pathname === "/admin/inquiry" ||
        location.pathname === "/admin/registmanager" ? (
        <header className="navbar" style={{backgroundColor:'#E2F5FF'}}>
          <Link to="/admin">
            <img className={styles.logo} src="assets/logo.png" alt="" />
          </Link>
          <div className={styles.navbtn}>
            {!IsMobile && <p>안녕하세요. 개발자 님</p>}
            <button onClick={logout}>로그아웃</button>
          </div>
        </header>
      ) : null}
    </>
  );
};
export default Navbar;
