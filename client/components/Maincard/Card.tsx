import {
  IconPaper,
  IconPlace,
  IconRibbon,
  IconWon,
  IconFullheart,
  IconEmptyheart,
} from "assets/icon/";

import { useEffect, useState } from "react";
import Link from "next/link";
// import useGetMainCard from "./useGetMainCard";
import getMainCard from "apis/card/getMainCard";
import {
  QueryClient,
  QueryClientProvider,
  useQuery,
  useQueryClient,
} from "@tanstack/react-query";
import useGetBookmarkModi from "hooks/detail/useGetBookmarkModi";
import useGetBookmark from "hooks/detail/useGetBookmark";
import Swal from "sweetalert2";
import { mainCardInfo } from "atoms/main/mainAtom";
import { useRouter } from "next/router";
import { useSetRecoilState, useRecoilState, useRecoilValue } from "recoil";
import { refetchBookmark } from "atoms/detail/detailAtom";
const client = new QueryClient();

const Card = () => {
  const router = useRouter();
  const [cards, setMainCardInfo] = useRecoilState(mainCardInfo);

  const queryClient = useQueryClient();
  const { refetch: cardRefetch, data: cardData } = useQuery(
    ["cards"],
    getMainCard,
    {
      enabled: true,
      onSuccess: data => {
        setMainCardInfo(data.data.data);
      },
      retry: 2,
    },
  );
  const toggle = useRecoilValue(refetchBookmark);
  const [lessonId, setLessonId] = useState(0);
  const [mark, setMark] = useState<boolean>(false);

  const { refetch: bookmarkRefetch, data: bookmarkModiData } =
    useGetBookmarkModi(lessonId);
  const { data: bookmarkData, refetch: getBookmark } = useGetBookmark(lessonId);

  useEffect(() => {
    cardRefetch();
  }, [toggle]);

  useEffect(() => {
    setMainCardInfo(cardData?.data.data);
  }, [cardData]);

  useEffect(() => {
    if (lessonId !== 0) {
      bookmarkRefetch();
    }
  }, [lessonId]);

  const handleLike = (lessonId: any) => {
    if (localStorage.getItem("token")) {
      setLessonId(lessonId);
    } else {
      return Swal.fire({
        text: "로그인 후 원하는 선생님을 찜 해보세요",
        icon: "warning",
        confirmButtonColor: "#3085d6",
      });
    }
  };
  return (
    <QueryClientProvider client={client}>
      <div className="flex flex-row flex-wrap w-full">
        {cards?.map((card: any) => (
          <div
            key={card.lessonId}
            className="h-fit m-1 desktop:w-[24%] tablet:w-[32%] w-[47%] rounded-lg"
          >
            <div className="flex flex-col w-full h-1/4 border border-borderColor rounded-lg">
              <div className="relative">
                <div className="flex relative w-full h-full">
                  <img
                    className="flex w-full object-cover rounded-t-lg desktop:h-44 tablet:h-40 h-36"
                    src={card.profileImage}
                  />
                  <button
                    className="flex decktop:w-[15%] tablet:w-[28px] w-[25px] absolute top-2 right-1"
                    onClick={() => {
                      handleLike(card.lessonId);
                    }}
                  >
                    {card.bookmark ? <IconFullheart /> : <IconEmptyheart />}
                  </button>
                </div>
              </div>
              <Link href={`/lesson/${card.lessonId}`}>
                <div className="flex flex-col w-full h-2/3">
                  <div className="flex flex-row whitespace-wrap">
                    <div className="flex">
                      {card.languages?.map((el: any, idx: any) => {
                        return (
                          <div
                            key={idx}
                            className={`flex justify-center bg-${el} items-center px-1 py-0.5 ml-1 mt-1 border font-SCDream5 text-white rounded-xl desktop:text-xs tablet:text-[10px] text-[6px]`}
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
                  <div className="flex justify-start items-start w-full h-fit font-SCDream6 desktop:text-base tablet:text-sm text-xs text-textColor ml-1 mb-2 flex-wrap">
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
                    {card.career}년
                  </div>
                  <div className="flex justify-start items-start w-full h-fit font-SCDream5 desktop:text-xs tablet:text-[10px] text-[8px] text-textColor ml-2 desktop:my-1 mb-1">
                    <div className="mr-1 desktop:w-3.5 tablet:w-2.5 w-2 ">
                      <IconPlace />
                    </div>
                    {card.address?.map((el: any, idx: any) => {
                      return (
                        <div className="ml-1" key={idx}>
                          {el}
                        </div>
                      );
                    })}
                  </div>
                  <div className="flex justify-center items-start w-full h-fit font-SCDream7 desktop:text-base tablet:text-sm text-[12px] text-moneyGrayColor ml-1 mb-2 font-bold">
                    {/* <div className="desktop:mt-0.5 tablet:mt-0.5 desktop:w-4 tablet:w-3.5 w-3">
                      <IconWon />
                    </div> */}
                    <div className="ml-1">
                      ₩ {card.cost.toLocaleString("ko-KR")}원/회
                    </div>
                  </div>
                </div>
              </Link>
            </div>
          </div>
        ))}
      </div>
    </QueryClientProvider>
  );
};

export default Card;
