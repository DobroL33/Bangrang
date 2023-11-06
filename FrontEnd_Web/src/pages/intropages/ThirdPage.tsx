import React, { useState, useEffect } from "react";
import { useTransition, animated } from "@react-spring/web";
import shuffle from "lodash.shuffle";
import data from "./DataList";
import styles from "./ThirdPage.module.css";
import Marquee from "react-fast-marquee";

const Images = [
  "public/assets/MarqueeImg/대전.png",
  "public/assets/MarqueeImg/서울.png",
];

function List() {
  const [rows, set] = useState(data);
  useEffect(() => {
    const t = setInterval(() => set(shuffle), 3000);
    return () => clearInterval(t);
  }, []);

  let height = 0;
  const transitions = useTransition(
    rows.map((data) => ({ ...data, y: (height += data.height) - data.height })),
    {
      key: (item: any) => item.name,
      from: { height: 0, opacity: 0 },
      leave: { height: 0, opacity: 0 },
      enter: ({ y, height }) => ({ y, height, opacity: 1 }),
      update: ({ y, height }) => ({ y, height, opacity: 1 }),
    }
  );

  return (
    <div className={styles.list}>
      {transitions((style, item, t, index) => (
        <animated.div
          className={styles.card}
          style={{ zIndex: data.length - index, ...style }}
        >
          <div className={styles.cell}>
            <div
              className={styles.details}
              style={{
                backgroundImage: item.css,
                color: "white",
                fontSize: "20px",
                fontWeight: "bold",
                // alignItems: "center",
                justifyContent: "center",
                display: "flex",
                flexDirection: "column",
                // marginTop: "30px",
              }}
            >
              <p
                style={{
                  textAlign: "start",
                  padding: "20px",
                  fontFamily: "bold",
                }}
              >
                {index + 1}등 {item.character}
              </p>
            </div>
          </div>
        </animated.div>
      ))}
    </div>
  );
}

interface ThirdPageProps {
  visible: boolean;
}

const ThirdPage: React.FC<ThirdPageProps> = ({ visible }) => {
  return (
    <div>
      <div className={styles.Page}>
        <p
          className={styles.Percent}
          style={{
            position: "absolute",
            right: visible ? "43%" : "10%",
            transition: "0.7s",
            opacity: visible ? "1" : "0",
            fontSize: "30px",
            color: "#1daeff",
          }}
        >
          방랑 명예의 전당
        </p>
        <div className={styles.detailContainer}>여기에는 간략한 설명</div>
        <div
          className={styles.listContainer}
          style={{
            opacity: visible ? "1" : "0",
            transition: "1s",
          }}
        >
          <List />
        </div>
        <div className={styles.messageContainer}>
          여기에는 알림창 같은 메시지
        </div>
      </div>
      <p
        style={{
          fontSize: "25px",
          color: "skyblue",
          fontWeight: "bold",
          backgroundColor: "#ffffff",
          textShadow: "#00d0ff71 1px 0 10px",
        }}
      >
        각 지역의 다양한 도장도 모아보세요 !!
      </p>
      <Marquee
        className="marquee"
        play={true}
        loop={300000}
        style={{
          height: "auto",
          paddingBottom: "20px",
          backgroundColor: "#fafdff",
          // marginBottom: "50px",
          overflow: "hidden",
        }}
        direction="left"
        speed={300}
      >
        <img className={styles.image} src="assets/MarqueeImg/서울.png" alt="" />
        <img className={styles.image} src="assets/MarqueeImg/대전.png" alt="" />
        <img className={styles.image} src="assets/MarqueeImg/대구.png" alt="" />
        <img className={styles.image} src="assets/MarqueeImg/부산.png" alt="" />
        <img className={styles.image} src="assets/MarqueeImg/제주.png" alt="" />
        <img className={styles.image} src="assets/MarqueeImg/경주.png" alt="" />
        <img className={styles.image} src="assets/MarqueeImg/서울.png" alt="" />
        <img className={styles.image} src="assets/MarqueeImg/대전.png" alt="" />
        <img className={styles.image} src="assets/MarqueeImg/대구.png" alt="" />
        <img className={styles.image} src="assets/MarqueeImg/부산.png" alt="" />
        <img className={styles.image} src="assets/MarqueeImg/제주.png" alt="" />
        <img className={styles.image} src="assets/MarqueeImg/경주.png" alt="" />
      </Marquee>
    </div>
  );
};

export default ThirdPage;
