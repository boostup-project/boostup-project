import { IconPaper, IconPlace, IconRibbon, IconWon } from "assets/icon/";

const Card = () => {
  return (
    <div className="h-fit m-1 border border-pointColor desktop:w-1/5 tablet:w-1/4 w-1/3 ">
      <div className="flex flex-col w-full h-1/4 border border-textColor">
        <div className="flex w-full desktop:h-1/3 border border-black tablet:h-1/3 h-1/2">
          <img
            className="rounded-t-lg"
            src="https://www.medidata.com/wp-content/uploads/2021/11/Medidata_RaveCoder_web_keyfeatures_A1-1.jpg"
          />
        </div>
        <div className="flex flex-col w-full h-2/3 border border-red">
          <div className="flex justify-start items-start w-full h-fit font-SCDream5 desktop:text-xs tablet:text-[10px] text-[8px] text-textColor ml-2  my-1">
            송길영
          </div>
          <div className="flex inline-block justify-start items-start w-full h-fit font-SCDream6  desktop:text-base tablet:text-sm text-xs text-textColor ml-2 mb-2 whitespace-wrap ">
            React-Native 입문
          </div>
          <div className="flex justify-start items-start w-full h-fit font-SCDream5 text-textColor ml-2 mb-2 desktop:text-xs tablet:text-[5px] text-[4px]">
            <div className="mr-1 desktop:w-4 tablet:w-3.5 w-3">
              <IconRibbon />
            </div>
            카카오
          </div>
          <div className="flex justify-start items-start w-full h-fit font-SCDream5 text-xs text-textColor ml-2  mb-2">
            <div className="mr-1 desktop:w-4 tablet:w-3.5 w-3">
              <IconPaper />
            </div>
            3년
          </div>
          <div className="flex justify-start items-start w-full h-fit font-SCDream5 text-xs text-textColor ml-2 my-1">
            <div className="mr-1 desktop:w-3.5 tablet:w-3 w-2.5">
              <IconPlace />
            </div>
            종로구
          </div>
          <div className="flex justify-center items-start w-full h-fit font-SCDream6 text-md text-textColor ml-1 mb-2">
            <IconWon width="15" heigth="15" />
            <div className="ml-1 whitespace-nowrap">65,000/회</div>
          </div>
        </div>
      </div>
    </div>
  );
};
export default Card;
