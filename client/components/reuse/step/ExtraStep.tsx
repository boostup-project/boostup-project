import { inputStep } from "atoms/main/mainAtom";
import { useRecoilState } from "recoil";

const ExtraStep = () => {
  const [step, setStep] = useRecoilState<number>(inputStep);
  
  const handleClickStep = () => {
    setStep(2);
  };

  return (
    <>
      <div className="w-fit h-fit flex flex-col justify-center items-center mx-8 cursor-pointer"
        onClick={handleClickStep}
      >
        {step === 2 ? (
          <div className="w-[12px] h-[12px] bg-pointColor rounded-xl"></div>
        ) : (
          <div className="w-[12px] h-[12px] bg-borderColor rounded-xl"></div>
        )}
        <div className="w-fit h-fit font-SCDream4 text-md text-textColor mt-3">
          추가정보
        </div>
      </div>
    </>
  );
};

export default ExtraStep;
