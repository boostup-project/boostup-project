import { IconPaper, IconPlace, IconRibbon, IconWon } from "assets/icon/";

const Card = () => {
  return (
    <div className="h-fit m-1 desktop:w-1/5 tablet:w-1/4 w-1/3 ">
      <div className="flex flex-col w-full h-1/4 border border-borderColor rounded-lg">
        <div className="flex w-full desktop:h-full tablet:h-1/3 h-1/2">
          <img
            className="rounded-t-lg"
            src="https://www.medidata.com/wp-content/uploads/2021/11/Medidata_RaveCoder_web_keyfeatures_A1-1.jpg"
          />
        </div>
        <div className="flex flex-col w-full h-2/3">
          <div className="flex flex-row">
            <div className="flex justify-center items-center px-1 py-0.5 ml-1 my-1 border rounded-xl bg-[#0A83F2] desktop:text-xs tablet:text-[10px] text-[8px]">
              JavaScript
            </div>
            <div className="flex justify-center items-center px-1 py-0.5 ml-1 my-1 border rounded-xl bg-[#6ED5F6] desktop:text-xs tablet:text-[10px] text-[8px]">
              python
            </div>
            <div className="flex justify-center items-center px-1 py-0.5 ml-1 my-1 border rounded-xl bg-[#CAD1FA] desktop:text-xs tablet:text-[10px] text-[8px]">
              kotlin
            </div>
          </div>
          <div className="flex justify-start items-start w-full h-fit font-SCDream5 desktop:text-xs tablet:text-[10px] text-[8px] text-textColor ml-2  my-1">
            송길영
          </div>
          <div className="flex inline-block justify-start items-start w-full h-fit font-SCDream6  desktop:text-base tablet:text-sm text-xs text-textColor ml-2 mb-2 whitespace-wrap ">
            React-Native 입문
          </div>
          <div className="flex justify-start items-start w-full h-fit font-SCDream5 text-textColor ml-2 mb-2 desktop:text-xs tablet:text-[10px] text-[8px]">
            <div className="mr-1 desktop:w-4 tablet:w-3.5 w-3">
              <IconRibbon />
            </div>
            카카오
          </div>
          <div className="flex justify-start items-start w-full h-fit font-SCDream5 desktop:text-xs tablet:text-[11px] text-[8px] text-textColor ml-2  mb-2">
            <div className="mr-1 desktop:w-4 tablet:w-3.5 w-3">
              <IconPaper />
            </div>
            3년
          </div>
          <div className="flex justify-start items-start w-full h-fit font-SCDream5 desktop:text-xs tablet:text-[10px] text-[8px] text-textColor ml-2 desktop:my-1 mb-1">
            <div className="mr-1 desktop:w-3.5 tablet:w-2.5 w-2 ">
              <IconPlace />
            </div>
            종로구
          </div>
          <div className="flex justify-center items-start w-full h-fit font-SCDream7 desktop:text-base tablet:text-sm text-[12px] text-textColor ml-1 mb-2">
            <div className="desktop:mt-0.5 tablet:mt-0.5  desktop:w-4 tablet:w-3.5 w-3">
              <IconWon />
            </div>
            <div className="ml-1">65,000/회</div>
          </div>
        </div>
      </div>
    </div>
  );
};
export default Card;
