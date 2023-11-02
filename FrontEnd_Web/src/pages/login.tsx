import React, { useEffect } from "react";
import { useNavigate, Link } from 'react-router-dom';
const Login: React.FC = () => {
    const navigate = useNavigate()
    useEffect(()=>{
      const AccessToken = localStorage.getItem('AccessToken')
      if (AccessToken) {
        navigate('/manage')
      }
    },[navigate])
  return (
    <div>
        <img src="assets/logo.png" alt="" />
        <h1>관리자 로그인</h1>
        <form onSubmit={()=>navigate('/manage')}>
            <label>아이디</label>
            <input type="email" name="" id="" />
            <br />
            <label>비밀번호</label>
            <input type="password" name="" id="" />
            <br />
            <button type="submit">로그인</button>
        </form>
        <Link to={'/signup'}>가입신청</Link>
    </div>
  );
};
export default Login;
