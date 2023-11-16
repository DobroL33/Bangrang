import React, { useEffect, useState } from "react";
import styles from "./MainPage.module.css";
import { Link, useLocation, useNavigate } from "react-router-dom";
import { useSpring, animated } from "@react-spring/web";

const MainPage: React.FC = () => {
  const [scrollPercent, setScrollPercent] = useState(0);

  const handleScroll = () => {
    const scrollTop = window.scrollY;
    const scrollHeight = 4800;

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

  const cities = ["서울", "구미", "대전", "광주", "부울경"];
  const borderList = [22, 43, 54, 79, 88];

  const cityStyles = cities.map((city, index) => {
    const border = borderList[index];

    const fadeInSpring = useSpring({
      opacity: scrollPercent > border ? 1 : 0,
      config: { duration: 500 },
    });

    const scaleSpring = useSpring({
      transform: `scale(${scrollPercent > border ? 0.8 : 1.2})`,
      config: { duration: 600 },
    });

    return { fadeInSpring, scaleSpring };
  });

  return (
    <div className={styles.Page}>
      <div className={styles.wrap}>
        <div
          className={styles.airplaneScrollTimeline}
          // style={{ height: window.innerWidth * 2.1 }}
        >
          <div className={styles.track}>
            <div className={styles.districtContainer}>
              {cities.map((city, index) => (
                <animated.div
                  className={styles[`distric${index + 1}`]}
                  key={index}
                  style={{
                    ...cityStyles[index].fadeInSpring,
                    ...cityStyles[index].scaleSpring,
                  }}
                >
                  <img src="assets/StampImg.svg" alt="" />
                  <p>{city}</p>
                </animated.div>
              ))}
            </div>
            <img
              className={styles.airplane}
              src="assets/cloud.svg"
              style={{ offsetDistance: scrollPercent + "%" }}
            />
            <svg
              className={styles.flightPath}
              viewBox="0 0 1400 4800"
              xmlns="http://www.w3.org/2000/svg"
            >
              <path
                className={styles.flightPath}
                d="m1158.65,1.31c26.59,293.89-21.19,516.56-62.62,652.13-61.76,202.12-124.95,261.63-176.38,291.87-63.6,37.39-139.4,40.26-291,46-120.93,4.58-162.78-5.59-319,3-118.84,6.54-176.92,16.95-224,61-42.66,39.91-57.35,91.11-62.51,109.52-16.44,58.58-6.87,104.88,7.51,174.48,6.4,30.97,22.92,98.72,64.85,171.29,15.27,26.43,27.52,42.44,34.71,51.33,24.14,29.84,52.37,64.75,100.08,80.73,7.76,2.6,36.09,11.42,72.59,7.13,17.02-2,108.68-15.41,135.76-92.48,14.29-40.68,9.81-96.87-29-135-42.41-41.67-107.94-44.21-152-19-67.4,38.57-65.34,131.06-65,140,.13,3.55.39,6.99.86,10.6,7.06,54.07,22.99,87.35,72.26,187.19,23.21,47.04,30.55,62.46,46.47,84.64,17.68,24.63,36.37,44,40.68,48.69,34.55,37.53,65.56,53.74,126.18,84.64,42.44,21.63,86,43.83,146.14,58.59,52.45,12.88,99.88,14.33,132.23,15.63,49.06,1.96,65.73-1.61,106.19,6.02,29.94,5.65,60.7,11.79,90.32,34.32,17.98,13.68,32.46,31.03,48.8,61.23,42.8,79.06,50.59,157.69,52.19,175.78,8.34,94.08,19.32,217.96-56.32,276.67-45.98,35.68-101.05,29.5-105,29-4.81-.61-72.73-10.39-103-69-33.15-64.17-3.73-153.1,55-181,55.2-26.22,131.22,3.89,166,52,92.98,128.62-91.04,410.47-206,469-44.76,22.79-92,33-92,33-33.76,7.3-59.3,7.44-109,8-14.73.17-26.05.04-35.96-.26-29.7-.9-51.48-2.45-56.21-2.8-44.83-3.36-100.75,1.87-152.83,3.07-108.28,2.49-162.42,3.74-197.03-27.5-30.31-27.36-44.36-70.84-38.97-109.5,9.1-65.24,73.02-116.62,139-116,61.3.58,100.6,52.79,120,78,36.1,46.94,45.35,93.08,57,155,35.01,186.01,52.51,279.02,55,310,10.98,136.83,16.47,205.25-4,276-3.98,13.76-7.77,24.25-12.29,33.98,0,0-21.99,51.78-59.06,92.96-52.93,58.79-92.49,44.99-151.65,95.07-45.02,38.11-64.64,82.09-82,121-14.42,32.33-58.97,136.13-26,255,5.14,18.53,11.12,39.49,26,64,38,62.6,109.84,108.46,186,110,23.78.48,60.85,1.23,94-25,3.35-2.65,44.47-36.27,45-90,.59-59.62-49.06-116.14-112-124-52.81-6.6-107.25,22.03-129,69-23.44,50.64-3.48,110.92,29,145,71.84,75.4,183.33-.02,259,70,49.98,46.25,4.99,82.51,68,212,4.25,8.73,80.79,161.96,186.53,180.43,1.07.19,2.1.36,3.47.57,72.85,11.49,119.06,18.78,149-9,30.34-28.15,27.19-76.74,26-95-1.17-18.06-3.86-59.41-36-86-22.8-18.86-48.73-20.62-69-22-25.51-1.73-71.41-4.85-102,27-32.75,34.1-29.45,88.98-13,124,19.21,40.91,56.53,58.27,69,64,35.98,16.54,75.48,13.92,153,8,21.76-1.66,43.34-3.31,73-8,49.54-7.83,125.14-24.71,217-65"
              />
            </svg>
          </div>
        </div>
      </div>
    </div>
  );
};

export default MainPage;
