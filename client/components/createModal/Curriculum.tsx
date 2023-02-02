import "@uiw/react-md-editor/markdown-editor.css";
import "@uiw/react-markdown-preview/markdown.css";
import SmallBtn from "components/reuse/btn/SmallBtn";
import dynamic from "next/dynamic";
import useWindowSize from "hooks/useWindowSize";
import { bold, italic } from "@uiw/react-md-editor/lib/commands/";
import { Dispatch, SetStateAction } from "react";
import { SetterOrUpdater, useSetRecoilState } from "recoil";
import { Info } from "./WriteModal";
import { langDict } from "components/reuse/dict";
import usePostWrite from "hooks/main/usePostWrite";
import { useEffect } from "react";
import { isWrite } from "atoms/main/mainAtom";
import { toast, ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

interface Props {
  basicInfo: Info;
  extraInfo: Info;
  curInfo: string;
  setCurInfo: Dispatch<SetStateAction<string>>;
  setStep: SetterOrUpdater<number>;
  setIsPowerWrite: SetterOrUpdater<boolean>;
}

interface BasicSubmit {
  [index: string]: any;
}

const MDEditor = dynamic(() => import("@uiw/react-md-editor"), {
  ssr: false,
});

const Curriculum = ({
  basicInfo,
  extraInfo,
  curInfo,
  setCurInfo,
  setStep,
  setIsPowerWrite,
}: Props) => {
  const setIsWritten = useSetRecoilState(isWrite);
  const { mutate, isSuccess, isError } = usePostWrite();
  const screenWidth = useWindowSize();
  const toBack = (e: React.MouseEvent<HTMLButtonElement>) => {
    setStep(prev => prev - 1);
  };
  const handleChangeValue = (e: any) => {
    setCurInfo(e);
  };

  useEffect(() => {
    if (isSuccess) {
      setIsPowerWrite(prev => !prev);
      setIsWritten(prev => !prev);
      setStep(prev => prev - 2);
    }
  }),
    [isSuccess, isError];

  const onClick = () => {
    const {
      address,
      career,
      company,
      cost,
      languages,
      profileImg,
      title,
    }: BasicSubmit = basicInfo;
    const {
      detailCompany,
      detailCost,
      detailLocation,
      detailImage,
      introduction,
      personality,
    } = extraInfo;
    const proImage = profileImg[0];
    const parseAddress = address.map((el: any) => el.value);
    const parseLang = languages.map((el: any) => langDict[el]);
    const pre = {
      title,
      language: parseLang,
      company,
      career: parseInt(career),
      address: parseAddress,
      cost: parseInt(cost),
      introduction,
      detailCompany,
      detailLocation,
      personality,
      detailCost,
      curriculum: curInfo,
    };

    const json = JSON.stringify({
      title,
      languages: parseLang,
      company,
      career,
      address: parseAddress,
      cost,
      introduction,
      detailCompany,
      detailLocation,
      personality,
      detailCost,
      curriculum: curInfo,
    });
    const blob = new Blob([json], { type: "application/json" });
    const formData = new FormData();
    formData.append("data", blob);
    formData.append("profileImage", proImage);
    formData.append("careerImage", proImage);
    mutate(formData);
  };

  return (
    <>
      <ToastContainer />
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
        <div data-color-mode="light" className="w-full">
          <MDEditor
            height={400}
            value={curInfo}
            onChange={handleChangeValue}
            preview="live"
            commands={[bold, italic]}
            className="flex flex-col w-full h-full mt-5"
          />
        </div>
      ) : (
        <div data-color-mode="light" className="w-full">
          <MDEditor
            height={400}
            value={curInfo}
            onChange={handleChangeValue}
            preview="edit"
            commands={[bold, italic]}
            className="flex flex-col w-full h-full mt-5"
          />
        </div>
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
