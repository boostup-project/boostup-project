import { inputStep } from "atoms/main/mainAtom";
import { useRecoilState } from "recoil";
const BasicStep = () => {
  const [step, setStep] = useRecoilState<number>(inputStep);

  const handleClickStep = () => {
    setStep(1);
  };

  return (
    <>
      <div
        className="w-fit h-fit flex flex-col justify-center items-center cursor-pointer"
        onClick={handleClickStep}
      >
        {step === 1 ? (
          <div className="w-[12px] h-[12px] bg-pointColor rounded-xl"></div>
        ) : (
          <div className="w-[12px] h-[12px] bg-borderColor rounded-xl"></div>
        )}

        <div className="w-fit h-fit font-SCDream4 text-md text-textColor mt-3">
          기본정보
        </div>
      </div>
    </>
  );
};

export default BasicStep;
