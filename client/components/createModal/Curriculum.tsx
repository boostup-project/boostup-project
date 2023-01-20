import "@uiw/react-md-editor/markdown-editor.css";
import "@uiw/react-markdown-preview/markdown.css";
import SmallBtn from "components/reuse/btn/SmallBtn";
import dynamic from "next/dynamic";
import useWindowSize from "hooks/useWindowSize";
import { bold, italic } from "@uiw/react-md-editor/lib/commands/";
import { Dispatch, SetStateAction } from "react";
import { SetterOrUpdater } from "recoil";
import { Info } from "./WriteModal";

interface Props {
  basicInfo: Info;
  addInfo: Info;
  curInfo: string;
  setCurInfo: Dispatch<SetStateAction<string>>;
  setStep: SetterOrUpdater<number>;
}

const MDEditor = dynamic(() => import("@uiw/react-md-editor"), {
  ssr: false,
});

const Curriculum = ({
  basicInfo,
  addInfo,
  curInfo,
  setCurInfo,
  setStep,
}: Props) => {
  const screenWidth = useWindowSize();

  const toBack = (e: React.MouseEvent<HTMLButtonElement>) => {
    setStep(prev => prev - 1);
  };
  const handleChangeValue = (e: any) => {
    setCurInfo(e);
  };

  const onClick = () => {
    console.log(basicInfo);
    console.log(addInfo);
    console.log(curInfo);
    const { address, career, company, cost, language, profileImg, title } =
      basicInfo;
    const {
      detailCompany,
      detailCost,
      detailLocation,
      introduction,
      personality,
    } = addInfo;
    const profileImage = Array.from(profileImg as any)[0];
    console.log("profile", profileImage);
    const object = new FormData();
    const registration: any = {
      data: {
        title,
        language,
        company,
        career,
        address,
        cost,
        introduction,
        detailCompany,
        detailLocation,
        personality,
        detailCost,
        curriculum: curInfo,
      },
      profileImage,
    };
    object.append("data", registration.data);
    object.append("profileImage", registration.profileImage);

    console.log("register", registration);
    // console.log(object);
    // postWrite(object);
  };

  return (
    <>
      <div className="flex flex-col justify-center items-center w-full h-fit">
        <div className="w-fit h-fit font-SCDream5 desktop:text-sm tablet:text-sm text-[13.5px] text-pointColor">
          과외 진행방식에 대한 자세한 정보를 입력하세요
        </div>
        <div className="flex tablet:flex-row flex-col w-fit h-fit justify-center items-center">
          <div className="w-fit h-fit font-SCDream4 text-[12px] text-textColor mt-2">
            회당 시간, 수업구성, 개발환경 및 요구하는 수준 등을
          </div>
          <div className="w-fit h-fit font-SCDream4 text-[12px] text-textColor mt-2">
            적어주면 좋습니다.
          </div>
        </div>
      </div>

      <div className="w-full h-fit flex flex-row justify-start items-center font-SCDream5 text-md text-textColor mt-10">
        진행방식
      </div>
      {screenWidth > 764 ? (
        <MDEditor
          height={400}
          value={curInfo}
          onChange={handleChangeValue}
          preview="live"
          commands={[bold, italic]}
          className="flex flex-col w-full h-full mt-5"
        />
      ) : (
        <MDEditor
          height={400}
          value={curInfo}
          onChange={handleChangeValue}
          preview="edit"
          commands={[bold, italic]}
          className="flex flex-col w-full h-full mt-5"
        />
      )}
      <div className="flex flex-row justify-center items-center w-full h-fit mt-10">
        <SmallBtn onClick={toBack}>이전</SmallBtn>
        <SmallBtn onClick={onClick} css="ml-5">
          등 록
        </SmallBtn>
      </div>
    </>
  );
};

export default Curriculum;
