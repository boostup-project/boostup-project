import ModalBackDrop from "components/reuse/container/ModalBackDrop";
import CreateModalContainer from "components/reuse/container/CreateModalContainer";
import BasicStep from "components/reuse/step/BasicStep";
import BasicInfo from "components/createModal/BasicInfo";
import { powerBasicEditModal } from "atoms/detail/detailAtom";
import { inputStep } from "atoms/main/mainAtom";
import { useRecoilState } from "recoil";
import { useState } from "react";

interface ObjectPart {
  [index: string]: string | string[];
}

export interface Info {
  [index: string]: string | string[] | ObjectPart;
}

const DetailBasicInfoEditModal = (basicData: any) => {
  const [basicInfo, setBasicInfo] = useState<Info>(basicData.basicData);
  const [isPowerModal, setIsPowerModal] = useRecoilState(powerBasicEditModal);
  const [step, setStep] = useRecoilState(inputStep);

  const toWrite = () => {
    setIsPowerModal(prev => !prev);
  };

  return (
    <>
      <ModalBackDrop onClick={() => {}}>
        <CreateModalContainer>
          <div className="w-fit h-fit mb-5">
            <BasicStep />
          </div>
          <BasicInfo
            basicInfo={basicData.basicData.data}
            setBasicInfo={setBasicInfo}
            toWrite={toWrite}
            setStep={setStep}
          />
        </CreateModalContainer>
      </ModalBackDrop>
    </>
  );
};

export default DetailBasicInfoEditModal;
