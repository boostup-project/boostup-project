// import SmallBtn from "components/reuse/btn/SmallBtn";
import Carouselcomp from "components/carousel/Carouselcomp";
import LanguageFilter from "components/filter/LanguageFilter";
import MoblieLanguageFilter from "components/filter/MobileLanguageFilter";
import Cards from "../components/Maincard/Cards";
import { useSetRecoilState } from "recoil";
import { chatActive } from "atoms/chat/chatAtom";
import { useEffect } from "react";
const Home = () => {
  const setActive = useSetRecoilState(chatActive);

  useEffect(() => {
    setActive(false);
  }, []);

  return (
    <>
      <div className="flex flex-col bg-bgColor items-center justify-start w-full h-full">
        <Carouselcomp />
        <LanguageFilter />
        <MoblieLanguageFilter />
        <Cards></Cards>
      </div>
    </>
  );
};
export default Home;
