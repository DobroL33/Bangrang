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
import styles from "./eventregist.module.css";

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
  };
  const handleAddressChange = (addressValue: string) => {
    setEvent((prevEvent) => ({
      ...prevEvent,
      address: addressValue,
    }));
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
    }

    formData.append("data", JSON.stringify(event));

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
    <div
      style={{
        padding: "20px",
        maxWidth: "90%",
        margin: "auto",
        border: "2px solid gray",
      }}
    >
      <form onSubmit={registEvent} style={{ maxWidth: "80%", margin: "auto" }}>
        <h1 style={{ textAlign: "center" }}>이벤트 등록하기</h1>
        <div
          style={{
            marginBottom: "15px",
            display: "flex",
            justifyContent: "center",
          }}
        >
          <label style={{ width: "100px" }}>Title :</label>
          <input
            className={styles.select_input}
            style={{
              width: "100%",
              boxSizing: "border-box",
              border: "1px solid black",
              borderRadius: "5px",
            }}
            type="text"
            value={title}
            onChange={handleTitleChange}
          />
        </div>
        <div
          style={{
            marginBottom: "15px",
            display: "flex",
            justifyContent: "center",
          }}
        >
          <label style={{ width: "100px" }}>SubTitle :</label>
          <input
            className={styles.select_input}
            style={{
              width: "100%",
              boxSizing: "border-box",
              border: "1px solid black",
              borderRadius: "5px",
            }}
            type="text"
            value={subTitle}
            onChange={handleSubTitleChange}
          />
        </div>

        <div
          style={{
            marginBottom: "15px",
            display: "flex",
            flexDirection: "column",
            alignItems: "center", // 수직 가운데 정렬
          }}
        >
          <label style={{ marginBottom: "5px" }}>내용 작성</label>
          <textarea
            className={styles.select_input}
            style={{ width: "100%", height: "70%" }}
            value={content}
            onChange={handleContentChange}
            rows={5}
          />
        </div>
        <div style={{ marginBottom: "15px" }}>
          <label>주소 작성</label>
          <PostCode
            address={address}
            setAddress={(value: string) => handleAddressChange(value)}
          />
        </div>

        <div
          style={{
            marginBottom: "15px",
          }}
        >
          <LocalizationProvider dateAdapter={AdapterDayjs}>
            <DemoContainer
              components={["DateTimePicker", "DateTimePicker"]}
              sx={{
                display: "flex",
                justifyContent: "center",
                alignItems: "center",
              }}
            >
              <DateTimePicker
                label="시작 날짜"
                value={startDate}
                onChange={startDateChange}
                format="YYYY-MM-DDTHH:mm:ss"
                sx={{ width: "100%" }}
              />
              <DateTimePicker
                label="종료 날짜"
                value={endDate}
                onChange={endDateChange}
                format="YYYY-MM-DDTHH:mm:ss"
                sx={{ width: "100%" }}
              />
            </DemoContainer>
          </LocalizationProvider>
        </div>

        <div style={{ marginBottom: "15px" }}>
          <label>이벤트 url 등록하기</label>
          <input
            style={{ width: "100%" }}
            type="text"
            value={eventUrl}
            onChange={handleEventUrlChange}
          />
        </div>

        <div style={{ marginBottom: "15px" }}>
          <label style={{ marginRight: "15px" }}>이미지 등록</label>
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
        </div>

        <div style={{ marginBottom: "15px" }}>
          <label style={{ marginRight: "15px" }}>서브이미지 등록</label>
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
        </div>

        {title &&
        subTitle &&
        startDate &&
        endDate &&
        content &&
        address &&
        image ? (
          <button type="submit" style={{ cursor: "pointer" }}>
            이벤트 등록하기
          </button>
        ) : (
          <button type="button" disabled>
            이벤트 등록하기
          </button>
        )}
      </form>
    </div>
  );
};

export default EventRegist;
