import React from "react";

const NotFound: React.FC = () => {
  return (
    <div style={{width:'100vw', height:'100vh', boxSizing:'border-box'}}>
        <h1 style={{margin:0, marginBottom:'2%'}}>404 Not Found</h1>
        <img crossOrigin="anonymous" src="https://img1.daumcdn.net/thumb/R658x0.q70/?fname=https://t1.daumcdn.net/news/202105/22/maxim/20210522012550092ozog.jpg" alt="" style={{height:'85vh'}}/>
    </div>
  );
};
export default NotFound;
