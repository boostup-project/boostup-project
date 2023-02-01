import Image from "next/image";
import NickNameIcon from "assets/icon/detailSummeryIcon/NickNameIcon";
import AreaIcon from "assets/icon/detailSummeryIcon/AreaIcon";
import CompanyIcon from "assets/icon/detailSummeryIcon/CompanyIcon";
import CareerIcon from "assets/icon/detailSummeryIcon/CareerIcon";
import { useRecoilState } from "recoil";
import { powerBasicEditModal, editMode } from "atoms/detail/detailAtom";

const DetailBasicInfo = (basicInfo: any) => {
  const [power, setPower] = useRecoilState(powerBasicEditModal);
  const [mode, setMode] = useRecoilState(editMode);
  const handleClickEdit = () => {
    setPower(true);
    setMode(true);
  };

  return (
    <>
      <div className="w-full h-full flex flex-row justify-center items-center">
        {/* profile Image */}
        <div className="w-1/5 h-full flex flex-col justify-center items-center p-5">
          <Image
            // src={"https://pbs.twimg.com/media/FgYA_RAWQAEWCw3.jpg"}
            src={basicInfo.basicInfo?.profileImage}
            alt="profile Image"
            width={200}
            height={200}
            className="rounded-xl border border-borderColor"
          />
        </div>
        {/* lesson Info */}
        <div className="w-3/5 h-full flex flex-col justify-center items-start p-5">
          {/* language Tag */}
          <div className="w-full h-fit flex flex-row justify-start items-center">
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
          <div className="w-full h-fit flex flex-row justify-start items-center font-SCDream5 desktop:text-xl text-md text-textColor mt-2">
            {basicInfo.basicInfo?.title}
          </div>
          {/* 닉네임 */}
          <div className="flex flex-row justify-start items-center w-full h-fit mt-3">
            <NickNameIcon />
            <div className="ml-2 pt-1 w-40 h-fit flex flex-row justify-start items-center font-SCDream3 desktop:text-sm text-xs text-textColor">
              닉네임
            </div>
            <div className="ml-1.5 pt-1 w-fit h-fit flex flex-row justify-start items-center font-SCDream3 desktop:text-sm text-xs text-textColor">
              {basicInfo.basicInfo?.name}
            </div>
          </div>
          {/* 재직회사 / 학력 */}
          <div className="flex flex-row justify-start items-center w-full h-fit mt-1">
            <CompanyIcon />
            <div className="ml-1.5 pt-1 w-40 h-fit flex flex-row justify-start items-center font-SCDream3 desktop:text-sm text-xs text-textColor">
              재직회사 / 학력
            </div>
            <div className="ml-2 pt-1 w-fit h-fit flex flex-row justify-start items-center font-SCDream3 desktop:text-sm text-xs text-textColor">
              {basicInfo.basicInfo?.company}
            </div>
          </div>
          {/* 경력 */}
          <div className="flex flex-row justify-start items-center w-full h-fit mt-1">
            <CareerIcon />
            <div className="ml-1.5 pt-1 w-40 h-fit flex flex-row justify-start items-center font-SCDream3 desktop:text-sm text-xs text-textColor">
              경력
            </div>
            <div className="ml-2 pt-1 w-fit h-fit flex flex-row justify-start items-center font-SCDream3 desktop:text-sm text-xs text-textColor">
              {basicInfo.basicInfo?.career}년
            </div>
          </div>
          {/* 가능지역 */}
          <div className="flex flex-row justify-start items-center w-full h-fit mt-1">
            <AreaIcon />
            <div className="ml-1.5 pt-1 w-40 h-fit flex flex-row justify-start items-center font-SCDream3 desktop:text-sm text-xs text-textColor">
              가능지역
            </div>
            <div className="ml-2 pt-1 w-fit h-fit flex flex-row justify-start items-center font-SCDream3 desktop:text-sm text-xs text-textColor">
              {basicInfo.basicInfo?.address.map((el: string, idx: number) => {
                return (
                  <div key={idx} className="w-fit h-fit mr-1">
                    {el}
                  </div>
                );
              })}
            </div>
          </div>
        </div>
        <div className="w-1/5 h-full flex flex-col justify-center items-end pr-10 pt-16">
          {/* 가격 */}
          <div className="w-full h-fit flex flex-row justify-end items-center font-SCDream5 desktop:text-xl text-sm text-textColor mt-2 mb-14">
            {basicInfo.basicInfo?.cost}원 / 1회
          </div>
          <div
            className="w-40 h-fit flex flex-row justify-end items-center font-SCDream3 text-sm text-pointColor cursor-pointer"
            onClick={handleClickEdit}
          >
            edit
          </div>
        </div>
      </div>
    </>
  );
};

export default DetailBasicInfo;
