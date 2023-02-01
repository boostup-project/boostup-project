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
const Mypage = () => {
  //memberId? memberName?
  const router = useRouter();
  const lessonId = Number(router.query.id);

  const [tab, setTab] = useState(1);

  const {
    refetch: basicInfoRefetch,
    data: basicInfo,
    isSuccess: basicInfoSuccess,
  } = useGetBasicInfo(lessonId);

  const { refetch: refetchStudentInfo, data: studentInfoData } =
    useGetStudentInfo();
  const { refetch: refetchTutorInfo, data: tuturInfoData } = useGetTutorInfo(
    lessonId,
    tab,
  );
  const handleTabClick = (id: number) => {
    setTab(id);
  };
  const widthSize = useWindowSize();

  useEffect(() => {
    // refetch 실행위치
    // tab이 바뀔때마다 refetch 실행
    // console.log(lessonId);
    if (lessonId) {
      // 요약정보 요청
      basicInfoRefetch();
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
      <div className="flex flex-col bg-bgColor items-center justify-center w-full h-full pt-28">
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
        <div className="desktop:min-w-[1000px] min-w-[95%] w-full h-full flex desktop:flex-row flex-col justify-center desktop:items-start items-center">
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
