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
            right: scrollPercent < 100 ? "10%" : "38%",
            transition: "0.7s",
            fontSize: scrollPercent < 100 ? "30px" : "50px",
            color: scrollPercent < 100 ? "#FFFFFF" : "#FFFFFF",
          }}
        >
          현재 정복률 : {scrollPercent.toFixed(0)}%
        </p>
        <p
          style={{
            position: "absolute",
            top: "40%",
            left: "40%",
            fontSize: "50px",
            color: "#FFFFFF",
            transition: "1s",
            opacity: scrollPercent < 100 ? "0" : "1",
          }}
        >
          안녕하세융
        </p>
      </div>
    </div>
  );
};

export default ThirdPage;
