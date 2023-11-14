import React, { useState, useEffect } from "react";
import DaumPostcode from "react-daum-postcode";
import ReactModal from "react-modal";
interface Props {
  setAddress: (value: string) => void;
  address: string;
}
const PostCode: React.FC<Props> = ({ setAddress, address }) => {
  // const [zipCode, setZipcode] = useState<string>("");
  const [roadAddress, setRoadAddress] = useState<string>("");
  // const [detailAddress, setDetailAddress] = useState<string>(""); // 추가
  const [isOpen, setIsOpen] = useState<boolean>(false); //추가

  useEffect(()=>{
    setRoadAddress(address);
  },[])
  const completeHandler = (data: any) => {
    // setZipcode(data.zonecode);
    // setRoadAddress(data.roadAddress);
    setAddress(data.roadAddress);
    // setAddress(roadAddress + detailAddress);
    setIsOpen(false); //추가
    // console.log(zipCode, roadAddress, detailAddress);
  };

  // Modal 스타일
  const customStyles = {
    overlay: {
      backgroundColor: "rgba(0,0,0,0.5)",
      zIndex: 1000, 
    },
    content: {
      left: "0",
      margin: "auto",
      width: "500px",
      height: "600px",
      padding: "0",
      overflow: "hidden",
      zIndex: 1001, 
    },
  };

  // 검색 클릭
  const toggle = () => {
    setIsOpen(!isOpen);
  };

  // 상세 주소검색 event
  // const changeHandler = (e: React.ChangeEvent<HTMLInputElement>) => {
  //   setDetailAddress(e.target.value);
  // };

  // 추가
  // const clickHandler = () => {
  //   console.log("주소 저장하기");
  //   // setAddress(roadAddress);
  //   // console.log(zipCode, roadAddress, detailAddress);
  // };

  return (
    <div style={{display:'flex', width:'70%', justifyContent:'space-between'}}>
      {/* <input
        value={zipCode}
        readOnly
        placeholder="우편번호"
        style={{ width: "30%" }}
      /> */}
      <input
        value={address}
        readOnly
        placeholder="도로명 주소"
        style={{ width: "85%",minHeight:'30px',borderRadius:'5px', border:'1px solid black' }}
      />
  
      <button type="button" onClick={toggle} style={{ width:'12%',borderRadius:'5px', border:'1px solid black'}}>
        찾기
      </button>
      <ReactModal isOpen={isOpen} ariaHideApp={false} style={customStyles}>
        <div
          style={{
            display: "flex",
            flexDirection: "column",
            alignItems: "center"}}>
          <DaumPostcode onComplete={completeHandler} />
          <button
            type="button"
            onClick={toggle}
            style={{
              marginTop: "10px",
              width: "10%",
              background: "#FF5353",
              color: "white",
              border: "none",
              padding: "5px",
              fontWeight: "bold",
            }}>
            X
          </button>
        </div>
      </ReactModal>
    </div>
  );
};

export default PostCode;
