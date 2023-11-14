import React, { useState, useEffect } from "react";
import { useNavigate, Link } from "react-router-dom";
import { useFormik } from "formik";
import * as Yup from "yup";
import axios from "axios";
import styled from "@emotion/styled";

const InputContainer = styled.div`
  display: flex;
  align-items: center;
  width: 25%;
  height: 2.625rem;
  color: #c6c5c5;
  border: 1px solid #3d5cff;
  border-radius: 5px;
  justify-content: space-between;
`;

const StyledInput = styled.input`
  flex-shrink: 0;
  padding-left: 2rem;
  border: none;
  outline: none;
  color: #525252;
  font-weight: bold;
`;

const validationSchema = Yup.object().shape({
  id: Yup.string()
    .matches(
      /^[a-zA-Z0-9._]{4,20}$/,
      "4~20 자, 영문,숫자, 특수문자 _ 만 사용가능"
    )
    .required("아이디를 입력해주세요"),
  password: Yup.string()
    .min(9, "9 자 이상, 영문/숫자/특수문자 1개 이상 포함")
    .max(15, "15 자 이하의 패스워드를 입력해주세요")
    .matches(
      /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{9,15}$/,
      "영문/숫자/특수문자 1개 이상 포함"
    )
    .required("비밀번호를 입력해주세요"),
  confirmPassword: Yup.string()
    .oneOf([Yup.ref("password") as any, null], "비밀번호가 일치하지 않습니다.")
    .required("비밀번호를 확인해주세요"),
  organizationName: Yup.string().required("기관명을 입력해주세요"),
});

