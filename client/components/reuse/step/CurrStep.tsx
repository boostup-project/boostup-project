import { inputStep } from "atoms/main/mainAtom";
import { useRecoilState } from "recoil";

const CurrStep = () => {
  const [step, setStep] = useRecoilState<number>(inputStep);

  const handleClickStep = () => {
    setStep(3);
  };

  return (
    <>
      <div className="w-fit h-fit flex flex-col justify-center items-center cursor-pointer"
        onClick={handleClickStep}
      >
        {step === 3 ? (
          <div className="w-[12px] h-[12px] bg-pointColor rounded-xl"></div>
        ) : (
          <div className="w-[12px] h-[12px] bg-borderColor rounded-xl"></div>
        )}
        <div className="w-fit h-fit font-SCDream4 text-md text-textColor mt-3">
          진행방식
        </div>
      </div>
    </>
  );
};

export default CurrStep;
