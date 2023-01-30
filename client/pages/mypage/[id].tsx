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
import MypageInfo from "components/mypage/MypageInfo";

const Mypage = () => {
  const router = useRouter();
  const lessonId = Number(router.query.id);

  const [tab, setTab] = useState(1);

  const {
    refetch: refetchGetExtra,
    isSuccess: extraSuccess,
    isError: extraError,
    data: extraData,
  } = useGetExtra(lessonId);

  const {
    refetch: refetchGetCur,
    isSuccess: curSuccess,
    isError: curError,
    data: curData,
  } = useGetCurriculum(lessonId);

  const handleTabClick = (id: number) => {
    setTab(id);
  };

  const {
    refetch: basicInfoRefetch,
    data: basicInfo,
    isSuccess: basicInfoSuccess,
  } = useGetBasicInfo(lessonId);

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
      // 상세정보 refetch
      refetchGetExtra();
    } else if (tab === 2 && lessonId) {
      // 진행방식 refetch
      refetchGetCur();
    } else if (tab === 3) {
      // 과외후기 refetch
    }
  }, [tab, lessonId]);

  return (
    <>
      <div className="flex flex-col bg-bgColor items-center justify-center w-full h-full pt-28">
        {/* 요약정보 */}
        <DetailSummeryContainer>
          {widthSize > 764 ? <MypageInfo></MypageInfo> : <></>}
        </DetailSummeryContainer>
        <div className="flex w-full desktop:min-w-[1000px] min-w-[95%] h-fit rounded-xl flex-row justify-center items-center mt-5">
          <MypageTabBtn
            bold={tab === 1 ? true : false}
            onClick={() => {
              handleTabClick(1);
            }}
          >
            선생님용
          </MypageTabBtn>
          <DetailTabBtn
            bold={tab === 2 ? true : false}
            onClick={() => {
              handleTabClick(2);
            }}
          >
            학생용
          </DetailTabBtn>
          <DetailTabBtn
            bold={tab === 3 ? true : false}
            onClick={() => {
              handleTabClick(3);
            }}
          >
            채팅목록
          </DetailTabBtn>
        </div>
        <div className="desktop:min-w-[1000px] min-w-[95%] w-full h-full flex desktop:flex-row flex-col justify-center desktop:items-start items-center">
          <MypageContentContainer>
            {tab === 1 && (
              <DetailExtra extraData={extraData} lessonId={lessonId} />
            )}
            {tab === 2 && <DetailCurriculum curData={curData} />}

            {/* 각 탭별 컴포넌트를 생성하여 넣어주세요! */}
          </MypageContentContainer>
        </div>
      </div>
    </>
  );
};

export default Mypage;
