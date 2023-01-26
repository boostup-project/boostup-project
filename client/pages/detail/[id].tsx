import { useRouter } from "next/router";
import DetailSummeryContainer from "components/reuse/container/DetailSummeryContainer";
import DetailTabBtn from "components/reuse/btn/DetailTabBtn";
import DetailContentContainer from "components/reuse/container/DetailContentContainer";
import DetailBtn from "components/reuse/btn/DetailBtn";
import DetailBasicInfo from "components/detailComp/DetailBasicInfo";
import { useEffect, useState } from "react";
import useGetBasicInfo from "hooks/detail/useGetBasicInfo";

const Detail = () => {
  // lessonId 받아오기
  const router = useRouter();
  const lessonId = Number(router.query.id);

  const [tab, setTab] = useState(1);

  const handleTabClick = (id: number) => {
    setTab(id);
  };

  const { refetch: basicInfoRefetch, data: basicInfo } =
    useGetBasicInfo(lessonId);

  useEffect(() => {
    // refetch 실행위치
    // tab이 바뀔때마다 refetch 실행
    if (lessonId) {
      // 요약정보 요청
      basicInfoRefetch();
    }

    if (tab === 1) {
      // 상세정보 refetch
    } else if (tab === 2) {
      // 진행방식 refetch
    } else if (tab === 3) {
      // 과외후기 refetch
    }
  }, [tab, lessonId]);

  return (
    <>
      <div className="flex flex-col bg-bgColor items-center justify-start w-full h-screen pt-28">
        <DetailSummeryContainer>
          <DetailBasicInfo basicInfo={basicInfo?.data} />
        </DetailSummeryContainer>
        <div className="flex w-3/4 desktop:min-w-[1000px] min-w-[95%] h-fit rounded-xl flex-row justify-start items-center mt-5">
          <DetailTabBtn
            bold={tab === 1 ? true : false}
            onClick={() => {
              handleTabClick(1);
            }}
          >
            상세정보
          </DetailTabBtn>
          <DetailTabBtn
            bold={tab === 2 ? true : false}
            onClick={() => {
              handleTabClick(2);
            }}
          >
            진행방식
          </DetailTabBtn>
          <DetailTabBtn
            bold={tab === 3 ? true : false}
            onClick={() => {
              handleTabClick(3);
            }}
          >
            과외후기
          </DetailTabBtn>
        </div>
        <div className="desktop:min-w-[1000px] min-w-[95%] w-3/4 h-fit flex desktop:flex-row flex-col justify-start items-center">
          <DetailContentContainer>
            각 탭별 컴포넌트를 생성하여 넣어주세요!
          </DetailContentContainer>
          <div className="w-full h-full flex flex-col justify-start items-center pl-3">
            {/* 각 버튼 안에는 디자인에 맞게 div 박스 넣어서 구현해주세요! */}
            <DetailBtn bold={true} remove={false} onClick={() => {}}>
              신청하기
            </DetailBtn>
            <DetailBtn bold={false} remove={false} onClick={() => {}}>
              실시간채팅
            </DetailBtn>
            <DetailBtn bold={false} remove={false} onClick={() => {}}>
              선생님 찜하기
            </DetailBtn>

            <DetailBtn bold={false} remove={true} onClick={() => {}}>
              삭제하기
            </DetailBtn>
          </div>
        </div>
      </div>
    </>
  );
};

export default Detail;
