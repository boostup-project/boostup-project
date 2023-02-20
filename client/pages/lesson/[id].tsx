import { useRouter } from "next/router";
import DetailSummeryContainer from "components/reuse/container/DetailSummeryContainer";
import DetailTabBtn from "components/reuse/btn/DetailTabBtn";
import DetailContentContainer from "components/reuse/container/DetailContentContainer";
import DetailBasicInfo from "components/detailComp/DetailBasicInfo";
import MobileDetailBasicInfo from "components/detailComp/MobileDetailBasicInfo";
import DetailBasicInfoEditModal from "components/detailComp/DetailBasicInfoEditModal";
import { useEffect, useState } from "react";
import useGetExtra from "hooks/detail/useGetExtra";
import useGetCurriculum from "hooks/detail/useGetCurriculum";
import useGetBasicInfo from "hooks/detail/useGetBasicInfo";
import useWindowSize from "hooks/useWindowSize";
import DetailCurriculum from "components/detailComp/DetailCurriculum";
import DetailReview from "components/detailComp/DetailReview";
import DetailExtra from "components/detailComp/DetailExtra";
import DetailButtons from "components/Detail/DetailButtons";
import { useRecoilValue } from "recoil";
import { powerBasicEditModal, refetchToggle } from "atoms/detail/detailAtom";
import useGetDetailReview from "hooks/detail/useGetDetailReview";
import axios from "axios";
import { BasicInfo } from "components/createModal/BasicInfo";
import SeoHead from "components/reuse/SEO/SeoHead";

interface LessonMeta {
  lessonMeta: BasicInfo;
}

const Detail = ({ lessonMeta }: LessonMeta) => {
  // lessonId 받아오기
  const router = useRouter();
  const lessonId = Number(router.query.id);

  const basicEditPower = useRecoilValue(powerBasicEditModal);
  const toggle = useRecoilValue(refetchToggle);

  const [tab, setTab] = useState(1);
  const [editable, setEditable] = useState<boolean>();

  const {
    refetch: basicInfoRefetch,
    data: basicInfo,
    isSuccess: basicInfoSuccess,
  } = useGetBasicInfo(lessonId!);

  const { refetch: refetchGetExtra, data: extraData } = useGetExtra(lessonId!);

  const { refetch: refetchGetCur, data: curData } = useGetCurriculum(lessonId!);

  const handleTabClick = (id: number) => {
    setTab(id);
  };

  const {
    refetch: reviewRefetch,
    data: reviewData,
    isSuccess: reviewSuccess,
  } = useGetDetailReview(lessonId!);

  const widthSize = useWindowSize();

  useEffect(() => {
    if (basicInfo) {
      setEditable(basicInfo.data.editable);
    }
  }, [basicInfoSuccess]);

  useEffect(() => {
    // refetch 실행위치
    // tab이 바뀔때마다 refetch 실행
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
    } else if (tab === 3 && lessonId) {
      // 과외후기 refetch
      reviewRefetch();
    }
  }, [tab, lessonId, toggle]); //tab,

  return (
    <>
      <SeoHead metaInfo={lessonMeta} metaType="Lesson" />
      {basicInfoSuccess && basicEditPower ? (
        <DetailBasicInfoEditModal basicData={basicInfo} />
      ) : null}
      <div className="flex flex-col bg-bgColor items-center justify-start w-full h-full pt-4">
        {/* 요약정보 */}
        <DetailSummeryContainer>
          {widthSize > 764 ? (
            <DetailBasicInfo basicInfo={basicInfo?.data} />
          ) : (
            <MobileDetailBasicInfo basicInfo={basicInfo?.data} />
          )}
        </DetailSummeryContainer>
        <div className="flex w-3/4 desktop:min-w-[1000px] min-w-[95%] h-fit rounded-xl flex-row justify-start mt-5">
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
        <div className="desktop:min-w-[1000px] min-w-[95%] w-3/4 h-full flex desktop:flex-row flex-col justify-start desktop:items-start items-center">
          <DetailContentContainer>
            {tab === 1 && (
              <DetailExtra
                extraData={extraData}
                lessonId={lessonId!}
                editable={editable}
              />
            )}
            {tab === 2 && (
              <DetailCurriculum curData={curData} editable={editable} />
            )}
            {tab === 3 && reviewSuccess && (
              <DetailReview reviewData={reviewData?.data} />
            )}

            {/* 각 탭별 컴포넌트를 생성하여 넣어주세요! */}
          </DetailContentContainer>
          <div className="w-full h-full flex flex-col justify-start items-center pl-3">
            <DetailButtons basicInfo={basicInfo?.data} />
          </div>
        </div>
      </div>
    </>
  );
};

export default Detail;

export async function getServerSideProps(context: { query: { id: any } }) {
  const { id } = context.query;
  const { data } = await axios.get(
    `${process.env.NEXT_PUBLIC_API_URL}/lesson/${id}`,
    {
      headers: {
        "content-Type": `application/json`,
        "ngrok-skip-browser-warning": "69420",
        Authorization: `${null}`,
      },
    },
  );
  data.id = id;
  const lessonMeta: BasicInfo = data;
  return {
    props: { lessonMeta },
  };
}
