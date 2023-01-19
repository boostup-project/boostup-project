import ModalBackDrop from "components/reuse/container/ModalBackDrop";
import StepNavWrapper from "components/reuse/container/StepNavWrapper";
import CreateModalContainer from "components/reuse/CreateModalContainer";
import Additional from "./Additional";
import BasicInfo from "./BasicInfo";
import Curriculum from "./Curriculum";
import { inputStep, isWriteModal } from "atoms/main/mainAtom";
import { useRecoilState, useRecoilValue, useSetRecoilState } from "recoil";
import { useState } from "react";

interface ObjectPart {
  [index: string]: string | string[];
}

export interface Info {
  [index: string]: string | string[] | ObjectPart;
}

const WriteModal = () => {
  const [basicInfo, setBasicInfo] = useState<Info>({});
  const [addInfo, setAddInfo] = useState<Info>({});
  const [curInfo, setCurInfo] = useState("");
  const [step, setStep] = useRecoilState(inputStep);
  const setIsWrite = useSetRecoilState(isWriteModal);
  const toWrite = () => {
    setIsWrite(prev => !prev);
  };

  console.log("basic", basicInfo);
  console.log("add", addInfo);
  console.log("cur", curInfo);

  return (
    <ModalBackDrop onClick={toWrite}>
      <CreateModalContainer>
        <StepNavWrapper />
        {step === 1 && (
          <BasicInfo
            basicInfo={basicInfo}
            setBasicInfo={setBasicInfo}
            toWrite={toWrite}
            setStep={setStep}
          />
        )}
        {step === 2 && (
          <Additional
            addInfo={addInfo}
            setAddInfo={setAddInfo}
            setStep={setStep}
          />
        )}
        {step === 3 && (
          <Curriculum
            basicInfo={basicInfo}
            addInfo={addInfo}
            curInfo={curInfo}
            setCurInfo={setCurInfo}
            setStep={setStep}
          />
        )}
      </CreateModalContainer>
    </ModalBackDrop>
  );
};
export default WriteModal;
