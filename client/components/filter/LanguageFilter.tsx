import { detailLangDict } from "../reuse/dict";
import Image from "next/image";
import useGetFilteredBoard from "hooks/board/useGetFilteredBoard";
import { useEffect, useState } from "react";
import { useQuery, useQueryClient } from "@tanstack/react-query";
import getMainCard from "apis/card/getMainCard";
import { useRecoilState } from "recoil";
import { mainCardInfo } from "atoms/main/mainAtom";

const LanguageFilter = () => {
  const [langId, setLangId] = useState<number>(0);
  const { refetch } = useGetFilteredBoard(langId);
  const queryClient = useQueryClient();
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

  const handleFilterClick = (languageId: number) => {
    setLangId(languageId);
  };

  const handleAllClick = () => {
    cardRefetch();
  };

  return (
    <>
      <div className="desktop:flex hidden w-3/4 min-w-[1000px] min-h-[100px] desktop-h-[100px] h-fit mt-7 rounded-xl flex-row justify-between items-center">
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
        {detailLangDict.map(lang => {
          return (
            <div
              key={lang.id}
              className="desktop:w-[100px] desktop:h-[100px] w-[50px] h-[50px] desktop:active:w-[90px] desktop:active:h-[90px] desktop:my-0 my-3 duration-200"
              onClick={() => handleFilterClick(lang.id)}
            >
              <Image
                width={100}
                height={100}
                src={lang.img}
                alt={lang.name}
                className="border border-borderColor/40 rounded-full w-full h-full cursor-pointer bg-white"
              />
            </div>
          );
        })}
      </div>
    </>
  );
};

export default LanguageFilter;
