import React, { useRef } from "react";
import styles from "./FirstPage.module.css";
import Marquee from "react-fast-marquee";

const Image = document.querySelector(".loop");
const Image1 = Marquee;

const FirstPage: React.FC = () => {
  return (
    <div className={styles.Page}>
      <div className={styles.Container}>
        <div className={styles.PathContainer}>
          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 4347.37 4961.22">
            <path d="m4322.88.05c-5.2,47.95-21.3,128.6-79.55,194.69-28.08,31.86-80.93,77.32-265.16,111.25-146.18,26.92-180.18,10.7-415.42,27.81-105.84,7.7-192.74,14.32-309.36,37.08-97.03,18.94-162.05,38.8-247.49,64.9-201.51,61.55-238.32,93.62-256.33,111.25-23.68,23.2-89.84,90-97.23,194.69-1.16,16.36-4.83,78.65,26.52,139.07,57.03,109.94,190.18,136.93,300.52,157.61,156.37,29.31,222.01,5.72,397.75,9.27,122.88,2.48,248.38,18.26,380.07,46.36,203.23,43.36,285.42,92.55,335.88,129.8,43.76,32.31,93.63,69.95,132.58,139.07,44.16,78.37,84.2,214.76,35.36,315.22-53.7,110.45-192.32,131.11-459.62,166.88-86.79,11.61-185.55,20.67-530.33,27.81-340.26,7.05-380.19,1.05-486.13,27.81-96.04,24.26-247.85,64.55-388.91,194.69-76.41,70.5-197,181.76-203.29,333.76-9.99,241.11,274.57,416.5,335.88,454.28,275.4,169.75,580.01,142.32,822.01,120.52,231.92-20.88,347.88-31.33,450.78-74.17,22.31-9.29,91.06-39.19,185.62-46.36,59.54-4.51,126.36-9.57,194.45,27.81,93.78,51.48,128.77,149.85,141.42,185.42,39.18,110.14,23.88,219.66,0,287.4-71.75,203.53-309.03,279.69-486.13,333.76-242.03,73.9-453.38,71.9-866.2,64.9-293.05-4.97-407.69-23.31-627.56,18.54-146.87,27.96-278.01,70.84-406.59,139.07-168.09,89.2-350.49,185.99-380.07,361.57-25.22,149.71,73.39,280.99,106.07,324.49,123.34,164.2,310.58,236.52,627.56,287.4,392.46,63,703.35,48.13,910.4,37.08,232.54-12.41,575.3-31.08,998.79-55.63" />
          </svg>
        </div>
        <Marquee
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
        </Marquee>
        <p className={styles.comment2} style={{ marginTop: "5%" }}>
          연습용 페이집니당
        </p>
      </div>
    </div>
  );
};

export default FirstPage;
