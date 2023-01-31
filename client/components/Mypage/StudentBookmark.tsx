import {
  IconRibbon,
  IconWon,
  IconPaper,
  IconPlace,
  IconEmptyheart,
  IconFullheart,
} from "assets/icon";

const StudentBookmark = () => {
  return (
    <>
      <div className="flex w-full">
        <div className="flex flex-row h-fit w-full rounded-lg border border-borderColor mt-3">
          <div className="desktop:w-100 border border-pointColor">
            <img
              src={
                "https://play-lh.googleusercontent.com/38AGKCqmbjZ9OuWx4YjssAz3Y0DTWbiM5HB0ove1pNBq_o9mtWfGszjZNxZdwt_vgHo=w240-h480-rw"
              }
            ></img>
          </div>
          <div className="flex flex-col w-1/2 border border-borderColor  justify-center">
            <div></div>
            <div className="flex justify-start items-start w-full h-fit font-SCDream5 desktop:text-xs tablet:text-[10px] text-[8px] text-textColor ml-2  my-1">
              {/* {card.name} */}myname
            </div>
            <div className="flex justify-start items-start w-full h-fit font-SCDream6 desktop:text-base tablet:text-sm text-xs text-textColor ml-1 mb-2 flex-wrap">
              {/* {card.title} */}JavaScript w.Java
            </div>
            <div className="flex justify-start items-start w-full h-fit font-SCDream5 text-textColor ml-2 mb-2 desktop:text-sm tablet:text-[12px] text-[12px]">
              <div className="mr-1 desktop:w-5 tablet:w-3.5 w-3.5">
                <IconRibbon />
              </div>
              {/* {card.company} */}company
            </div>
            <div className="flex justify-start items-start w-full h-fit font-SCDream5 desktop:text-sm tablet:text-[11px] text-[12px] text-textColor ml-2  mb-2">
              <div className="mr-1 desktop:w-5 tablet:w-3.5 w-3.5">
                <IconPaper />
              </div>
              {/* {card.career}년 */}3년
            </div>
            <div className="flex justify-start items-start w-full h-fit font-SCDream5 desktop:text-sm tablet:text-xs text-[12px] text-textColor ml-2 desktop:my-1 mb-1">
              <div className="mr-1 desktop:w-4 tablet:w-3 w-2.5 ">
                <IconPlace />
              </div>
              {/* {card.address?.map((el: any, idx: any) => {
                    return (
                      <div className="ml-1" key={idx}>
                        {el}
                      </div>
                    );
                  })} */}
              강북구 강서구
            </div>
          </div>
          <div className="flex flex-col w-1/3 border border-borderColor justify-center items-end desktop:mr-4 tablet:mr-2 mr-2">
            <div className="flex  desktop:text-xl tablet:text-xs text-[14px]">
              <div className="mt-1 mr-1 desktop:w-5 tablet:w-3.5 w-3">
                <IconWon />
              </div>
              {/* {cost} */}65000원
            </div>
            <button className="decktop:w-[30px] tablet:w-[28px] w-[25px] my-6">
              {true ? <IconFullheart /> : <IconEmptyheart />}
            </button>
            <div className="flex">
              <button className="text text-pointColor m-2 desktop:text-base tablet:text-sm text-[10px]">
                채팅하기
              </button>
            </div>
          </div>
        </div>
      </div>
    </>
  );
};
export default StudentBookmark;
