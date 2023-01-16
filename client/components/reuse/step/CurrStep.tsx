import { inputStep } from "atoms/main/mainAtom";
import { useRecoilValue } from "recoil";

const CurrStep = () => {
  const step = useRecoilValue<number>(inputStep);

  return (
    <>
      <div className="w-fit h-fit flex flex-col justify-center items-center cursor-pointer">
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
