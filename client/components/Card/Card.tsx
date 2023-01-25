import {
  IconPaper,
  IconPlace,
  IconRibbon,
  IconWon,
  IconFullheart,
  IconEmptyheart,
} from "assets/icon/";
import { NextPage, GetStaticProps } from "next";
import { useEffect, useState } from "react";
//import useGetMainCard from "./useGetMainCard";
import getMainCard from "apis/card/getMainCard";
import { dehydrate, QueryClient, useQuery } from "react-query";
import axios from "axios";

export async function getStaticProps() {
  const queryClient = new QueryClient();

  await queryClient.prefetchQuery("cards", () => getMainCard());

  return {
    props: {
      dehydratedState: dehydrate(queryClient),
    },
  };
}
const Card = () => {
  const [cards, setCards] = useState([]);

  const { isLoading, isError, data } = useQuery("cards", () => getMainCard(), {
    enabled: true,
    onSuccess: data => {
      // 성공시 호출
      console.log(data.data.data.bookmark);
      setCards(data.data.data);
    },
    onError: error => {
      console.log(error);
    },
    retry: 2,
  });

  return (
    <div className="flex flex-row flex-wrap w-full">
      {cards?.map((card: any) => (
        <div className="h-fit m-1 desktop:w-[24%] tablet:w-[32%] w-[48%] ">
          <div className="flex flex-col w-full h-1/4 border border-borderColor rounded-lg">
            <div className="relative">
              <div className="flex relative w-full h-full">
                <img
                  className="flex w-full object-cover rounded-t-lg desktop:h-44 tablet:h-40 h-36"
                  src={card.profileImage}
                />
                <div className="flex w-1/6 h-1/6 absolute top-2 right-px">
                  {card.bookmark ? <IconFullheart /> : <IconEmptyheart />}
                </div>
              </div>
            </div>
            <div className="flex flex-col w-full h-2/3">
              <div className="flex flex-row whitespace-wrap">
                <div className="flex">
                  {card.languages.map((el: any) => {
                    return (
                      <div
                        className={`flex justify-center bg-${el} items-center px-1 py-0.5 ml-1 mt-1 border rounded-xl desktop:text-xs tablet:text-[10px] text-[6px]`}
                      >
                        {el}
                      </div>
                    );
                  })}
                </div>
              </div>
              <div className="flex justify-start items-start w-full h-fit font-SCDream5 desktop:text-xs tablet:text-[10px] text-[8px] text-textColor ml-2  my-1">
                {card.name}
              </div>
              <div className="flex inline-block justify-start items-start w-full h-fit font-SCDream6  desktop:text-base tablet:text-sm text-xs text-textColor ml-2 mb-2 whitespace-wrap ">
                {card.title}
              </div>
              <div className="flex justify-start items-start w-full h-fit font-SCDream5 text-textColor ml-2 mb-2 desktop:text-xs tablet:text-[10px] text-[8px]">
                <div className="mr-1 desktop:w-4 tablet:w-3.5 w-3">
                  <IconRibbon />
                </div>
                {card.company}
              </div>
              <div className="flex justify-start items-start w-full h-fit font-SCDream5 desktop:text-xs tablet:text-[11px] text-[8px] text-textColor ml-2  mb-2">
                <div className="mr-1 desktop:w-4 tablet:w-3.5 w-3">
                  <IconPaper />
                </div>

                {card.career}
              </div>
              <div className="flex justify-start items-start w-full h-fit font-SCDream5 desktop:text-xs tablet:text-[10px] text-[8px] text-textColor ml-2 desktop:my-1 mb-1">
                <div className="mr-1 desktop:w-3.5 tablet:w-2.5 w-2 ">
                  <IconPlace />
                </div>
                {card.address.map((el: any) => {
                  return <div className="ml-1">{el}</div>;
                })}
              </div>
              <div className="flex justify-center items-start w-full h-fit font-SCDream7 desktop:text-base tablet:text-sm text-[12px] text-textColor ml-1 mb-2">
                <div className="desktop:mt-0.5 tablet:mt-0.5 desktop:w-4 tablet:w-3.5 w-3">
                  <IconWon />
                </div>
                <div className="ml-1">{card.cost}/회</div>
              </div>
            </div>
          </div>
        </div>
      ))}
    </div>
  );
};

export default Card;
