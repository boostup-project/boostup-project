import React from "react";
import CreateModalContainer from "components/reuse/container/CreateModalContainer";
// import SmallBtn from "components/reuse/btn/SmallBtn";
import StepNavWrapper from "components/reuse/container/StepNavWrapper";
import Curriculum from "components/createModal/Curriculum";

const Home = () => {
  return (
    <>
      <div className="flex flex-col bg-bgColor items-center justify-center w-full h-screen">
        <CreateModalContainer>
          <StepNavWrapper />
          <Curriculum />
        </CreateModalContainer>
      </div>
    </>
  );
};
export default Home;
