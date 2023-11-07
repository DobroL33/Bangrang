import React, { useState } from "react";
import Navbar from "../components/navbar";

const EventRegist = () => {
  const [event, setEvent] = useState({
    title: "",
    subTitle: "",
    content: "",
    startDate: "",
    endDate: "",
    address: "",
    eventUrl: "",
  });

  const { title, subTitle, content, startDate, endDate, address, eventUrl } =
    event;

  const handleTitleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setEvent((prevEvent) => ({
      ...prevEvent,
      title: e.target.value,
    }));
  };

  return (
    <div>
      <h1>이벤트 등록하기</h1>
      <h2>이벤트 제목</h2>
      <input value={title} onChange={handleTitleChange} />
      <p>ds</p>
    </div>
  );
};

export default EventRegist;
