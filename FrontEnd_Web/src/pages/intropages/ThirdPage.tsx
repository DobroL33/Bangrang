import React, { useState, useEffect } from "react";
import styles from "./ThirdPage.module.css";

const ThirdPage: React.FC = () => {
  const [scrollPercent, setScrollPercent] = useState(0);

  const handleScroll = () => {
    const scrollTop = window.scrollY;
    const scrollHeight =
      document.documentElement.scrollHeight - window.innerHeight;

    const percentage = (scrollTop / scrollHeight) * 100;
    setScrollPercent(percentage);
  };

  useEffect(() => {
    window.addEventListener("scroll", handleScroll);

    return () => {
      window.removeEventListener("scroll", handleScroll);
    };
  }, []);

  return (
    <div>
      <div className={styles.Background} style={{ position: "absolute" }}>
        <p
          className={styles.Percent}
          style={{
            position: "absolute",
            right: scrollPercent < 100 ? "10%" : "35%",
            transition: "0.7s",
            fontSize: scrollPercent < 100 ? "30px" : "50px",
            color: scrollPercent < 100 ? "#FFFFFF" : "#FFFFFF",
          }}
        >
          현재 정복률 : {scrollPercent.toFixed(0)}%
        </p>
        <img
          src="assets/Busy.gif"
          alt=""
          style={{
            position: "absolute",
            top: "40%",
            left: "40%",
            transition: "0.75s",
            opacity: scrollPercent < 99 ? "0" : "1",
          }}
        />
      </div>
    </div>
  );
};

export default ThirdPage;
