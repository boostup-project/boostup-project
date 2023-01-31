const ApplicationList = () => {
  return (
    <div className="flex flex-col w-full">
      <button className="flex flex-col bg-pointColor text-white font-SCDream7 rounded-xl items-start justify-center border border-borderColor w-full h-full py-3 mt-5 pl-5">
        나의 과외로 이동하기
      </button>
      {/* {map} 수업신청정보 */}
      <div className="flex flex-col">
        <div className="flex flex-row w-full h-fit border border-borderColor rounded-xl mt-3 p-3 pl-5">
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
