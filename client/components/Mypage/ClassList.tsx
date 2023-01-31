const ClassList = () => {
  return (
    <>
      <div className="flex flex-col w-full">
        <div className="flex flex-col">
          <div className="flex flex-row w-full h-fit border border-borderColor rounded-xl mt-3 p-3 pl-5">
            <div className="flex flex-col w-[40%]">
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
              <div className="text text-textColor font-SCDream6 m-2">
                과외 중
              </div>
              <button className="text text-pointColor m-2">채팅하기</button>
              <button className="text text-negativeMessage m-2">
                과외종료
              </button>
            </div>
          </div>
        </div>
      </div>
    </>
  );
};
export default ClassList;
