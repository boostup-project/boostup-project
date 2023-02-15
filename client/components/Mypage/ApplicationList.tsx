import useGetMyTutor from "hooks/mypage/useGetMyTutor";
import useGetTutorInfo from "hooks/mypage/useGetTutorInfo";
import { useRouter } from "next/router";
import AcceptModal from "./AcceptModal";
import DeclineModal from "./DeclineModal";
import Swal from "sweetalert2";

import { useState, useCallback, useEffect, useRef } from "react";
const ApplicationList = () => {
  const [openAccept, setOpenAccept] = useState<boolean>(false);
  const [openDecline, setOpenDecline] = useState<boolean>(false);

  const [islessonId, setIsLessonId] = useState(0);
  const { data: myTutorUrl, isSuccess } = useGetMyTutor();
  // const lessonId = myTutorUrl?.data.lessonId;

  const {
    refetch: refetchApplyInfo,
    data: applyInfoData,
    isSuccess: tutorInfoSuccess,
  } = useGetTutorInfo(islessonId, 1);

  useEffect(() => {
    if (isSuccess) {
      setIsLessonId(myTutorUrl?.data.lessonId);
    }
  }, [isSuccess]);

  useEffect(() => {
    if (islessonId) {
      refetchApplyInfo();
    }
  }, [islessonId]);

  const router = useRouter();
  const toMyTutor = () => {
    if (myTutorUrl) {
      router.push(`/lesson/${islessonId}`);
    } else {
      Swal.fire({
        title: "등록하신 과외가 없습니다",
        text: "과외를 등록해보세요",
        icon: "warning",
        confirmButtonColor: "#3085d6",
      });
    }
  };

  const [suggestId, setSuggestId] = useState(0);

  const onClickAcceptModal = useCallback(
    (suggestId: number) => {
      setSuggestId(suggestId);
      setOpenAccept(!openAccept);
    },
    [openAccept],
  );

  const openDeclineModal = useCallback(
    (suggestId: number) => {
      setSuggestId(suggestId);
      setOpenDecline(!openDecline);
    },
    [openDecline],
  );

  const toChat = () => {
    router.push("/chat/0");
  };
  return (
    <div className="flex flex-col w-full min-h-[300px] bg-bgColor">
      <button
        className="flex flex-col bg-pointColor text-white font-SCDream7 desktop:text-lg tablet:text-base text-sm rounded-xl items-start justify-center border border-borderColor w-full desktop:h-[50px] tablet:h-[43px] h-[38px] py-3 desktop:mt-5 tablet:mt-3 mt-2 pl-5"
        onClick={toMyTutor}
      >
        나의 과외로 이동하기
      </button>
      {applyInfoData === undefined || applyInfoData?.data.data.length === 0 ? (
        <div className="flex flex-col justify-center items-center w-full h-36 font-SCDream3 text-lg text-textColor mt-10">
          아직 신청내역이 없어요🙂
        </div>
      ) : null}
      {/* {map} 수업신청정보 */}

      {tutorInfoSuccess
        ? applyInfoData?.data.data.map((apply: any) => (
            <div className="flex flex-col" key={apply.name}>
              <div className="flex flex-row w-full h-fit bg-white border border-borderColor rounded-xl desktop:mt-5 tablet:mt-3 mt-2 p-3 pl-5">
                <div className="flex flex-col w-[60%] font-SCDream4">
                  <div className="flex mb-2">
                    <div className="mr-3">신청학생</div>
                    <div> {apply.name}</div>
                  </div>
                  <div className="flex mb-2">
                    <div className="mr-3">희망요일</div>
                    <div> {apply.days}</div>
                  </div>
                  <div className="flex mb-2">
                    <div className="mr-3">희망언어</div>
                    <div> {apply.languages}</div>
                  </div>
                  <div className="flex">
                    <div className="mr-3">요청사항</div>
                    <div> {apply.status}</div>
                  </div>
                </div>

                <div className="flex flex-col w-[60%] justify-center items-end">
                  <div className="text text-textColor font-SCDream6 mb-2">
                    {apply.status}
                  </div>
                  {apply.status === "결제 대기 중" ? (
                    <>
                      <button
                        className="text text-pointColor mt-4"
                        onClick={toChat}
                      >
                        채팅하기
                      </button>
                      <button
                        className="text text-negativeMessage mt-4"
                        onClick={e => openDeclineModal(apply.suggestId)}
                      >
                        거절하기
                      </button>
                    </>
                  ) : (
                    <>
                      <button
                        className="text text-pointColor mb-2"
                        onClick={e => onClickAcceptModal(apply.suggestId)}
                      >
                        수락하기
                      </button>
                      <button
                        className="text text-pointColor mb-2"
                        onClick={toChat}
                      >
                        채팅하기
                      </button>
                      <button
                        className="text text-negativeMessage mb-1"
                        onClick={e => openDeclineModal(apply.suggestId)}
                      >
                        거절하기
                      </button>
                    </>
                  )}
                </div>
              </div>
            </div>
          ))
        : null}
      {openAccept && (
        <AcceptModal
          onClickToggleModal={onClickAcceptModal}
          suggestId={suggestId}
        />
      )}

      {openDecline && (
        <DeclineModal
          onClickToggleModal={openDeclineModal}
          suggestId={suggestId}
          // openDeclineModal={openDeclineModal}
        ></DeclineModal>
      )}
    </div>
  );
};
export default ApplicationList;
