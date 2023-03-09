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

// async function fetchData() {
//   const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/lesson`, {
//     cache: "no-store",
//   });

//   const data = await res.json();
//   return data.data;
// }

export async function getServerSideProps() {
  const res = await axios.get(`${process.env.NEXT_PUBLIC_API_URL}/lesson`);
  const data = res.data.data;

  return { props: { data } };
}

const Home = (data: any) => {
  const [active, setActive] = useRecoilState(chatActive);
  const [roomId, setRoomId] = useRecoilState(roomIdState);
  const widthSize = useWindowSize();
  const [cardData, setCardData] = useRecoilState(mainCardInfo);
  // const data: any = fetchData();
  // console.log(data.data);

  useEffect(() => {
    setActive(false);
    setRoomId(0);
    setCardData(data.data);
  }, []);

  return (
    <>
      <SeoHead metaType="Home" />
      <div className="flex flex-col bg-bgColor items-center justify-start w-full h-full">
        <Carouselcomp />
        {widthSize > 1024 ? <LanguageFilter /> : <MoblieLanguageFilter />}
        <Cards data={data.data}></Cards>
      </div>
    </>
  );
};
export default Home;
