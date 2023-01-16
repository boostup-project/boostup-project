import React from "react";
import CreateModalContainer from "components/reuse/container/CreateModalContainer";
// import SmallBtn from "components/reuse/btn/SmallBtn";
import StepNavWrapper from "components/reuse/container/StepNavWrapper";
import BasicInfo from "components/createModal/BasicInfo";
import Additional from "components/createModal/Additional";
import Curriculum from "components/createModal/Curriculum";
import Carouselcomp from "components/carousel/Carouselcomp";
import LanguageFilter from "components/filter/LanguageFilter";

const Home = () => {
  return (
    <>
      <div className="flex flex-col bg-bgColor items-center justify-start w-full h-screen">
        <Carouselcomp />
        <LanguageFilter />
        {/* <CreateModalContainer>
          <StepNavWrapper />
          <Additional />
          <BasicInfo />
          <Curriculum />
        </CreateModalContainer> */}
      </div>
    </>
  );
};
export default Home;
