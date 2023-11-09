import React, { useState, ChangeEvent, useEffect, FormEvent } from "react";
import DaumPostcode from "react-daum-postcode";
import PostCode from "../components/postcode";
import dayjs, { Dayjs } from "dayjs";
import { DemoContainer } from "@mui/x-date-pickers/internals/demo";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import { DateTimePicker } from "@mui/x-date-pickers/DateTimePicker";
import utc from "dayjs/plugin/utc";
import timezone from "dayjs/plugin/timezone";
import axiosInstance from "../axiosinstance";
import { FormEncType, useNavigate } from "react-router-dom";
interface Event {
  title: string;
  subTitle: string;
  content: string;
  startDate: string;
  endDate: string;
  address: string;
  eventUrl: string;
}

const EventRegist: React.FC = () => {
  const navigate = useNavigate();
  const [event, setEvent] = useState<Event>({
    title: "",
    subTitle: "",
    content: "",
    startDate: "",
    endDate: "",
    address: "",
    eventUrl: "",
  });
  const [image, setImage] = useState<FileList | null>(null);
  const [subImage, setSubImage] = useState<FileList | null>(null);

  const { title, subTitle, content, startDate, endDate, address, eventUrl } =
    event;

  const handleTitleChange = (e: ChangeEvent<HTMLInputElement>) => {
    setEvent((prevEvent) => ({
      ...prevEvent,
      title: e.target.value,
    }));
  };

  const handleSubTitleChange = (e: ChangeEvent<HTMLInputElement>) => {
    setEvent((prevEvent) => ({
      ...prevEvent,
      subTitle: e.target.value,
    }));
  };

  const handleContentChange = (e: ChangeEvent<HTMLTextAreaElement>) => {
    setEvent((prevEvent) => ({
      ...prevEvent,
      content: e.target.value,
    }));
    console.log(event);
  };
  const handleAddressChange = (addressValue: string) => {
    setEvent((prevEvent) => ({
      ...prevEvent,
      address: addressValue,
    }));
    console.log(event);
  };

  const datePickerFormat = "YYYY-MM-DDTHH:mm:ss";
  // const datePickerUtils = {
  //   format: datePickerFormat,
  //   parse: (value) => dayjs(value, datePickerFormat, true).toDate(),
  //   // You can add other utils as needed, such as `isValid`, etc.
  // };

  const startDateChange = (date: string | null) => {
    if (date) {
      const formattedDate = dayjs(date).format(datePickerFormat);
      setEvent((prev) => ({
        ...prev,
        startDate: formattedDate,
      }));
    }
    console.log(event);
  };

  const endDateChange = (date: string | null) => {
    if (date) {
      const formattedDate = dayjs(date).format(datePickerFormat);
      setEvent((prev) => ({
        ...prev,
        endDate: formattedDate,
      }));
    }
  };

  const handleEventUrlChange = (e: ChangeEvent<HTMLInputElement>) => {
    setEvent((prevEvent) => ({
      ...prevEvent,
      eventUrl: e.target.value,
    }));
  };

  const registEvent = (e: FormEvent) => {
    e.preventDefault();
    const formData = new FormData();
    if (image != null) {
      formData.append("image", image[0]);
    }
    if (subImage != null) {
      formData.append("subImage", subImage[0]);
    } else {
      formData.append("subImage", "");
    }

    formData.append(
      "data",
      new Blob([JSON.stringify(event)], { type: "application/json" })
    );

    axiosInstance({
      method: "post",
      url: `${process.env.REACT_APP_API}/web/event`,
      data: formData,
      headers: {
        "Content-Type": "multipart/form-data",
      },
    })
      .then((res) => {
        console.log(res);
        alert("이벤트 등록 완료");
        navigate("/manage/eventlist");
      })
      .catch((err) => console.log(err));
  };

  return (
    <div>
      <form onSubmit={registEvent}>
        <h1>이벤트 등록하기</h1>
        <span>이벤트 제목</span>
        <input value={title} onChange={handleTitleChange} />
        <br />
        <span>이벤트 부제목</span>
        <input value={subTitle} onChange={handleSubTitleChange} />
        <br />
        <span>내용 작성</span>
        <textarea value={content} onChange={handleContentChange} />

        <br />
        <span>주소 작성</span>
        <PostCode setAddress={(value: string) => handleAddressChange(value)} />
        <br />

        <LocalizationProvider dateAdapter={AdapterDayjs}>
          <DemoContainer components={["DateTimePicker", "DateTimePicker"]}>
            <DateTimePicker
              label="Controlled picker"
              value={startDate}
              onChange={startDateChange}
              format="YYYY-MM-DDTHH:mm:ss"
            />
            <DateTimePicker
              label="Controlled picker"
              value={endDate}
              onChange={endDateChange}
              format="YYYY-MM-DDTHH:mm:ss"
            />
          </DemoContainer>
        </LocalizationProvider>
        <br />
        <span>이벤트 url 등록하기</span>
        <input type="text" value={eventUrl} onChange={handleEventUrlChange} />
        <br />
        <br />
        {/* 이미지 input */}
        <span>이미지 등록</span>
        <input
          type="file"
          placeholder="인증파일"
          onChange={({ target: { files } }) => {
            if (files && files[0]) {
              setImage(files);
            } else {
              setImage(null);
            }
          }}
        />
        <p style={{ fontSize: "8px", fontWeight: "bold", color: "red" }}>
          {image === null ? (
            <span>파일을 첨부해주세요</span>
          ) : (
            <span style={{ color: "green" }}>첨부완료</span>
          )}
        </p>
        <br />
        {/* 서브이미지 input */}
        <span>서브이미지 등록</span>
        <input
          type="file"
          placeholder="인증파일"
          onChange={({ target: { files } }) => {
            if (files && files[0]) {
              setSubImage(files);
            } else {
              setSubImage(null);
            }
          }}
        />
        {/* <p style={{ fontSize: "8px", fontWeight: "bold", color: "red" }}>
          {subImage === null ? (
            <span>파일을 첨부해주세요</span>
          ) : (
            <span style={{ color: "green" }}>첨부완료</span>
          )}
        </p> */}
        {title &&
        subTitle &&
        startDate &&
        endDate &&
        content &&
        address &&
        image ? (
          <button type="submit">이벤트 등록하기</button>
        ) : (
          <button disabled>이벤트 등록하기</button>
        )}
      </form>

      {/* <button type="button" onClick={() => console.log(event)}>
        이벤트 찍기
      </button> */}
    </div>
  );
};

export default EventRegist;
