import React, {
  useState,
  ChangeEvent,
  useEffect,
  FormEvent,
  useRef,
} from "react";
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
import { FormEncType, useParams } from "react-router-dom";
import { spawn } from "child_process";
import styles from "./eventupdate.module.css";

interface Event {
  title: string;
  subTitle: string;
  content: string;
  startDate: string;
  endDate: string;
  address: string;
  eventUrl: string;
}

const EventUpdate: React.FC = () => {
  const { eventIdx } = useParams();
  const imgRef = useRef();
  const [event, setEvent] = useState<Event>({
    title: "",
    subTitle: "",
    content: "",
    startDate: "",
    endDate: "",
    address: "",
    eventUrl: "",
  });
  const [imageSrc, setImageSrc] = useState<string | null>("");
  const [image, setImage] = useState<FileList | null>(null);
  const [subImage, setSubImage] = useState<FileList | null>(null);
  const [updateStartDate, setUpdateStartDate] = useState("");
  const [updateEndDate, setUpdateEndDate] = useState("");
  const [nowStartDate, setNowStartDate] = useState("");
  const [nowEndDate, setNowEndDate] = useState("");

  useEffect(() => {
    const [datePart, timePart] = event.startDate.split("T");
    setNowStartDate(datePart + " " + timePart);
  }, [event.startDate]);

  useEffect(() => {
    const [datePart, timePart] = event.startDate.split("T");
    setNowEndDate(datePart + " " + timePart);
  }, [event.endDate]);

  useEffect(() => {
    axiosInstance
      .get(`${process.env.REACT_APP_API}/web/event/${eventIdx}`)
      .then((res) => {
        // console.log(res);
        // 응답에서 데이터 추출 및 상태 업데이트
        const eventData = res.data;
        setEvent({
          title: eventData.title,
          subTitle: eventData.subtitle,
          content: eventData.content,
          startDate: eventData.startDate,
          endDate: eventData.endDate,
          address: eventData.address,
          eventUrl: eventData.eventUrl,
        });
        setImageSrc(eventData.image);
        setImage(eventData.image);
        setSubImage(eventData.subImage);
      })
      .catch((err) => console.log(err));
  }, [eventIdx]);

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

  const startDateChange = (date: string | null) => {
    if (date) {
      const formattedDate = dayjs(date).format(datePickerFormat);
      setEvent((prev) => ({
        ...prev,
        startDate: formattedDate,
      }));
    }
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
      method: "put",
      url: `${process.env.REACT_APP_API}/web/event/${eventIdx}`,
      data: formData,
      headers: {
        "Content-Type": "multipart/form-data",
      },
    })
      .then((res) => {
        console.log(res);
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
        <br />
        <p>시작 날짜 : {nowStartDate}</p>
        <p>종료 날짜 : {nowEndDate}</p>

        <LocalizationProvider dateAdapter={AdapterDayjs}>
          <DemoContainer components={["DateTimePicker", "DateTimePicker"]}>
            <DateTimePicker
              label="시작 날짜"
              sx={{ width: "100%" }}
              value={updateStartDate}
              defaultValue={dayjs("2022-04-17T15:30").toString()}
              onChange={startDateChange}
              format="YYYY-MM-DDTHH:mm:ss"
            />
            <DateTimePicker
              sx={{ width: "100%" }}
              label="종료 날짜"
              value={updateEndDate}
              onChange={endDateChange}
              format="YYYY-MM-DDTHH:mm:ss"
            />
          </DemoContainer>
        </LocalizationProvider>
        <br />

        <div
          style={{
            marginBottom: "15px",
            display: "flex",
            justifyContent: "center",
          }}
        >
          <label style={{ width: "100px" }}>이벤트 URL:</label>
          <input
            className={styles.select_input}
            style={{
              width: "100%",
              boxSizing: "border-box",
              border: "1px solid black",
              borderRadius: "5px",
            }}
            type="text"
            value={eventUrl}
            onChange={handleEventUrlChange}
          />
        </div>

        <div
          style={{
            marginTop: "15px",
            marginBottom: "15px",
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
          }}
        >
          <label htmlFor="imageInput">이미지 등록</label>

          <div style={{ display: "flex", alignItems: "center" }}>
            {imageSrc ? (
              <img
                style={{ width: "20vw", height: "20vh", marginRight: "10px" }}
                src={imageSrc}
                alt="미리보기"
              />
            ) : (
              <span>이미지 입력되었습니다.</span>
            )}

            <input
              type="file"
              id="imageInput"
              style={{ display: "none" }}
              onChange={({ target: { files } }) => {
                if (files && files[0]) {
                  setImage(files);
                  setImageSrc(null);
                } else {
                  setImage(null);
                }
              }}
            />
            <div
              style={{
                border: "1px solid gray",
                padding: "4px",
                borderRadius: "5px",
                fontSize: "10px",
                background: "#CDCDCD",
              }}
            >
              <label htmlFor="imageInput" style={{ cursor: "pointer" }}>
                이미지 등록버튼
              </label>
            </div>
          </div>

          <p
            style={{
              fontSize: "8px",
              fontWeight: "bold",
              color: image ? "green" : "red",
            }}
          >
            {image === null ? "파일을 첨부해주세요" : "첨부완료"}
          </p>
        </div>
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
        <div style={{ marginTop: "10px" }}>
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
        </div>
      </form>
    </div>
  );
};

export default EventUpdate;
