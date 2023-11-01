import React from "react";
import MainPage from "./intropages/MainPage";
import FirstPage from "./intropages/FirstPage";
import SecondPage from "./intropages/SecondPage";
import ThirdPage from "./intropages/ThirdPage";
import styles from "./introhome.module.css";
const IntroHome: React.FC = () => {
  return (
    <div className={styles.Page}>
      <MainPage></MainPage>
      <FirstPage></FirstPage>
      <SecondPage></SecondPage>
      <ThirdPage></ThirdPage>
    </div>
  );
};

export default IntroHome;
