import { useRouter } from "next/router";
import DetailSummeryContainer from "components/reuse/container/DetailSummeryContainer";
import MypageContentContainer from "components/reuse/container/MypageContentContainer";
import { useEffect, useState } from "react";
import MypageTabBtn from "components/reuse/btn/MypageTabBtn";
import MypageInfo from "components/Mypage/MypageInfo";
import TeacherTab from "components/Mypage/TeacherTab";
import StudentTab from "components/Mypage/StudentTab";
import useGetTutorInfo from "hooks/mypage/useGetTutorInfo";
import useGetMyTutor from "hooks/mypage/useGetMyTutor";
import { useSetRecoilState } from "recoil";
import { chatActive, roomIdState } from "atoms/chat/chatAtom";
const Mypage = () => {
  const router = useRouter();

  const setActive = useSetRecoilState(chatActive);
  const setRoomId = useSetRecoilState(roomIdState);

  useEffect(() => {
    setActive(false);
    setRoomId(0);
  }, []);

  const { data: myTutorUrl, isSuccess } = useGetMyTutor();
  const [lessonId, setLessonId] = useState(0);
  // const lessonId = myTutorUrl?.data.lessonUrl.split("/")[4];

  const [tab, setTab] = useState(1);

  const {
    refetch: refetchTutorInfo,
    data: tutorInfoData,
    isSuccess: tutorInfoSuccess,
  } = useGetTutorInfo(lessonId, tab);
  const handleTabClick = (id: number) => {
    setTab(id);
    refetchTutorInfo();
  };

  useEffect(() => {
    setLessonId(myTutorUrl?.data.lessonId);
  }, [isSuccess]);

  useEffect(() => {
    // tab이 바뀔때마다 refetch 실행
    if (lessonId && tutorInfoSuccess) {
      refetchTutorInfo();
    }
    if (tab === 1 && lessonId) {
      // teacherTab refetch
      refetchTutorInfo();
    } else if (tab === 2 && lessonId) {
      // StudentTab refetch
    } else if (tab === 3) {
      router.push("/chat/0");
    }
  }, [tab, lessonId]);

  return (
    <>
      <div className="flex flex-col bg-bgColor items-center justify-center w-full h-full pt-4">
        {/* 내요약정보 */}
        <DetailSummeryContainer>
          <MypageInfo></MypageInfo>
        </DetailSummeryContainer>
        <div className="flex w-full h-fit rounded-xl flex-row justify-center mt-5">
          <MypageTabBtn
            bold={tab === 1 ? true : false}
            onClick={() => {
              handleTabClick(1);
            }}
          >
            선생님용
          </MypageTabBtn>
          <MypageTabBtn
            bold={tab === 2 ? true : false}
            onClick={() => {
              handleTabClick(2);
            }}
          >
            학생용
          </MypageTabBtn>
          <MypageTabBtn
            bold={tab === 3 ? true : false}
            onClick={() => {
              handleTabClick(3);
            }}
          >
            채팅목록
          </MypageTabBtn>
        </div>
        <div className="desktop:min-w-[1000px] min-w-[95%] desktop:min-h-[300px] w-full h-full flex desktop:flex-row flex-col justify-center desktop:items-start items-center">
          <MypageContentContainer>
            {tab === 1 && tutorInfoSuccess && <TeacherTab></TeacherTab>}
            {tab === 1 && !tutorInfoSuccess && (
              <div className="flex flex-col justify-center items-center w-full h-36 font-SCDream3 text-lg text-textColor mt-20">
                <div>아직 등록한 과외가 없어요🙂</div>
                <div>과외를 등록하고 수업을 진행해 보세요</div>
              </div>
            )}
            {tab === 2 && <StudentTab></StudentTab>}
          </MypageContentContainer>
        </div>
      </div>
    </>
  );
};

export default Mypage;
