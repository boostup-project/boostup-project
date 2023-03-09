import {
  IconPaper,
  IconPlace,
  IconRibbon,
  IconFullheart,
  IconEmptyheart,
} from "assets/icon/";
import { useEffect, useState } from "react";
import Link from "next/link";
import getMainCard from "apis/card/getMainCard";
import {
  QueryClient,
  QueryClientProvider,
  useQuery,
  useQueryClient,
} from "@tanstack/react-query";
import useGetBookmarkModi from "hooks/detail/useGetBookmarkModi";
import Swal from "sweetalert2";
import { mainCardInfo } from "atoms/main/mainAtom";
import { useRecoilState, useRecoilValue } from "recoil";
import { refetchBookmark } from "atoms/detail/detailAtom";
import Pagination from "./Pagination";
import { totalCard } from "atoms/main/mainAtom";
import { useRouter } from "next/router";
const client = new QueryClient();

const Card = ({ data }: any) => {
  const router = useRouter();
  const [cards, setMainCardInfo] = useRecoilState(mainCardInfo);
  //pagination
  const limit = 12;
  const [page, setPage] = useState(1);
  const offset = (page - 1) * limit;
  const [doLike, setDoLike] = useState(false);
  const [totalCards, setTotalCards] = useRecoilState(totalCard);
  const queryClient = useQueryClient();

  const {
    refetch: cardRefetch,
    data: cardData,
    isSuccess,
  } = useQuery(["cards"], getMainCard, {
    enabled: false,
    onSuccess: data => {
      setMainCardInfo(data.data.data);
      setTotalCards(data.data.data.length);
    },
    retry: 2,
  });
  const toggle = useRecoilValue(refetchBookmark);
  const [lessonId, setLessonId] = useState(0);

  const { refetch: bookmarkRefetch, data: bookmarkModiData } =
    useGetBookmarkModi(lessonId);

  useEffect(() => {
    if (lessonId !== 0) {
      bookmarkRefetch();
    }
  }, [lessonId, doLike]);

  useEffect(() => {
    if (localStorage.getItem("token")) {
      cardRefetch();
    } else {
      data.then((res: any) => setMainCardInfo(res));
    }
  }, [toggle]);

  useEffect(() => {
    setMainCardInfo(cardData?.data.data);
  }, [cardData]);

  const handleLike = (lessonId: any) => {
    if (localStorage.getItem("token")) {
      setLessonId(lessonId);
      setDoLike(prev => !prev);
    } else {
      return Swal.fire({
        title: "로그인 하시겠습니까?",
        text: "로그인이 필요한 서비스입니다.",
        icon: "question",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
      }).then(result => {
        if (result.isConfirmed) {
          router.push("/login");
        }
      });
    }
  };
  return (
    <QueryClientProvider client={client}>
      <div className="flex flex-row flex-wrap w-full justify-start desktop:ml-0 ml-1">
        {cards?.slice(offset, offset + limit).map((card: any) => (
          <div
            key={card.lessonId}
            className="h-fit m-1 desktop:w-[24%] tablet:w-[32%] w-[47%] rounded-lg"
          >
            <div className="flex flex-col w-full h-1/4 border border-borderColor rounded-lg">
              <div className="relative z-0">
                <div className="flex relative w-full h-full">
                  <Link
                    href={`/lesson/${card.lessonId}`}
                    className="flex relative w-full h-full"
                  >
                    <img
                      className="flex w-full object-cover rounded-t-lg desktop:h-44 tablet:h-40 h-36"
                      src={card.profileImage}
                      alt="profileImage"
                    />
                  </Link>
                  <button
                    className="flex decktop:w-[15%] tablet:w-[28px] w-[25px] absolute top-2 right-1 z-30"
                    onClick={() => {
                      handleLike(card.lessonId);
                    }}
                  >
                    {card.bookmark ? <IconFullheart /> : <IconEmptyheart />}
                  </button>
                </div>
              </div>
              <div>
                <Link href={`/lesson/${card.lessonId}`}>
                  <div className="flex flex-col w-full h-2/3 bg-white rounded-xl pt-2">
                    <div className="flex flex-row whitespace-wrap">
                      <div className="flex mb-1">
                        {card.languages?.map((el: any, idx: any) => {
                          return (
                            <div
                              key={idx}
                              className={`flex justify-center bg-${el} items-center px-1.5 py-1 ml-1.5 mt-1 font-SCDream5 text-white rounded-xl desktop:text-xs tablet:text-[10px] text-[6px]`}
                            >
                              {el}
                            </div>
                          );
                        })}
                      </div>
                    </div>
                    <div className="flex justify-start items-start w-full h-fit font-SCDream5 desktop:text-xs tablet:text-[10px] text-[8px] text-textColor/80 ml-2  my-1">
                      {card.name}
                    </div>
                    <div className="flex justify-start items-start w-full h-fit font-SCDream6 desktop:text-base tablet:text-sm text-xs text-textColor ml-2 mb-2 flex-wrap">
                      {card.title.length > 12
                        ? `${card.title.slice(0, 12)}...`
                        : card.title}
                    </div>
                    <div className="flex justify-start items-start w-full h-fit font-SCDream5 text-textColor ml-2 mb-3 desktop:text-xs tablet:text-[10px] text-[8px]">
                      <div className="mr-2 desktop:w-4 tablet:w-3.5 w-3">
                        <IconRibbon />
                      </div>
                      {card.company.length > 19
                        ? `${card.company.slice(0, 19)}...`
                        : card.company}
                    </div>
                    <div className="flex justify-start items-start w-full h-fit font-SCDream5 desktop:text-xs tablet:text-[11px] text-[8px] text-textColor ml-2  mb-2.5">
                      <div className="mr-2 desktop:w-4 tablet:w-3.5 w-3">
                        <IconPaper />
                      </div>
                      {card.career}년
                    </div>
                    <div className="flex justify-start items-start w-full h-fit font-SCDream5 desktop:text-xs tablet:text-[10px] text-[8px] text-textColor ml-2 desktop:my-1 mb-2">
                      <div className="mr-2 desktop:w-3.5 tablet:w-2.5 w-2 ">
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
                    <div className="flex justify-center items-start w-full h-fit font-SCDream7 desktop:text-base tablet:text-sm text-[12px] text-moneyGrayColor my-2 font-bold">
                      ₩ {card.cost.toLocaleString("ko-KR")}원/회
                    </div>
                  </div>
                </Link>
              </div>
            </div>
          </div>
        ))}
      </div>
      <>
        {isSuccess ? (
          <Pagination
            // total={cardData?.data.data.length}
            limit={limit}
            page={page}
            setPage={setPage}
          />
        ) : null}
      </>
    </QueryClientProvider>
  );
};

export default Card;
