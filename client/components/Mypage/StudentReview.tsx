import {
  IconRibbon,
  IconWon,
  IconPaper,
  IconPlace,
  IconEmptyheart,
  IconFullheart,
} from "assets/icon";
const StudentReview = () => {
  return (
    <>
      <div className="flex w-full">
        <div className="flex flex-row h-fit w-full rounded-lg border border-borderColor mt-3">
          {/* {left} */}
          <div className="flex flex-col w-2/3  justify-center desktop:pl-2 m-3">
            <div className="flex">
              {/* {card.languages?.map((el: any, idx: any) => {
                      return (
                        <div
                          key={idx}
                          className={`flex justify-center bg-${el} items-center px-1 py-0.5 ml-1 mt-1 border rounded-xl desktop:text-xs tablet:text-[10px] text-[6px]`}
                        >
                          {el}
                        </div>
                      );
                    })} */}
              data.languages
            </div>
            <div className="flex justify-start items-start w-full h-fit font-SCDream5 desktop:text-xs tablet:text-[12px] text-[8px] text-textColor ml-2  my-1">
              {/* {card.name} */}myname
            </div>
            <div className="flex justify-start items-start w-full h-fit font-SCDream6 desktop:text-lg tablet:text-base text-xs text-textColor ml-1 mb-2 flex-wrap">
              {/* {card.title} */}JavaScript w.Java
            </div>
            <div className="flex desktop:text-base tablet:text-sm text-[12px]">
              만족도 {"★ ".repeat(5)}
            </div>
            <div className="flex desktop:text-base tablet:text-sm text-[12px] desktop:mt-3 tablet:mt-2">
              만족스러운 과외였습니다 12번의 수업으로 JS에 대해 헷갈렸던
              개념들이 확실히 잡혔어요! 수업 준비도 잘 해주시고, 필요한 난이도에
              맞춰서 쉽게 진행해주셨습니다. 입문자분들에게 추천드려요!
            </div>
          </div>
          {/* right */}
          <div className="flex flex-col w-1/2 justify-center items-end desktop:mr-4 tablet:mr-2 mr-2">
            <div className="flex  desktop:text-xl tablet:text-lg text-[14px]">
              {/* {period} */}2022.10.11~2022.12.26
            </div>
            <div className="flex">
              <button className="text text-pointColor m-2 desktop:text-base tablet:text-sm text-[10px]">
                수정하기
              </button>
              <button className="text text-negativeMessage m-2 desktop:text-base tablet:text-sm text-[10px]">
                삭제하기
              </button>
            </div>
          </div>
        </div>
      </div>
    </>
  );
};
export default StudentReview;
