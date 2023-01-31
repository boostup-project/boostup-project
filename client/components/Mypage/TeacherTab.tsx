import ApplicationList from "./ApplicationList";
import { useState, useEffect } from "react";
import MypageTabBtn from "components/reuse/btn/MypageTabBtn";
import DetailTabBtn from "components/reuse/btn/DetailTabBtn";
import MypageContentContainer from "components/reuse/container/MypageContentContainer";
import MypageUnderContainer from "components/reuse/container/MypageUnderContainer";
import ClassList from "./ClassList";
import FinishedClass from "./FinishedClass";
const TeacherTab = () => {
  const [tab, setTab] = useState(1);

  const handleTabClick = (id: number) => {
    setTab(id);
  };
  useEffect(() => {
    // refetch 실행위치
    // tab이 바뀔때마다 refetch 실행
    // console.log(lessonId);

    if (tab === 1) {
      // teacherTab refetch
    } else if (tab === 2) {
      // StudentTab refetch
    } else if (tab === 3) {
      // ChatList refetch
    }
  }, [tab]);

  return (
    <>
      <div className="flex flex-col bg-bgColor items-center justify-center w-full h-full pt-1">
        <div className="flex w-full h-fit rounded-xl flex-row justify-center items-center mt-5">
          <MypageTabBtn
            bold={tab === 1 ? true : false}
            onClick={() => {
              handleTabClick(1);
            }}
          >
            신청내역
          </MypageTabBtn>
          <MypageTabBtn
            bold={tab === 2 ? true : false}
            onClick={() => {
              handleTabClick(2);
            }}
          >
            과외내역
          </MypageTabBtn>
          <MypageTabBtn
            bold={tab === 3 ? true : false}
            onClick={() => {
              handleTabClick(3);
            }}
          >
            종료과외
          </MypageTabBtn>
        </div>
        <div className="desktop:min-w-[1000px] min-w-[95%] w-full h-full flex desktop:flex-row flex-col justify-center desktop:items-start items-center">
          <MypageUnderContainer>
            {tab === 1 && <ApplicationList></ApplicationList>}
            {tab === 2 && <ClassList></ClassList>}
            {tab === 3 && <FinishedClass></FinishedClass>}
          </MypageUnderContainer>
        </div>
      </div>
    </>
  );
};
export default TeacherTab;
