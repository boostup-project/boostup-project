import { IconPaper, IconPlace, IconRibbon, IconWon } from "assets/icon/";
import { NextPage, GetStaticProps } from "next";
import axios from "axios";
import { useEffect } from "react";
import useGetMainCard from "./useGetMainCard";
import { useQuery } from "react-query";
import getMainCard from "apis/card/getMainCard";
// import { dehydrate, QueryClient, useQuery } from "react-query";

// export async function getStaticProps() {
//   const queryClient = new QueryClient();

//   await queryClient.prefetchQuery("cards", getCards);

//   return {
//     props: {
//       dehydratedState: dehydrate(queryClient),
//     },
//   };
// }

interface CardData {
  lessonId: any;
  language: number[];
  title: string;
  cost: string;
  profileImage: string;
  name: string;
  company: string;
  career: string;
  address: [];
  bookmark: boolean;
}

const Card = ({}) => {
  const { isLoading, isError, data, error } = useQuery(
    "cards",
    () => getMainCard(),
    {
      enabled: true,
      onSuccess: data => {
        // 성공시 호출
        console.log(data.data.data);
      },
      onError: error => {
        console.log(error);
      },
      retry: 2,
    },
  );

  return (
    <div>
      {data.map(card => (
        <div className="h-fit m-1 desktop:w-[24%] tablet:w-[32%] w-[48%] ">
          <div className="flex flex-col w-full h-1/4 border border-borderColor rounded-lg">
            <div className="flex w-full h-full">
              <img
                className="w-full object-cover rounded-t-lg desktop:h-44 tablet:h-40 h-36"
                //src={card.profileImage}
              />
            </div>
            <div className="flex flex-col w-full h-2/3">
              <div className="flex flex-row whitespace-wrap">
                <div className="flex justify-center items-center px-1 py-0.5 ml-1 my-1 border rounded-xl bg-[#0A83F2] desktop:text-xs tablet:text-[10px] text-[6px]">
                  JavaScript
                </div>
              </div>
              <div className="flex justify-start items-start w-full h-fit font-SCDream5 desktop:text-xs tablet:text-[10px] text-[8px] text-textColor ml-2  my-1">
                Sky Jung
              </div>
              <div className="flex inline-block justify-start items-start w-full h-fit font-SCDream6  desktop:text-base tablet:text-sm text-xs text-textColor ml-2 mb-2 whitespace-wrap ">
                React Native
              </div>
              <div className="flex justify-start items-start w-full h-fit font-SCDream5 text-textColor ml-2 mb-2 desktop:text-xs tablet:text-[10px] text-[8px]">
                <div className="mr-1 desktop:w-4 tablet:w-3.5 w-3">
                  <IconRibbon />
                </div>
                Kakao
              </div>
              <div className="flex justify-start items-start w-full h-fit font-SCDream5 desktop:text-xs tablet:text-[11px] text-[8px] text-textColor ml-2  mb-2">
                <div className="mr-1 desktop:w-4 tablet:w-3.5 w-3">
                  <IconPaper />
                </div>
                3 year
              </div>
              <div className="flex justify-start items-start w-full h-fit font-SCDream5 desktop:text-xs tablet:text-[10px] text-[8px] text-textColor ml-2 desktop:my-1 mb-1">
                <div className="mr-1 desktop:w-3.5 tablet:w-2.5 w-2 ">
                  <IconPlace />
                </div>
                Jongro
              </div>
              <div className="flex justify-center items-start w-full h-fit font-SCDream7 desktop:text-base tablet:text-sm text-[12px] text-textColor ml-1 mb-2">
                <div className="desktop:mt-0.5 tablet:mt-0.5 desktop:w-4 tablet:w-3.5 w-3">
                  <IconWon />
                </div>
                <div className="ml-1">65,000/회</div>
              </div>
            </div>
          </div>
        </div>
      ))}
    </div>
  );
};
// export async function getStaticProps() {
//   const apiUrl: any = process.env.apiUrl;
//   const res = await axios.get(apiUrl);
//   const data = res.data;

//   return {
//     props: {
//       list: data,
//       name: process.env.name,
//     },
//   };
// }
export default Card;
