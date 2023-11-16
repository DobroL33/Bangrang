import styled from "@emotion/styled";
import axios from "axios";
import React, { useState, useEffect } from "react";
import { useNavigate, Link } from "react-router-dom";
import { AiOutlineUser } from "react-icons/ai";
import { BiLockAlt } from "react-icons/bi";

const InputContainer = styled.div`
  display: flex;
  align-items: center;
  width: 30%;
  height: 2.625rem;
  color: #c6c5c5;
  border: 1px solid #3d5cff;
  border-radius: 5px;
`;

const StyledInput = styled.input`
  flex-shrink: 0;
  padding-left: 2rem;
  border: none;
  outline: none;
  color: #525252;
  font-weight: bold;
`;

const IconID = styled(AiOutlineUser)`
  font-size: 1.5rem;
  flex-shrink: 0;
  color: #7d7b7b;
  margin-left: 10px;
  height: 100%;
  weight: 7rem;
`;

const IconPW = styled(BiLockAlt)`
  font-size: 1.5rem;
  flex-shrink: 0;
  color: #7d7b7b;
  margin-left: 10px;
  height: 100%;
  weight: 7rem;
`;

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
    })
      .then((res) => {
        localStorage.setItem("AccessToken", res.headers["authorization"]);
        localStorage.setItem(
          "RefreshToken",
          res.headers["authorization-refresh"]
        );

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
    <div
      style={{
        width: "100%",
        height:'100%',
        padding: "5% 10%",
        boxSizing: "border-box",
      }}
    >
      <img src="assets/logo.png" alt="" style={{width:'300px'}}/>
      <h1 style={{ color: "gray" }}>관리자 로그인</h1>

      <form onSubmit={login}>
        <div
          style={{
            display: "flex",
            justifyContent: "center",
            alignItems: "center",
          }}
        >
          <InputContainer>
            <IconID />
            <StyledInput
              type="text"
              // className={className}
              placeholder="아이디"
              value={isId}
              onChange={(e) => {
                setId(e.target.value);
              }}
              autoComplete="username"
            />
          </InputContainer>
        </div>
        <div
          style={{
            display: "flex",
            justifyContent: "center",
            alignItems: "center",
            marginTop: "10px",
          }}
        >
          <InputContainer>
            <IconPW />
            <StyledInput
              type="password"
              // className={className}
              placeholder="비밀번호"
              value={isPw}
              onChange={(e) => {
                setPw(e.target.value);
              }}
              autoComplete="current-password"
            />
          </InputContainer>
        </div>
        <button
          type="submit"
          style={{
            marginTop: "30px",
            border: "none",
            background: "#0057FF",
            width: "30%",
            height: "6vh",
            borderRadius: "6px",
            color: "white",
            fontWeight: "bold",
            fontSize: "18px",
            cursor:'pointer'
          }}
        >
          로그인
        </button>
      </form>

      <br />
      <Link to={"/signup"} style={{ textDecoration: "none", color: "#0057FF" }}>
        가입신청
      </Link>
    </div>
  );
};
export default Login;
