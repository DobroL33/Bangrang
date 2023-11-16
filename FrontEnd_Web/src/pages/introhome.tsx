import React, { useState, useEffect } from "react";
import MainPage from "./intropages/MainPage";
// import FirstPage from "./intropages/FirstPage";
import SecondPage from "./intropages/SecondPage";
import ThirdPage from "./intropages/ThirdPage";
import styles from "./introhome.module.css";
import { WiDirectionUp } from "react-icons/wi";
const IntroHome: React.FC = () => {
  const [scrollPercent, setScrollPercent] = useState(0);
  const [visible, setVisible] = useState(false);

  const handleScroll = () => {
    const scrollTop = window.scrollY;
    const scrollHeight =
      document.documentElement.scrollHeight - window.innerHeight;

    const percentage = (scrollTop / scrollHeight) * 100;
    setScrollPercent(percentage);

    if (percentage >= 93) {
      setVisible(true);
    } else {
      setVisible(false);
    }
  };

  useEffect(() => {
    window.addEventListener("scroll", handleScroll);

    return () => {
      window.removeEventListener("scroll", handleScroll);
    };
  }, []);
  const scrollToTop = () => {
    const scrollStep = -window.scrollY / (300 / 10);
    const scrollInterval = setInterval(() => {
      if (window.scrollY !== 0) {
        window.scrollBy(0, scrollStep);
      } else {
        clearInterval(scrollInterval);
      }
    }, 10);
  };
  return (
    <div className={styles.Page}>
      <p
        style={{
          position: "fixed",
          top: "10%",
          left: "3%",
          transition: "1s",
          fontSize: "30px",
          opacity: scrollPercent < 95 ? "1" : "0",
          color: "#1daeff",
          zIndex: "5",
        }}
      >
        방랑도
      </p>
      <p
        style={{
          position: "fixed",
          top: "14%",
          left: "3%",
          transition: "1s",
          fontSize: "30px",
          opacity: scrollPercent < 95 ? "1" : "0",
          color: "#1daeff",
          zIndex: "5",
        }}
      >
        {scrollPercent.toFixed(0)}%
      </p>
      <MainPage></MainPage>
      {/* <FirstPage></FirstPage> */}
      <SecondPage></SecondPage>
      <ThirdPage visible={visible}></ThirdPage>


      <div
        className={`${styles.buttonContainer} ${
          scrollPercent > 50 ? styles.show : ""
        }`}
      > 
      {scrollPercent > 50 &&
        <button className={styles.pageup} onClick={() => scrollToTop()}>
          <WiDirectionUp style={{ fontSize: 50 }} />
        </button> }
      </div>
    </div>
  );
};

export default IntroHome;
