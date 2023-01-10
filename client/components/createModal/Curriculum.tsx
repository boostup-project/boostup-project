import SmallBtn from "components/reuse/btn/SmallBtn";

const Curriculum = () => {
  return (
    <>
      <div className="flex flex-col justify-center items-center w-full h-fit">
        <div className="w-fit h-fit font-SCDream5 text-md text-pointColor">
          등록할 과외의 진행방식에 대한 자세한 정보를 입력하세요
        </div>
        <div className="w-fit h-fit font-SCDream4 text-xs text-textColor mt-2">
          회당 시간, 수업구성, 개발환경 및 요구하는 수준 등을 적어주면 좋습니다.
        </div>
      </div>

      <div className="w-full h-fit flex flex-row justify-start items-center font-SCDream5 text-md text-textColor mt-10">
        진행방식
      </div>
      <div className="w-full min-h-[500px] h-fit flex flex-row justify-center items-center rounded-xl border border-borderColor mt-3">
        hello
      </div>
      <div className="flex flex-row justify-center items-center w-full h-fit mt-10">
        <SmallBtn>취 소</SmallBtn>
        <SmallBtn css="ml-5">등 록</SmallBtn>
      </div>
    </>
  );
};

export default Curriculum;
