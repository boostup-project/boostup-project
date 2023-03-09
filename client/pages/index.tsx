// import SmallBtn from "components/reuse/btn/SmallBtn";
import Carouselcomp from "components/carousel/Carouselcomp";
import LanguageFilter from "components/filter/LanguageFilter";
import MoblieLanguageFilter from "components/filter/MobileLanguageFilter";
import Cards from "../components/Maincard/Cards";
import { useRecoilState } from "recoil";
import { chatActive, roomIdState } from "atoms/chat/chatAtom";
import { useEffect, useState } from "react";
import useWindowSize from "hooks/useWindowSize";
import SeoHead from "components/reuse/SEO/SeoHead";
import axios from "axios";
import { mainCardInfo } from "atoms/main/mainAtom";

async function fetchData() {
  const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/lesson`, {
    cache: "no-store",
  });

  const data = await res.json();
  return data.data;
}

const Home = () => {
  const [active, setActive] = useRecoilState(chatActive);
  const [roomId, setRoomId] = useRecoilState(roomIdState);
  const widthSize = useWindowSize();

  const data: any = fetchData();

  useEffect(() => {
    setActive(false);
    setRoomId(0);
  }, []);

  return (
    <>
      <SeoHead metaType="Home" />
      <div className="flex flex-col bg-bgColor items-center justify-start w-full h-full">
        <Carouselcomp />
        {widthSize > 1024 ? <LanguageFilter /> : <MoblieLanguageFilter />}
        <Cards data={data}></Cards>
      </div>
    </>
  );
};
export default Home;
