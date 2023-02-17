// import SmallBtn from "components/reuse/btn/SmallBtn";
import Carouselcomp from "components/carousel/Carouselcomp";
import LanguageFilter from "components/filter/LanguageFilter";
import MoblieLanguageFilter from "components/filter/MobileLanguageFilter";
import Cards from "../components/Maincard/Cards";
import { useRecoilState } from "recoil";
import { chatActive, roomIdState } from "atoms/chat/chatAtom";
import { useEffect } from "react";
import useWindowSize from "hooks/useWindowSize";
const Home = () => {
  const [active, setActive] = useRecoilState(chatActive);
  const [roomId, setRoomId] = useRecoilState(roomIdState);
  const widthSize = useWindowSize();

  useEffect(() => {
    setActive(false);
    setRoomId(0);
  }, []);

  return (
    <>
      <div className="flex flex-col bg-bgColor items-center justify-start w-full h-full">
        <Carouselcomp />
        {widthSize > 764 ? <LanguageFilter /> : <MoblieLanguageFilter />}
        <Cards></Cards>
      </div>
    </>
  );
};
export default Home;
