import Image from "next/image";
import AreaIcon from "../../assets/icon/detailSummeryIcon/AreaIcon";
import CareerIcon from "../../assets/icon/detailSummeryIcon/CareerIcon";
import CompanyIcon from "../../assets/icon/detailSummeryIcon/CompanyIcon";
import NickNameIcon from "assets/icon/detailSummeryIcon/NickNameIcon";

const MobileDetailBasicInfo = (basicInfo: any) => {
  return (
    <>
      <div className="w-full h-fit flex flex-col justify-center items-center p-5">
        {/* profile Image */}
        <div className="w-fit h-fit flex flex-col justify-center items-center p-3">
          <Image
            src={"https://pbs.twimg.com/media/FgYA_RAWQAEWCw3.jpg"}
            // src={basicInfo.profileImage}
            alt="profile Image"
            width={150}
            height={150}
            className="rounded-xl border border-borderColor"
          />
        </div>
        {/* Language Tags */}
        <div className="flex flex-row w-full h-fit justify-center items-center mb-2">
          {basicInfo.basicInfo?.languages.map((el: string, idx: number) => {
            return (
              <div
                key={idx}
                className={
                  el === "Javascript"
                    ? `w-fit h-fit bg-Javascript px-2 py-1 rounded-xl mr-2 text-white font-SCDream3 desktop:text-xs text-[11px]`
                    : el === "Python"
                    ? `w-fit h-fit bg-Python px-2 py-1 rounded-xl mr-2 text-white font-SCDream3 desktop:text-xs text-[11px]`
                    : el === "Java"
                    ? `w-fit h-fit bg-Java px-2 py-1 rounded-xl mr-2 text-white font-SCDream3 desktop:text-xs text-[11px]`
                    : el === "Go"
                    ? `w-fit h-fit bg-Go px-2 py-1 rounded-xl mr-2 text-white font-SCDream3 desktop:text-xs text-[11px]`
                    : el === "Kotlin"
                    ? `w-fit h-fit bg-Kotlin px-2 py-1 rounded-xl mr-2 text-white font-SCDream3 desktop:text-xs text-[11px]`
                    : el === "Swift"
                    ? `w-fit h-fit bg-Swift px-2 py-1 rounded-xl mr-2 text-white font-SCDream3 desktop:text-xs text-[11px]`
                    : el === "C#"
                    ? `w-fit h-fit bg-C# px-2 py-1 rounded-xl mr-2 text-white font-SCDream3 desktop:text-xs text-[11px]`
                    : `w-fit h-fit bg-PHP px-2 py-1 rounded-xl mr-2 text-white font-SCDream3 desktop:text-xs text-[11px]`
                }
              >
                {el}
              </div>
            );
          })}
        </div>
        {/* Title */}
        <div className="w-full h-fit flex flex-row justify-center items-center font-SCDream5 desktop:text-xl text-md text-textColor mt-2">
          {basicInfo.basicInfo?.title}
        </div>
        <div className="w-full h-fit flex flex-row justify-center items-center font-SCDream4 text-sm text-textColor mt-2 mb-4">
          {basicInfo.basicInfo?.cost}원 / 1회
        </div>
        <div className="flex flex-col w-full h-fit justify-center items-center ml-7">
          {/* 닉네임 */}
          <div className="flex flex-row justify-start items-center w-64 h-fit mb-3">
            <NickNameIcon />
            <div className="ml-2 pt-1 w-24 h-fit flex flex-row justify-start items-center font-SCDream3 desktop:text-sm text-xs text-textColor">
              닉네임
            </div>
            <div className="ml-1.5 pt-1 w-fit h-fit flex flex-row justify-start items-center font-SCDream3 desktop:text-sm text-xs text-textColor">
              {basicInfo.basicInfo?.name}
            </div>
          </div>
          {/* 재직회사 / 학력 */}
          <div className="flex flex-row justify-start items-center w-64 h-fit mb-3">
            <CompanyIcon />
            <div className="ml-1.5 pt-1 w-24 h-fit flex flex-row justify-start items-center font-SCDream3 desktop:text-sm text-xs text-textColor">
              재직회사 / 학력
            </div>
            <div className="ml-2 pt-1 w-fit h-fit flex flex-row justify-start items-center font-SCDream3 desktop:text-sm text-xs text-textColor">
              {basicInfo.basicInfo?.company}
            </div>
          </div>
          {/* 경력 */}
          <div className="flex flex-row justify-start items-center w-64 h-fit mb-3">
            <CareerIcon />
            <div className="ml-1.5 pt-1 w-24 h-fit flex flex-row justify-start items-center font-SCDream3 desktop:text-sm text-xs text-textColor">
              경력
            </div>
            <div className="ml-2 pt-1 w-fit h-fit flex flex-row justify-start items-center font-SCDream3 desktop:text-sm text-xs text-textColor">
              {basicInfo.basicInfo?.career}년
            </div>
          </div>
          {/* 가능지역 */}
          <div className="flex flex-row justify-start items-center w-64 h-fit mb-3">
            <AreaIcon />
            <div className="ml-1.5 pt-1 w-24 h-fit flex flex-row justify-start items-center font-SCDream3 desktop:text-sm text-xs text-textColor">
              가능지역
            </div>
            <div className="ml-2 pt-1 w-fit h-fit flex flex-row justify-start items-center font-SCDream3 desktop:text-sm text-xs text-textColor">
              {basicInfo.basicInfo?.address.map((el: string) => {
                return <div className="w-fit h-fit mr-1">{el}</div>;
              })}
            </div>
          </div>
        </div>
      </div>
    </>
  );
};

export default MobileDetailBasicInfo;
