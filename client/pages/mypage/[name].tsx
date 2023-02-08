import { useRouter } from "next/router";
import DetailSummeryContainer from "components/reuse/container/DetailSummeryContainer";
import DetailTabBtn from "components/reuse/btn/DetailTabBtn";
import MypageContentContainer from "components/reuse/container/MypageContentContainer";
import DetailBtn from "components/reuse/btn/DetailBtn";
import { useEffect, useState } from "react";
import useGetExtra from "hooks/detail/useGetExtra";
import useGetCurriculum from "hooks/detail/useGetCurriculum";
import useGetBasicInfo from "hooks/detail/useGetBasicInfo";
import useWindowSize from "hooks/useWindowSize";
import DetailCurriculum from "components/detailComp/DetailCurriculum";
import DetailExtra from "components/detailComp/DetailExtra";
import MypageTabBtn from "components/reuse/btn/MypageTabBtn";
import MypageInfo from "components/Mypage/MypageInfo";
import TeacherTab from "components/Mypage/TeacherTab";
import StudentTab from "components/Mypage/StudentTab";
import useGetStudentInfo from "hooks/mypage/useGetStudentInfo";
import useGetTutorInfo from "hooks/mypage/useGetTutorInfo";
import useGetMyTutor from "hooks/mypage/useGetMyTutor";
import { useSetRecoilState } from "recoil";
import { chatActive } from "atoms/chat/chatAtom";
const Mypage = () => {
  const router = useRouter();

  const setActive = useSetRecoilState(chatActive);

  useEffect(() => {
    setActive(false);
  }, []);

  const { data: myTutorUrl } = useGetMyTutor();
  const lessonId = Number(myTutorUrl?.data.lessonUrl.slice(29));

  const [tab, setTab] = useState(1);

  const { refetch: refetchStudentInfo, data: studentInfoData } =
    useGetStudentInfo();
  const { refetch: refetchTutorInfo, data: tutorInfoData } = useGetTutorInfo(
    lessonId,
    tab,
  );
  const handleTabClick = (id: number) => {
    setTab(id);
    refetchTutorInfo;
  };
  const widthSize = useWindowSize();

  useEffect(() => {
    // tab이 바뀔때마다 refetch 실행
    if (lessonId) {
      // 요약정보 요청
    }

    if (tab === 1 && lessonId) {
      // teacherTab refetch
    } else if (tab === 2 && lessonId) {
      // StudentTab refetch
    } else if (tab === 3) {
      // ChatList refetch
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
            {tab === 1 && <TeacherTab></TeacherTab>}
            {tab === 2 && <StudentTab></StudentTab>}
          </MypageContentContainer>
        </div>
      </div>
    </>
  );
};

export default Mypage;
