import ApplicationList from "./ApplicationList";
import { useState, useEffect } from "react";
import MypageTabBtn from "components/reuse/btn/MypageTabBtn";
import MypageUnderContainer from "components/reuse/container/MypageUnderContainer";
import StudentClass from "./StudentClass";
import StudentBookmark from "./StudentBookmark";
import StudentReview from "./StudentReview";
import useGetStudentInfo from "hooks/mypage/useGetStudentInfo";
import useGetAllBookmark from "hooks/mypage/useGetAllBookmark";
const StudentTab = () => {
  const [tab, setTab] = useState(1);

  const handleTabClick = (id: number) => {
    setTab(id);
  };
  const { refetch: refetchStudentInfo } = useGetStudentInfo();
  const { refetch: allBookmark } = useGetAllBookmark();
  useEffect(() => {
    // tab이 바뀔때마다 refetch 실행
    if (tab === 1) {
      refetchStudentInfo();
    } else if (tab === 2) {
      allBookmark();
    } else if (tab === 3) {
      // ChatList refetch
    }
  }, [tab]);

  return (
    <>
      <div className="flex flex-col bg-bgColor items-center justify-center w-full h-full pt-1">
        <div className="flex w-full h-fit rounded-xl flex-row justify-center items-center desktop:mt-5 tablet:mt-3 mt-2">
          <MypageTabBtn
            bold={tab === 1 ? true : false}
            onClick={() => {
              handleTabClick(1);
            }}
          >
            수강내역
          </MypageTabBtn>
          <MypageTabBtn
            bold={tab === 2 ? true : false}
            onClick={() => {
              handleTabClick(2);
            }}
          >
            관심과외
          </MypageTabBtn>
          <MypageTabBtn
            bold={tab === 3 ? true : false}
            onClick={() => {
              handleTabClick(3);
            }}
          >
            나의후기
          </MypageTabBtn>
        </div>
        <div className="desktop:min-w-[1000px] min-w-[95%] w-full h-full flex desktop:flex-row flex-col justify-center desktop:items-start items-center">
          <MypageUnderContainer>
            {tab === 1 && <StudentClass></StudentClass>}
            {tab === 2 && <StudentBookmark></StudentBookmark>}
            {tab === 3 && <StudentReview></StudentReview>}
          </MypageUnderContainer>
        </div>
      </div>
    </>
  );
};
export default StudentTab;
