import React from "react";
import CreateModalContainer from "components/reuse/container/CreateModalContainer";
import SmallBtn from "components/reuse/btn/SmallBtn";
import StepNavWrapper from "components/reuse/container/StepNavWrapper";
import Additional from "components/Additional";

const Home = () => {
  return (
    <>
      <div className="flex flex-col bg-bgColor items-center justify-center w-full h-screen">
        <CreateModalContainer>
          <StepNavWrapper />
          <Additional />
          <SmallBtn>다음</SmallBtn>
        </CreateModalContainer>
      </div>
    </>
  );
};
export default Home;
