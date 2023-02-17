import useGetTutorInfo from "hooks/mypage/useGetTutorInfo";
import useGetMyTutor from "hooks/mypage/useGetMyTutor";
import useGetCloseClass from "hooks/mypage/useGetCloseClass";
import { useState, useEffect } from "react";
import Swal from "sweetalert2";
import { useRouter } from "next/router";
const ClassList = () => {
  const [aSuggestId, setASuggestId] = useState(0);

  const { data: myTutorUrl } = useGetMyTutor();
  const lessonId = Number(myTutorUrl?.data.lessonId);

  const { refetch: refetchTutorInfo, data: tutorInfoData } = useGetTutorInfo(
    lessonId,
    2,
  );
  const {
    refetch: classRefetch,
    isSuccess,
    isError,
  } = useGetCloseClass(aSuggestId);

  const closeClass = (suggestId: any) => {
    Swal.fire({
      title: "과외를 종료하시겠습니까?",
      icon: "question",
      showCancelButton: true,
      confirmButtonColor: "#3085d6",
    }).then(result => {
      if (result.isConfirmed) {
        setASuggestId(suggestId);
        Swal.fire({
          text: "과외종료가 완료되었습니다",
          icon: "success",
          confirmButtonColor: "#3085d6",
        });
      }
    });
  };
  useEffect(() => {
    if (aSuggestId !== 0) {
      classRefetch();
      refetchTutorInfo();
    }
  }, [aSuggestId]);

  useEffect(() => {
    if (isError) {
      Swal.fire({
        text: "다시 시도해주세요",
        icon: "warning",
        confirmButtonColor: "#3085d6",
      });
    }
  }, [isSuccess, isError]);

  const router = useRouter();
  const toChat = () => {
    router.push("/chat/0");
  };
  return (
    <>
      <div className="flex flex-col w-full min-h-[300px] bg-bgColor">
        <div className="flex flex-col">
          {tutorInfoData === undefined ||
          tutorInfoData?.data.data.length === 0 ? (
            <div className="flex flex-col justify-center items-center w-full h-36 font-SCDream3 text-lg text-textColor mt-20">
              아직 진행중인 과외가 없어요🙂
            </div>
          ) : null}
          {tutorInfoData?.data.data.map((curStu: any) => (
            <div className="flex flex-row w-full h-fit bg-white  border border-borderColor rounded-xl mt-3 p-3 pl-5">
              <div className="flex flex-col w-[60%]">
                <div className="flex mb-2 font-SCDream4">
                  <div className="mr-3">신청학생</div>
                  <div> {curStu.name}</div>
                </div>
                <div className="flex font-SCDream4">
                  <div className="mr-3 mb-2">희망요일</div>
                  <div>{curStu.days}</div>
                </div>
                <div className="flex font-SCDream4">
                  <div className="mr-3 mb-2">희망언어</div>
                  <div> {curStu.languages}</div>
                </div>
                <div className="flex font-SCDream4">
                  <div className="mr-3">요청사항</div>
                  <div> {curStu.requests}</div>
                </div>
              </div>
              <div className="flex flex-col w-[60%] justify-end items-end">
                <div className="text text-textColor font-SCDream6 mb-1 mr-2">
                  {curStu.status}
                </div>
                <div className="text text-textColor mb-1 mr-2">
                  {curStu.startTime?.slice(0, 10)}
                </div>
                <button className="text text-pointColor mr-2" onClick={toChat}>
                  채팅하기
                </button>
                <button
                  className="text text-negativeMessage mt-2 mr-2"
                  onClick={() => {
                    closeClass(curStu.suggestId);
                  }}
                >
                  과외종료
                </button>
              </div>
            </div>
          ))}
        </div>
      </div>
    </>
  );
};
export default ClassList;
