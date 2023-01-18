import { inputStep, isWriteModal } from "atoms/main/mainAtom";
import StepNavWrapper from "components/reuse/container/StepNavWrapper";
import CreateModalContainer from "components/reuse/CreateModalContainer";
import { useEffect } from "react";
import { useRecoilValue, useSetRecoilState } from "recoil";
import Additional from "./Additional";
import BasicInfo from "./BasicInfo";
import Curriculum from "./Curriculum";

const WriteModal = () => {
  const step = useRecoilValue(inputStep);
  const setIsWrite = useSetRecoilState(isWriteModal);
  const toWrite = (e: React.MouseEvent<HTMLDivElement>) => {
    setIsWrite(prev => !prev);
  };
  useEffect(() => {
    document.body.style.cssText = `
    overflow: hidden;
    width: 100%;`;
    return () => {
      document.body.style.cssText = "overflow: unset;";
    };
  }, []);
  return (
    <div
      className="fixed z-[999] top-0 left-0 bottom-0 right-0 bg-modalBgColor grid place-items-center"
      onClick={e => toWrite(e)}
    >
      <CreateModalContainer>
        <StepNavWrapper />
        {step === 1 && <BasicInfo />}
        {step === 2 && <Additional />}
        {step === 3 && <Curriculum />}
      </CreateModalContainer>
    </div>
  );
};
export default WriteModal;