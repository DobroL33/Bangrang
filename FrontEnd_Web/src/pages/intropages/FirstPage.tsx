import React, { useRef } from "react";
import styles from "./FirstPage.module.css";
import Marquee from "react-fast-marquee";

const Image = document.querySelector(".loop");
const Image1 = Marquee;

const FirstPage: React.FC = () => {
  return (
    <div className={styles.Page}>
      <div className={styles.Container}>
        {/* <Marquee
          className="marquee"
          play={true}
          loop={300000}
          style={{
            marginTop: "10%",
            // background: "linear-gradient(to bottom, #ffffff 0%, #d5ecf9 100%)",
            width: "1500px",
            height: "180px",
            overflow: "hidden",
            borderRadius: "20px",
            transform: "rotate(-5deg)",
          }}
          direction="left"
          speed={500}
        >
          <img
            className={styles.loop}
            src="assets/MarqueeImg/Spring.gif"
            alt=""
          />
          <p className={styles.comment1}>봄이 좋냐? - 10cm</p>
          <img
            className={styles.loop}
            src="assets/MarqueeImg/Summer.gif"
            alt=""
          />
          <p className={styles.comment2}>여행 - 볼빨간 사춘기</p>
          <img
            className={styles.loop}
            src="assets/MarqueeImg/Autumn.gif"
            alt=""
          />
          <p className={styles.comment3}>가을타나봐 - 바이브</p>
          <img
            className={styles.loop}
            src="assets/MarqueeImg/Winter.gif"
            alt=""
          />
          <p className={styles.comment4}>겨울을 걷는다 - 윤딴딴</p>
        </Marquee> */}
      </div>
    </div>
  );
};

export default FirstPage;
