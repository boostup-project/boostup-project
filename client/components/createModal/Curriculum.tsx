import SmallBtn from "components/reuse/btn/SmallBtn";
import { useState } from "react";
// import "../../styles/markdown.css"

import "@uiw/react-md-editor/markdown-editor.css";
import "@uiw/react-markdown-preview/markdown.css";
import { bold, italic } from "@uiw/react-md-editor/lib/commands/";
import dynamic from "next/dynamic";
import useWindowSize from "hooks/useWindowSize";
import { currSave, inputStep } from "atoms/main/mainAtom";
import { useRecoilState } from "recoil";

const MDEditor = dynamic(() => import("@uiw/react-md-editor"), {
  ssr: false,
});

const Curriculum = () => {
  const [value, setValue] = useState<string>("");
  const [step, setStep] = useRecoilState(inputStep);
  const [curr, setCurr] = useRecoilState(currSave);
  // const [toolbar, setToolbar] = useState<boolean>(true);

  const screenWidth = useWindowSize();

  const toBack = (e: React.MouseEvent<HTMLButtonElement>) => {
    setStep(step - 1);
  };
  const handleChangeValue = (e: any) => {
    setValue(e);
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
          value={value}
          onChange={handleChangeValue}
          preview="live"
          commands={[bold, italic]}
          className="flex flex-col w-full h-full mt-5"
        />
      ) : (
        <MDEditor
          height={400}
          value={value}
          onChange={handleChangeValue}
          preview="edit"
          commands={[bold, italic]}
          className="flex flex-col w-full h-full mt-5"
        />
      )}
      <div className="flex flex-row justify-center items-center w-full h-fit mt-10">
        <SmallBtn onClick={toBack}>이전</SmallBtn>
        <SmallBtn css="ml-5">등 록</SmallBtn>
      </div>
    </>
  );
};

export default Curriculum;
