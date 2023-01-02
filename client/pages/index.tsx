import React from "react";

const Home = () => {
  return (
    <>
      <div className="w-screen h-fit bg-pointColor  font-SCDream3 text-lg">
        안녕하세요
      </div>
      <div className="w-screen h-fit desktop:bg-pointColor tablet:bg-textColor bg-bgColor  font-SCDream9 text-lg ">
        안녕하세요
      </div>
      <div className="w-screen h-fit desktop:bg-pointColor tablet:bg-textColor bg-bgColor  font-SCDream9 text-lg ">
        자동배포 들어가요
      </div>
    </>
  );
};
export default Home;
