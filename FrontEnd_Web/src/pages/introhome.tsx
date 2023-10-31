import React from "react";
import FirstPage from "./intropages/FirstPage";
import SecondPage from "./intropages/SecondPage";
import ThirdPage from "./intropages/ThirdPage";

const IntroHome: React.FC = () => {
  return (
    <div>
      <FirstPage></FirstPage>
      {/* <SecondPage></SecondPage> */}
      <ThirdPage></ThirdPage>
    </div>
  );
};

export default IntroHome;
