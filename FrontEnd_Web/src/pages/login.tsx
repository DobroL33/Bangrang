import axios from "axios";
import React, { useState, useEffect } from "react";
import { useNavigate, Link } from "react-router-dom";
const Login: React.FC = () => {
  const [isId, setId] = useState("");
  const [isPw, setPw] = useState("");

  const navigate = useNavigate();
  useEffect(() => {
    const AccessToken = localStorage.getItem("AccessToken");
    if (AccessToken) {
      if (localStorage.getItem("UserName") === "admin@bangrang") {
        navigate("/admin");
      } else {
        navigate("/manage");
      }
    }
  }, [navigate]);

  const login = (e: any) => {
    e.preventDefault();
    if (!isId) {
      if (!isPw) {
        return alert("아이디와 비밀번호를 입력해주세요.");
      }
      return alert("아이디를 입력해주세요.");
    }
    if (!isPw) return alert("패스워드를 입력해주세요.");
    axios({
      method: "post",
      url: `${process.env.REACT_APP_API}/web/login`,
      data: {
        id: isId,
        password: isPw,
      },
    }).then((res) => {
        localStorage.setItem("AccessToken", res.headers["authorization"]);
        localStorage.setItem("RefreshToken",res.headers["authorization-refresh"]);

        const UserName = res.data.OrganizationName;
        localStorage.setItem("UserName", UserName);
        if (UserName === "admin@bangrang") {
          navigate("/admin");
        } else {
          navigate("/manage");
        }
      })
      .catch((err) => {
        alert(
          "해당 계정이 존재하지않습니다.\n아이디 & 비밀번호를 다시 확인해주세요"
        );
      });
  };
  return (
    <div>
      <img src="assets/logo.png" alt="" />
      <h1>관리자 로그인</h1>
      <form onSubmit={login}>
        <label>아이디</label>
        <input
          type="text"
          value={isId}
          onChange={(e) => {
            setId(e.target.value);
          }}
          autoComplete="username"
        />
        <br />
        <label>비밀번호</label>
        <input
          type="password"
          value={isPw}
          onChange={(e) => {
            setPw(e.target.value);
          }}
          autoComplete="current-password"
        />
        <br />
        <button type="submit">로그인</button>
      </form>
      <Link to={"/signup"}>가입신청</Link>
    </div>
  );
};
export default Login;
