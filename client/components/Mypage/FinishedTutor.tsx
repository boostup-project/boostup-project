import useGetTutorInfo from "hooks/mypage/useGetTutorInfo";
import useGetMyTutor from "hooks/mypage/useGetMyTutor";
import useDeleteFinishedTutor from "hooks/mypage/useDeleteFinishedTutor";
import { useState, useEffect } from "react";
import Swal from "sweetalert2";
const FinishedTutor = () => {
  const [islessonId, setIsLessonId] = useState(0);
  const { data: myTutorUrl } = useGetMyTutor();
  const lessonId = myTutorUrl?.data.lessonId;

  const { refetch: refetchFinishedClass, data: finishedClassData } =
    useGetTutorInfo(lessonId, 3);

  const { mutate: deleteTutor } = useDeleteFinishedTutor();
  const deleteFinishedList = (suggestId: number) => {
    Swal.fire({
      title: "과외 내역을 삭제하시겠습니까?",
      text: "학생의 수업내역에서도 해당 수업이 사라집니다.",
      icon: "question",
      showCancelButton: true,
      confirmButtonColor: "#3085d6",
    }).then(result => {
      if (result.isConfirmed) {
        deleteTutor(suggestId);
        return Swal.fire({
          text: "삭제가 완료되었습니다",
          icon: "success",
          confirmButtonColor: "#3085d6",
        });
      }
    });
  };

  return (
    <>
      <div className="flex flex-col w-full min-h-[300px] bg-bgColor">
        <div className="flex flex-col">
          {finishedClassData === undefined ||
          finishedClassData?.data.data.length === 0 ? (
            <div className="flex flex-col justify-center items-center w-full h-36 font-SCDream3 text-lg text-textColor mt-20">
              아직 종료된 과외가 없어요🙂
            </div>
          ) : null}
          {finishedClassData?.data.data.map((finished: any) => (
            <div className="flex flex-row w-full h-fit border border-borderColor rounded-xl mt-3 p-3 pl-5">
              <div className="flex flex-col w-[60%]">
                <div className="mb-3 ">{finished.name}</div>
                <div className="flex">
                  <div className="mr-3">희망요일</div>
                  <div> {finished.days}</div>
                </div>
                <div className="flex">
                  <div className="mr-3">희망언어</div>
                  <div> {finished.languages}</div>
                </div>
                <div className="flex">
                  <div className="mr-3">요청사항</div>
                  <div>{finished.requests}</div>
                </div>
              </div>
              <div className="flex flex-col w-[60%] justify-end items-end">
                <div className="text text-textColor font-SCDream6 m-2">
                  {finished.status}
                </div>
                <div className="text text-textColor">
                  {finished.endTime?.slice(0, 10)}
                </div>
                <button
                  className="text text-negativeMessage m-2"
                  onClick={() => deleteFinishedList(finished.suggestId)}
                >
                  삭제하기
                </button>
              </div>
            </div>
          ))}
        </div>
      </div>
    </>
  );
};
export default FinishedTutor;
