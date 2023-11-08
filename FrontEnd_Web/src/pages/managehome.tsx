import React, { useEffect } from "react";
import { useNavigate } from "react-router-dom";

const ManageHome: React.FC = () => {
  const navigate = useNavigate();
  useEffect(() => {
    const AccessToken = localStorage.getItem("AccessToken");
    if (AccessToken) {
      if (localStorage.getItem("UserName") === "admin@bangrang") {
        navigate("/admin");
      }
    } else {
      navigate("");
    }
  }, [navigate]);

  return (
    <div
      style={{
        width: "100%",
        height: window.innerHeight - 80,
        display: "flex",
        backgroundColor: "#E2F5FF",
        padding: "5% 0",
        justifyContent: "space-evenly",
        boxSizing: "border-box",
      }}
    >
      <div
        style={{ width: "40%", backgroundColor: "red" }}
        onClick={() => navigate("/manage/inquiry")}
      >
        <h1>1:1 문의</h1>
      </div>
      <div
        style={{ width: "40%", backgroundColor: "red" }}
        onClick={() => navigate("/manage/eventList")}
      >
        <h1>이벤트 등록 및 관리</h1>
      </div>
    </div>
  );
};
export default ManageHome;
