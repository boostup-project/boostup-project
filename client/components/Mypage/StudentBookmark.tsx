import {
  IconRibbon,
  IconWon,
  IconPaper,
  IconPlace,
  IconEmptyheart,
  IconFullheart,
} from "assets/icon";
import useGetAllBookmark from "hooks/mypage/useGetAllBookmark";
import { useEffect, useState } from "react";
import Link from "next/link";
import useGetBookmarkModi from "hooks/detail/useGetBookmarkModi";
import { useRecoilValue } from "recoil";
import { refetchBookmark } from "atoms/detail/detailAtom";

const StudentBookmark = () => {
  const [lessonId, setLessonId] = useState(0);
  const { refetch: bookmarkRefetch } = useGetBookmarkModi(lessonId);
  const { data: studentBookmark, refetch: allBookmark } = useGetAllBookmark();
  const [bookmarkData, setBookmarkData] = useState<any>();
  const toggle = useRecoilValue(refetchBookmark);

  useEffect(() => {
    allBookmark();
  }, [toggle]);

  useEffect(() => {
    setBookmarkData(studentBookmark?.data.data);
  }, [studentBookmark]);

  useEffect(() => {
    if (lessonId !== 0) {
      bookmarkRefetch();
    }
  }, [lessonId]);
  const saveBookmark = (lessonId: any) => {
    setLessonId(lessonId);
  };
  return (
    <>
      <div className="mt-6 flex flex-col w-full font-SCDream4">
        <div className="w-full">
          {bookmarkData?.map((bookmark: any) => (
            <div className="flex flex-row h-fit w-full rounded-lg border border-borderColor mt-3">
              {/* {Left} */}
              <Link
                href={`/lesson/${bookmark.lessonId}`}
                className="flex desktop:w-1/4 justify-center items-center "
              >
                <img
                  className="flex desktop:w-[200px] tablet:w-[150px] w-[100px] desktop:h-[200px] tablet:h-[150px] h-[100px] object-cover border border-borderColor rounded-xl m-3"
                  src={bookmark.image}
                ></img>
              </Link>

              {/* {center} */}
              <div className="flex flex-col w-[45%]  justify-center desktop:pl-2 mt-2">
                <div className="flex">
                  {bookmark.languages?.map((el: any, idx: any) => {
                    return (
                      <div
                        key={idx}
                        className={`flex justify-center bg-${el} items-center px-1 py-0.5 ml-1 mt-1 border rounded-xl desktop:text-xs tablet:text-[10px] text-[6px]`}
                      >
                        {el}
                      </div>
                    );
                  })}
                </div>
                <div className="flex justify-start items-start w-full h-fit font-SCDream5 desktop:text-xs tablet:text-[12px] text-[8px] text-textColor ml-2  my-1">
                  {bookmark.name}
                </div>
                <div className="flex justify-start items-start w-full h-fit font-SCDream6 desktop:text-lg tablet:text-base text-xs text-textColor ml-1 mb-2 flex-wrap">
                  {bookmark.title}
                </div>
                <div className="flex justify-start items-start w-full h-fit font-SCDream5 text-textColor ml-2 mb-2 desktop:text-sm tablet:text-sm text-[12px]">
                  <div className="mr-1 desktop:w-5 tablet:w-3.5 w-3.5">
                    <IconRibbon />
                  </div>
                  {bookmark.company}
                </div>
                <div className="flex justify-start items-start w-full h-fit font-SCDream5 desktop:text-sm tablet:text-sm text-[12px] text-textColor ml-2  mb-2">
                  <div className="mr-1 desktop:w-5 tablet:w-3.5 w-3.5">
                    <IconPaper />
                  </div>
                  {bookmark.career}년
                </div>
                <div className="flex justify-start items-start w-full h-fit font-SCDream5 desktop:text-sm tablet:text-sm text-[12px] text-textColor ml-2 desktop:my-1 mb-1">
                  <div className="mr-1 desktop:w-4 tablet:w-3 w-2.5 ">
                    <IconPlace />
                  </div>
                  {bookmark.address?.map((el: any, idx: any) => {
                    return (
                      <div className="ml-1" key={idx}>
                        {el}
                      </div>
                    );
                  })}
                </div>
              </div>
              <div className="flex flex-col w-[40%] justify-center items-end desktop:mr-4 tablet:mr-2 mr-2">
                <div className="flex  desktop:text-xl tablet:text-lg text-[12px]">
                  <div className="desktop:mt-1 tablet:mt-2 mt-1 mr-px desktop:w-5 tablet:w-3.5 w-2.5">
                    <IconWon />
                  </div>
                  {bookmark.cost}원/회
                </div>
                <button
                  className="decktop:w-[30px] tablet:w-[28px] w-[25px] my-6"
                  onClick={() => saveBookmark(bookmark.lessonId)}
                >
                  {true ? <IconFullheart /> : <IconEmptyheart />}
                </button>
                <div className="flex">
                  <button className="text text-pointColor font-SCDream3 m-2 desktop:text-base tablet:text-sm text-[10px]">
                    채팅하기
                  </button>
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>
    </>
  );
};
export default StudentBookmark;
