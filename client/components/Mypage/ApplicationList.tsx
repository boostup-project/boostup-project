import useGetMyTutor from "hooks/mypage/useGetMyTutor";
import { useRouter } from "next/router";
const ApplicationList = () => {
  // const { data: myTutorUrl } = useGetMyTutor;

  // const router = useRouter();
  // const toMyTutor = () => {
  //   router.push(myTutorUrl);
  // };
  return (
    <div className="flex flex-col w-full">
      <button
        className="flex flex-col bg-pointColor text-white font-SCDream7 desktop:text-lg tablet:text-base text-sm rounded-xl items-start justify-center border border-borderColor w-full desktop:h-[50px] tablet:h-[43px] h-[38px] py-3 desktop:mt-5 tablet:mt-3 mt-2 pl-5"
        // onClick={toMyTutor}
      >
        나의 과외로 이동하기
      </button>
      {/* {map} 수업신청정보 */}
      <div className="flex flex-col">
        <div className="flex flex-row w-full h-fit border border-borderColor rounded-xl desktop:mt-5 tablet:mt-3 mt-2 p-3 pl-5">
          <div className="flex flex-col w-[43%]">
            <div className="mb-3 ">data.name</div>
            <div className="flex">
              <div className="mr-3">희망요일</div>
              <div> data.days</div>
            </div>
            <div className="flex">
              <div className="mr-3">희망언어</div>
              <div> data.languages</div>
            </div>
            <div className="flex">
              <div className="mr-3">요청사항</div>
              <div> data.requests</div>
            </div>
          </div>
          <div className="flex flex-col w-[60%] justify-end items-end">
            <button className="text text-pointColor m-2">수락하기</button>
            <button className="text text-pointColor m-2">채팅하기</button>
            <button className="text text-negativeMessage m-2">거절하기 </button>
          </div>
        </div>
      </div>
    </div>
  );
};
export default ApplicationList;