const SignUp: React.FC = () => {
  const navigate = useNavigate();
  const [isCheckId, setCheckId] = useState(false); //아이디 중복 검사 체크변수
  const [file, setFile] = useState<FileList | null>(null);
  useEffect(() => {
    const AccessToken = localStorage.getItem("AccessToken");
    if (AccessToken) {
      navigate("/manage");
    }
  }, [navigate]);

  const formik = useFormik({
    initialValues: {
      id: "",
      password: "",
      confirmPassword: "",
      organizationName: "",
    },
    validationSchema: validationSchema,
    onSubmit: (values) => {
      // 회원가입 요청 로직
      if (!file) {
        // 첨부 파일없으면 리턴
        return;
      }
      const formData = new FormData();
      formData.append("authFile", file[0]);
      formData.append(
        "userData",
        JSON.stringify({
          id: values.id,
          password: values.password,
          organizationName: values.organizationName,
        })
      );
      axios({
        method: "post",
        url: `${process.env.REACT_APP_API}/web/signup`,
        data: formData,
        headers: {
          "Content-Type": "multipart/form-data",
        },
      })
        .then((res) => {
          console.log(res);
          navigate("/login");
        })
        .catch((err) => {
          console.log(err);
          alert("회원가입 실패!");
        });
    },
  });

  const idcheck = (id: string) => {
    axios({
      method: "get",
      url: `${process.env.REACT_APP_API}/web/idCheck/${id}`,
    })
      .then((res) => {
        setCheckId(true);
      })
      .catch((err) => {
        alert("이미 존재하는 아이디입니다.");
      });
  };

  return (
    <div>
      <img src="assets/logo.png" alt="" style={{ width: "50vh" }} />
      <h1>관리자 가입신청</h1>
      <form onSubmit={formik.handleSubmit}>
        {/* 아이디 input */}
        <div
          style={{
            display: "flex",
            justifyContent: "center",
            alignItems: "center",
          }}
        >
          <InputContainer>
            <StyledInput
              type="text"
              placeholder="아이디"
              {...formik.getFieldProps("id")}
              onChange={(event) => {
                formik.handleChange(event);
                setCheckId(false);
              }}
              autoComplete="id"
            />
            {/* 올바른 아이디 입력시 인증버튼 활성화 */}
            {formik.values.id === "" || formik.errors.id ? (
              <button style={{ marginRight: "10%" }} type="button" disabled>
                인증
              </button>
            ) : isCheckId ? (
              <button
                disabled
                type="button"
                style={{ color: "green", marginRight: "10%" }}
              >
                완료
              </button>
            ) : (
              <button
                type="button"
                style={{ marginRight: "10%" }}
                onClick={() => {
                  idcheck(formik.values.id);
                }}
              >
                인증
              </button>
            )}
          </InputContainer>
        </div>
        <p style={{ fontSize: "8px", fontWeight: "bold" }}>
          {formik.values.id === "" ? (
            <span> </span>
          ) : formik.errors.id ? (
            <span style={{ color: "red" }}>{formik.errors.id}</span>
          ) : !isCheckId ? (
            <span style={{ color: "yellowgreen" }}>
              아이디 중복을 확인해주세요.
            </span>
          ) : (
            <span style={{ color: "green" }}>사용 가능한 아이디입니다.</span>
          )}
        </p>

        {/* 패스워드 input */}
        <div
          style={{
            display: "flex",
            justifyContent: "center",
            alignItems: "center",
          }}
        >
          <InputContainer>
            <StyledInput
              type="password"
              placeholder="비밀번호"
              {...formik.getFieldProps("password")}
              autoComplete="new-password"
            />
          </InputContainer>
        </div>
        <p style={{ fontSize: "8px", fontWeight: "bold" }}>
          {formik.values.password === "" ? (
            <span> </span>
          ) : formik.errors.password ? (
            <span style={{ color: "red" }}>{formik.errors.password}</span>
          ) : (
            <span style={{ color: "green" }}>
              사용할 수 있는 비밀번호입니다.
            </span>
          )}
        </p>

        {/* 패스워드 확인 input */}
        <div
          style={{
            display: "flex",
            justifyContent: "center",
            alignItems: "center",
          }}
        >
          <InputContainer>
            <StyledInput
              type="password"
              placeholder="비밀번호 확인"
              {...formik.getFieldProps("confirmPassword")}
              autoComplete="new-password"
            />
          </InputContainer>
        </div>
        <p style={{ fontSize: "8px", fontWeight: "bold", color: "red" }}>
          {formik.values.confirmPassword === "" ? (
            <span> </span>
          ) : formik.errors.confirmPassword ? (
            <span>{formik.errors.confirmPassword}</span>
          ) : formik.errors.password ? (
            <span>비밀번호를 확인해주세요.</span>
          ) : (
            <span style={{ color: "green" }}>비밀번호가 일치합니다.</span>
          )}
        </p>

        {/* 기관 이름 input */}
        <div
          style={{
            display: "flex",
            justifyContent: "center",
            alignItems: "center",
          }}
        >
          <InputContainer>
            <StyledInput
              type="text"
              placeholder="기관명"
              {...formik.getFieldProps("organizationName")}
            />
          </InputContainer>
        </div>
        <p style={{ fontSize: "8px", fontWeight: "bold", color: "red" }}>
          {formik.values.organizationName === "" ? (
            <span></span>
          ) : formik.errors.organizationName ? (
            <span>{formik.errors.organizationName}</span>
          ) : (
            <span style={{ color: "green" }}>입력완료</span>
          )}
        </p>

        {/* 인증파일 input */}
        <p>인증파일 등록</p>
        <input
          type="file"
          placeholder="인증파일"
          accept=".pdf, .doc, .docx, .hwp, .hwpx"
          onChange={({ target: { files } }) => {
            if (files && files[0]) {
              setFile(files);
            }
          }}
        />
        <p style={{ fontSize: "8px", fontWeight: "bold", color: "red" }}>
          {file === null ? (
            <span>파일을 첨부해주세요</span>
          ) : (
            <span style={{ color: "green" }}>첨부완료</span>
          )}
        </p>

        {/* 정상적으로 모든 입력이 되었을때 버튼 활성화 */}
        {isCheckId &&
        formik.values.password !== "" &&
        formik.values.confirmPassword !== "" &&
        formik.values.organizationName !== "" &&
        !formik.errors.password &&
        !formik.errors.confirmPassword &&
        !formik.errors.organizationName &&
        file ? (
          <button
            type="submit"
            style={{
              marginTop: "30px",
              border: "none",
              background: "#0057FF",
              width: "25%",
              height: "6vh",
              borderRadius: "6px",
              color: "white",
              fontWeight: "bold",
              fontSize: "18px",
            }}
          >
            가입신청
          </button>
        ) : (
          <button
            style={{
              width: "25%",
              height: "6vh",
            }}
            disabled
          >
            가입신청
          </button>
        )}
      </form>
    </div>
  );
};
export default SignUp;
