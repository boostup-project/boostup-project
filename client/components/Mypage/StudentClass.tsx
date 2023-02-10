import { IconRibbon, IconWon, IconPaper, IconPlace } from "assets/icon";
import useGetStudentInfo from "hooks/mypage/useGetStudentInfo";
import useDeleteApply from "hooks/mypage/useDeleteApply";
import useDeleteFinishedClass from "hooks/mypage/useDeleteFinishedClass";
import Swal from "sweetalert2";
import Link from "next/link";
import { useRouter } from "next/router";
import { useState, useCallback, useEffect } from "react";
import ReviewModal from "./ReviewModal";
import useGetCreateRoom from "hooks/chat/useGetCreateRoom";
const StudentClass = () => {
  const router = useRouter();

  const { refetch: refetchStudentInfo, data: studentInfoData } =
    useGetStudentInfo();
  const [openReview, setOpenReview] = useState<boolean>(false);
  const [openAccept, setOpenAccept] = useState<boolean>(false);
  const [chat, setChat] = useState(false);

  const { mutate: deleteApplication, isSuccess: deleteThisApply } =
    useDeleteApply();
  const deleteApply = (suggestId: number) => {
    Swal.fire({
      title: "신청을 취소하시겠습니까?",
      icon: "question",
      showCancelButton: true,
      confirmButtonColor: "#3085d6",
    }).then(result => {
      if (result.isConfirmed) {
        deleteApplication(suggestId);
        refetchStudentInfo();
        return Swal.fire({
          text: "삭제가 완료되었습니다",
          icon: "success",
          confirmButtonColor: "#3085d6",
        });
      }
    });
  };

  const { mutate: deleteClass, isSuccess, isError } = useDeleteFinishedClass();
  const deleteFinishedClass = (suggestId: number) => {
    Swal.fire({
      title: "과외 내역을 삭제하시겠습니까?",
      text: "선생님의 수업내역에서도 해당 수업이 사라집니다.",
      icon: "question",
      showCancelButton: true,
      confirmButtonColor: "#3085d6",
    }).then(result => {
      if (result.isConfirmed) {
        deleteClass(suggestId);
        refetchStudentInfo();
      }
    });
  };

  useEffect(() => {
    if (isSuccess) {
      Swal.fire({
        text: "삭제가 완료되었습니다",
        icon: "success",
        confirmButtonColor: "#3085d6",
      });
    }
    if (isError) {
      Swal.fire({
        text: "다시 삭제해주세요",
        icon: "warning",
        confirmButtonColor: "#3085d6",
      });
    }
  }, [isSuccess, isError]);

  const toPayment = (suggestId: number) => {
    router.push(`/shop/${suggestId}`);
  };
  const toReceipt = (suggestId: number) => {
    router.push(`/shop/${suggestId}/receipt`);
  };
  const [suggestId, setSuggestId] = useState(0);
  const [lessonId, setLessonId] = useState(0);

  const onClickAcceptModal = useCallback(
    (suggestId: number, lessonId: number) => {
      setSuggestId(suggestId);
      setLessonId(lessonId);
      setOpenReview(prev => !prev);
    },
    [openAccept],
  );

  const openReviewModal = () => {
    setOpenReview(prev => !prev);
  };
  const { refetch: createChatRoomRefetch } = useGetCreateRoom(lessonId);
  const chatNow = (lessonId: number) => {
    setChat(true);
    setLessonId(lessonId);
    if (chat && lessonId !== 0) {
      setChat(false);
      createChatRoomRefetch();
    }
  };
  return (
    <>
      <div className="flex flex-col w-full min-h-[300px] bg-bgColor">
        <div className="w-full">
          {studentInfoData === undefined ||
          studentInfoData?.data.data.length === 0 ? (
            <div className="flex flex-col justify-center items-center w-full h-36 font-SCDream3 text-lg text-textColor mt-20">
              아직 수강중인 과외가 없어요🙂
            </div>
          ) : null}
          {studentInfoData?.data.data.map((tutor: any) => (
            <div
              key={tutor.lessonId}
              className="flex flex-row h-fit w-full rounded-lg border border-borderColor mt-3"
            >
              {/* {Left} */}
              <Link
                href={`/lesson/${tutor.lessonId}`}
                className="flex desktop:w-1/4 justify-center items-center "
              >
                {tutor.profileImage ? (
                  <img
                    className="flex desktop:w-[200px] tablet:w-[150px] w-[100px] desktop:h-[200px] tablet:h-[150px] h-[100px] object-cover border border-borderColor rounded-xl m-3"
                    src={tutor.profileImage}
                  />
                ) : (
                  <img
                    className="flex desktop:w-[200px] tablet:w-[150px] w-[100px] desktop:h-[200px] tablet:h-[150px] h-[100px] object-cover border border-borderColor rounded-xl m-3"
                    src={
                      "https://play-lh.googleusercontent.com/38AGKCqmbjZ9OuWx4YjssAz3Y0DTWbiM5HB0ove1pNBq_o9mtWfGszjZNxZdwt_vgHo=w240-h480-rw"
                    }
                  />
                )}
              </Link>
              {/* {center} */}
              <div className="flex flex-col w-[45%] justify-center desktop:pl-2 mt-2">
                <div className="flex">
                  {tutor.languages?.map((el: any, idx: any) => {
                    return (
                      <div
                        key={idx}
                        className={`flex justify-center bg-${el} items-center px-1 py-0.5 ml-1 mt-1 font-SCDream5 text-white border rounded-xl desktop:text-xs tablet:text-[10px] text-[6px]`}
                      >
                        {el}
                      </div>
                    );
                  })}
                </div>
                <div className="flex justify-start items-start w-full h-fit font-SCDream5 desktop:text-xs tablet:text-[12px] text-[8px] text-textColor ml-2  my-1">
                  {tutor.name}
                </div>
                <div className="flex justify-start items-start w-full h-fit font-SCDream6 desktop:text-lg tablet:text-base text-xs text-textColor ml-1 mb-2 flex-wrap">
                  {tutor.title}
                </div>
                <div className="flex justify-start items-start w-full h-fit font-SCDream5 text-textColor ml-2 mb-2 desktop:text-sm tablet:text-sm text-[12px]">
                  <div className="mr-1 desktop:w-5 tablet:w-3.5 w-3.5">
                    <IconRibbon />
                  </div>
                  {tutor.company}
                </div>
                <div className="flex justify-start items-start w-full h-fit font-SCDream5 desktop:text-sm tablet:text-sm text-[12px] text-textColor ml-2  mb-2">
                  <div className="mr-1 desktop:w-5 tablet:w-3.5 w-3.5">
                    <IconPaper />
                  </div>
                  {tutor.career}년
                </div>
                <div className="flex justify-start items-start w-full h-fit font-SCDream5 desktop:text-sm tablet:text-sm text-[12px] text-textColor ml-2 desktop:my-1 mb-1">
                  <div className="mr-1 desktop:w-4 tablet:w-3 w-2.5 ">
                    <IconPlace />
                  </div>
                  {tutor.address?.map((el: any, idx: any) => {
                    return (
                      <div className="ml-1" key={idx}>
                        {el}
                      </div>
                    );
                  })}
                </div>
              </div>

              {/* {Right} */}
              <div className="flex flex-col w-[40%] justify-center items-end desktop:mr-4 tablet:mr-2 mr-2">
                <div className="flex  desktop:text-xl tablet:text-lg text-[14px] text-moneyGrayColor font-SCDream5">
                  ₩ {tutor.cost.toLocaleString("ko-KR")}원/회
                </div>
                <div className="flex flex-col font-SCDream5 mt-5 tablet:mt-3 desktop:text-sm tablet:text-xs text-[8px] items-center">
                  {tutor.status}
                  <div className="text text-borderColor">
                    {tutor.startTime?.slice(0, 10)}
                  </div>
                  {tutor.status === "결제 대기 중" ? (
                    <button
                      className="text text-pointColor m-2 desktop:text-base tablet:text-sm text-[10px]"
                      onClick={() => toPayment(tutor.suggestId)}
                    >
                      결제하기
                    </button>
                  ) : tutor.status === "과외 중" ||
                    tutor.status === "과외 종료" ? (
                    <button
                      className="text text-pointColor m-2 desktop:text-base tablet:text-sm text-[10px]"
                      onClick={() => toReceipt(tutor.suggestId)}
                    >
                      영수증 보기
                    </button>
                  ) : (
                    <></>
                  )}
                </div>

                <div className="flex">
                  {tutor.status === "수락 대기 중" ||
                  tutor.status === "결제 대기 중" ? (
                    <>
                      <button
                        className="text text-pointColor font-SCDream3 m-2 desktop:text-base tablet:text-sm text-[10px]"
                        onClick={() => chatNow(tutor.lessonId)}
                      >
                        채팅하기
                      </button>
                      <button
                        className="text text-negativeMessage font-SCDream3 m-2 desktop:text-base tablet:text-sm text-[10px]"
                        onClick={() => deleteApply(tutor.suggestId)}
                      >
                        신청취소
                      </button>
                    </>
                  ) : tutor.status === "과외 중" ? (
                    <button
                      className="text text-pointColor font-SCDream3 m-2 desktop:text-base tablet:text-sm text-[10px]"
                      onClick={() => chatNow(tutor.lessonId)}
                    >
                      채팅하기
                    </button>
                  ) : tutor.status === "과외 종료" &&
                    tutor.reviewCheck === false ? (
                    <>
                      <button
                        className="text text-pointColor font-SCDream3 m-2 desktop:text-base tablet:text-sm text-[10px]"
                        onClick={e =>
                          onClickAcceptModal(tutor.suggestId, tutor.lessonId)
                        }
                      >
                        후기작성
                      </button>
                      <button
                        className="text text-negativeMessage m-2 font-SCDream3 desktop:text-base tablet:text-sm text-[10px]"
                        onClick={() => deleteFinishedClass(tutor.suggestId)}
                      >
                        삭제하기
                      </button>
                    </>
                  ) : tutor.status === "과외 종료" &&
                    tutor.reviewCheck === true ? (
                    <button
                      className="text text-negativeMessage m-2 font-SCDream3 desktop:text-base tablet:text-sm text-[10px]"
                      onClick={() => deleteFinishedClass(tutor.suggestId)}
                    >
                      삭제하기
                    </button>
                  ) : (
                    <></>
                  )}
                </div>
              </div>
            </div>
          ))}
          {openReview && (
            <ReviewModal
              onClickToggleModal={onClickAcceptModal}
              suggestId={suggestId}
              lessonId={lessonId}
              openDeclineModal={openReviewModal}
            ></ReviewModal>
          )}
        </div>
      </div>
    </>
  );
};
export default StudentClass;
