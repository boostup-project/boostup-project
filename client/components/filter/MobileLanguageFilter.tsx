import { filterModal, mainCardInfo } from "atoms/main/mainAtom";
import { useRecoilState } from "recoil";
import { detailLangDict } from "components/reuse/dict";
import Image from "next/image";
import { useEffect, useState } from "react";
import useGetFilteredBoard from "hooks/board/useGetFilteredBoard";
import getMainCard from "apis/card/getMainCard";
import { useQuery } from "@tanstack/react-query";

const MoblieLanguageFilter = () => {
  const [modal, setModal] = useRecoilState(filterModal);
  const [langId, setLangId] = useState<number>(0);
  const { refetch } = useGetFilteredBoard(langId);
  const [cards, setMainCardInfo] = useRecoilState(mainCardInfo);

  const {
    refetch: cardRefetch,
    data: cardData,
    isSuccess,
  } = useQuery(["cards"], getMainCard, {
    enabled: false,
    onSuccess: data => {
      setMainCardInfo(data.data.data);
    },
    retry: 2,
  });

  useEffect(() => {
    if (langId !== 0) {
      refetch();
    }
  }, [langId]);

  // 하단 Navbar의 필터 버튼 클릭시 20~26줄 코드를 넣어주시면 됩니다!
  // const handleModalClick = () => {
  //   if (modal === 0) {
  //     setModal(48);
  //   } else {
  //     setModal(0);
  //   }
  // };

  const handleFilterClick = (languageId: number) => {
    setLangId(languageId);
  };

  const handleAllClick = () => {
    cardRefetch();
  };
  return (
    <>
      <div
        className={
          modal === 0
            ? `right-0 ease-in-out translate-x-0 duration-500 flex flex-col justify-between items-center fixed overflow-auto scrollbar-hide w-[120px] h-[75%]  top-24 bg-white/90 border border-borderColor rounded-2xl z-[1]`
            : `right-0 ease-in-out translate-x-48 duration-500 flex flex-col justify-between items-center fixed overflow-auto scrollbar-hide w-[120px] h-[75%]  top-24 bg-white/90 border border-borderColor rounded-2xl`
        }
      >
        <div
          key={0}
          className="desktop:w-[100px] desktop:h-[100px] w-[50px] h-[50px] desktop:active:w-[90px] desktop:active:h-[90px] desktop:my-0 my-3 duration-200"
          onClick={handleAllClick}
        >
          <div className="w-full h-full rounded-full border border-borderColor/40 cursor-pointer flex flex-col justify-center items-center font-SCDream6 text-md text-white overflow-hidden">
            <Image
              width={100}
              height={100}
              src={"/images/all.png"}
              alt={"모두보기"}
              className="w-full h-full cursor-pointer"
            />
          </div>
        </div>
        {detailLangDict.map((el, idx) => {
          return (
            <div
              key={el.id}
              className="desktop:w-[100px] desktop:h-[100px] w-[50px] h-[50px] desktop:my-0 my-3"
              onClick={() => handleFilterClick(el.id)}
            >
              <Image
                key={el.id}
                width={100}
                height={100}
                src={el.img}
                alt={el.name}
                className="border border-borderColor/40 rounded-full w-full h-full cursor-pointer bg-white"
              />
            </div>
          );
        })}
      </div>
    </>
  );
};

export default MoblieLanguageFilter;
