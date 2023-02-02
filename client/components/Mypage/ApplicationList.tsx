import useGetMyTutor from "hooks/mypage/useGetMyTutor";
import useGetTutorInfo from "hooks/mypage/useGetTutorInfo";
import { useRouter } from "next/router";
import AcceptModal from "./AcceptModal";
import Swal from "sweetalert2";
import DeclineModal from "./DeclineModal";
import { useState, useCallback, useEffect } from "react";
const ApplicationList = () => {
  const [OpenModal, setOpenModal] = useState<boolean>(false);

  const [islessonId, setIsLessonId] = useState(0);
  const { data: myTutorUrl } = useGetMyTutor();
  const lessonId = Number(myTutorUrl?.data.lessonUrl.slice(29));

  const { refetch: refetchTutorInfo, data: tutorInfoData } = useGetTutorInfo(
    lessonId,
    1,
  );

  useEffect(() => {
    setIsLessonId(Number(myTutorUrl?.data.lessonUrl.slice(29)));
  }, [myTutorUrl]);
  useEffect(() => {
    if (islessonId) {
      refetchTutorInfo();
    }
  }, [islessonId]);

  const router = useRouter();
  const toMyTutor = () => {
    if (myTutorUrl) {
      router.push(myTutorUrl?.data.lessonUrl);
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
      setOpenModal(!OpenModal);
    },
    [OpenModal],
  );

  return (
    <div className="flex flex-col w-full">
      <button
        className="flex flex-col bg-pointColor text-white font-SCDream7 desktop:text-lg tablet:text-base text-sm rounded-xl items-start justify-center border border-borderColor w-full desktop:h-[50px] tablet:h-[43px] h-[38px] py-3 desktop:mt-5 tablet:mt-3 mt-2 pl-5"
        onClick={toMyTutor}
      >
        나의 과외로 이동하기
      </button>
      {/* {map} 수업신청정보 */}
      {tutorInfoData?.data.data.map((apply: any) => (
        <div className="flex flex-col">
          <div className="flex flex-row w-full h-fit border border-borderColor rounded-xl desktop:mt-5 tablet:mt-3 mt-2 p-3 pl-5">
            <div className="flex flex-col w-[43%]">
              <div className="mb-3 ">{apply.name}</div>
              <div className="flex">
                <div className="mr-3">희망요일</div>
                <div> {apply.days}</div>
              </div>
              <div className="flex">
                <div className="mr-3">희망언어</div>
                <div> {apply.languages}</div>
              </div>
              <div className="flex">
                <div className="mr-3">요청사항</div>
                <div> {apply.status}</div>
              </div>
            </div>
            {OpenModal && (
              <AcceptModal
                onClickToggleModal={onClickAcceptModal}
                suggestId={suggestId}
              />
            )}
            <div className="flex flex-col w-[60%] justify-center items-end">
              <div className="text text-textColor font-SCDream6 mt-2">
                {apply.status}
              </div>
              {apply.status === "결제 대기 중" ? (
                <>
                  <button className="text text-pointColor mt-1">
                    채팅하기
                  </button>
                  <button className="text text-negativeMessage mt-1">
                    거절하기
                  </button>
                </>
              ) : (
                <>
                  <button
                    className="text text-pointColor mt-1"
                    onClick={e => onClickAcceptModal(apply.suggestId)}
                  >
                    수락하기
                  </button>
                  <button className="text text-pointColor mt-1">
                    채팅하기
                  </button>
                  <button className="text text-negativeMessage mt-1">
                    거절하기
                  </button>
                </>
              )}
            </div>
          </div>
        </div>
      ))}
    </div>
  );
};
export default ApplicationList;
