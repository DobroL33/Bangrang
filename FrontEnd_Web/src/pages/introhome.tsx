import React from "react";
import FirstPage from "./intropages/FirstPage";
import SecondPage from "./intropages/SecondPage";

const IntroHome: React.FC = () => {
  return (
    <div>
      <FirstPage></FirstPage>
      <SecondPage></SecondPage>
    </div>
  );
};

export default IntroHome;
