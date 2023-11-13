import React, { useEffect, useState } from "react";
import styles from "./MainPage.module.css";
import { Link, useLocation, useNavigate } from "react-router-dom";
import { useSpring, animated } from "@react-spring/web";

const MainPage: React.FC = () => {
  const [scrollPercent, setScrollPercent] = useState(0);

  const handleScroll = () => {
    const scrollTop = window.scrollY;
    const scrollHeight = 3000;

    const percentage = (scrollTop / scrollHeight) * 100;
    setScrollPercent(percentage);

    // Disable horizontal scroll
    window.scrollTo(0, scrollTop);
  };

  useEffect(() => {
    window.addEventListener("scroll", handleScroll);

    return () => {
      window.removeEventListener("scroll", handleScroll);
    };
  }, []);

  /* 서울 */
  // if (scrollPercent > 22 && scrollPercent < 25) {
  //   console.log("서울이에요");
  // }
  /* 구미 */
  // if (scrollPercent > 39) {
  //   console.log("구미에요");
  // }
  /* 대전 */
  // if (scrollPercent > 55) {
  //   console.log("구미에요");
  // }
  /* 광주 */
  // if (scrollPercent > 77) {
  //   console.log("광주에요");
  // }
  /* 부산 */
  // if (scrollPercent > 88) {
  //   console.log("부산이에요");
  // }
  const cities = ["서울", "구미", "대전", "광주", "부울경"];
  const borderList = [22, 44, 55, 77, 88];

  const cityStyles = cities.map((city, index) => {
    const border = borderList[index];

    const fadeInSpring = useSpring({
      opacity: scrollPercent > border ? 1 : 0,
      config: { duration: 500 },
    });

    const scaleSpring = useSpring({
      transform: `scale(${scrollPercent > border ? 1 : 1.3})`,
      config: { duration: 800 },
    });

    return { fadeInSpring, scaleSpring };
  });

  return (
    <div className={styles.Page}>
      {/* <div className={styles.districtContainer}>
        {cities.map((city, index) => (
          <animated.div
            className={styles[`distric${index + 1}`]}
            key={index}
            style={{
              ...cityStyles[index].fadeInSpring,
              ...cityStyles[index].scaleSpring,
            }}
          >
            <img src="assets/orange.svg" alt="" />
            <p>{city}</p>
          </animated.div>
        ))}
      </div> */}
      <div className={styles.wrap}>
        <div
          className={styles.airplaneScrollTimeline}
          style={{ height: window.innerWidth * 2.1 }}
        >
          <div className={styles.track}>
            <img
              className={styles.airplane}
              src="assets/cloud.svg"
              style={{ offsetDistance: scrollPercent + "%" }}
            />
            <svg
              className={styles.flightPath}
              viewBox="0 0 1400 3500"
              xmlns="http://www.w3.org/2000/svg"
            >
              <path
                className={styles.flightPath}
                d="m446.46.12c8.22,32.35,24.36,113.97-8,209-9.06,26.6-45.87,134.7-146,188-104.79,55.78-176.57-2.96-242,58-45.7,42.58-61.78,118.86-41,175,32.52,87.87,150.18,112.5,229,129,50.53,10.58,92.48,13.35,433,8,52.34-.82,107.73-1.77,167,30,71.98,38.58,106.26,101.47,116,120,47.85,91.06,62.63,236.45-10,314-110.92,118.43-390.61,41.14-412,35-37.25-10.7-104.26-34.12-194-27-77.62,6.16-111.82,30.36-123,39-37.89,29.28-86.22,90.4-67,139,19.37,48.98,96.17,57.32,130,61,71,7.72,139.18-15.43,158-22,30.4-10.62,43.17-19.25,75-24,19.41-2.9,42.4-3.82,94,4,33.67,5.1,79.31,13.9,133,30,0,0,89.22,34.97,155,94,10.87,9.76,28.16,26.51,41,53,7.49,15.45,18.35,38.66,14,68-.58,3.9-4.76,29.84-24,51-34.64,38.08-93.41,33.53-139,30-41.69-3.23-96.1-7.44-135-49-5.81-6.2-41.52-44.36-30-73,13.48-33.52,84.1-35.26,128-25,63.84,14.92,97.98,61.49,104,70,10.75,15.18,42.89,60.55,31,118-9.49,45.87-41.44,72.32-51,80-52.52,42.2-117.15,31.88-155,26-29.87-4.64-65.19-10.65-92-40-3.78-4.14-35.76-39.15-25-66,12.99-32.42,80.7-34.07,123-24,15.35,3.65,60.62,15.17,92,59,5.57,7.78,46.45,66.3,22,129-18.08,46.36-60.4,65.62-81,75-49.6,22.58-94.88,14.54-121,10-23.69-4.12-45.31-8.18-65-26-1.74-1.57-38.44-35.58-29-65,10.36-32.28,71.04-42.72,112-31,45.11,12.91,72.36,54.39,74,57,.57.91,1.44,2.29,2.5,4.09,15.41,26.1,27.22,71.96,9.5,114.91-24.77,60.01-90.67,73.79-106,77-13.21,2.76-49.63,9.85-88-8-5.12-2.38-60.45-28.81-57-61,3.07-28.68,51.15-46.51,86-47,4.42-.06,53.03.06,86,39,22.46,26.53,34.02,66.76,24,100-26.21,86.98-206.08,145.54-310,94-22.06-10.94-46.06-29.61-86-31-8.9-.31-62.22-2.16-96,35-45.97,50.57-17.29,131.12-12,145,28.79,75.46,102.1,108.13,116,114,140.98,59.58,233.59-95.62,423-65,19.19,3.1,161.95,27.97,188,120,19.09,67.46-31.78,145.41-84,184-23.19,17.14-37.6,19.82-194,57-116.66,27.73-136.23,32.79-161,53-36.81,30.03-80.19,89.48-70,152,13.22,81.17,109.05,120.35,162,142,82,33.53,156.89,31.04,218,29,60.75-2.02,113.73-12.1,150-19,60.26-11.46,104.63-24.58,116-28,46.95-14.1,108.16-36.06,177-71"
              />
            </svg>
          </div>
        </div>
      </div>
    </div>
  );
};

export default MainPage;
