import CreateModalContainer from "./reuse/container/CreateModalContainer";
import { useForm, Controller } from "react-hook-form";
import { langDict, dayDict } from "./reuse/dict";
import SmallBtn from "./reuse/btn/SmallBtn";

interface BasicInfo {
  [index: string]: string | string[];
}
interface Props {
  toWrite: () => void;
}

const ApplyModal = ({ toWrite }: Props) => {
  const {
    control,
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<BasicInfo>({ mode: "onBlur" });

  const langArr = Object.keys(langDict);
  const dayArr = Object.keys(dayDict);
  return (
    <>
      <CreateModalContainer>
        <div className="flex w-full desktop:w-4/6 h-fit font-SCDream7 text-lg text-textColor mt-4 mb-2">
          신청하기
        </div>
        <div className="flex w-full desktop:w-4/6 h-fit font-SCDream5 text-lg text-textColor mt-4 mb-2">
          <div>희망요일</div>
          <div className="text-pointColor">*</div>{" "}
          <div className="text-pointColor text-xs">3개까지 선택가능합니다</div>
        </div>
        <div className="w-11/12 list h-fit flex flex-row items-start desktop:w-4/6">
          {dayArr.map((el: string, idx: number) => {
            if (idx < 4) {
              return (
                <div key={idx} className="w-1/4 h-fit text-xs">
                  <label className="checkboxLabel">
                    <input
                      type="checkbox"
                      value={el}
                      {...register("day", { required: "true" })}
                    />
                    {el}
                  </label>
                </div>
              );
            }
          })}
        </div>
        <div className="w-11/12 list h-fit flex flex-row items-start mt-3 desktop:w-4/6">
          {dayArr.map((el: string, idx: number) => {
            if (idx > 3) {
              return (
                <div key={idx} className="w-1/4 h-fit text-xs">
                  <label className="checkboxLabel">
                    <input
                      type="checkbox"
                      value={el}
                      {...register("day", { required: "true" })}
                    />
                    {el}
                  </label>
                </div>
              );
            }
          })}
        </div>
        <p className="w-11/12 text-xs text-negativeMessage mt-1 tablet:text-sm desktop:w-4/6">
          {errors?.language && <span>필수 정보입니다</span>}
        </p>
        <div className="flex w-full desktop:w-4/6 h-fit font-SCDream5 text-lg text-textColor mt-4 mb-2">
          <div>희망언어</div>
          <div className="text-pointColor">*</div>{" "}
          <div className="text-pointColor text-xs">3개까지 선택가능합니다</div>
        </div>
        <div className="w-11/12 list h-fit flex flex-row items-start desktop:w-4/6">
          {langArr.map((el: string, idx: number) => {
            if (idx < 4) {
              return (
                <div key={idx} className="w-1/4 h-fit text-xs">
                  <label className="checkboxLabel">
                    <input
                      type="checkbox"
                      value={el}
                      {...register("language", { required: "true" })}
                    />
                    {el}
                  </label>
                </div>
              );
            }
          })}
        </div>
        <div className="w-11/12 list h-fit flex flex-row items-start mt-3 desktop:w-4/6">
          {langArr.map((el: string, idx: number) => {
            if (idx > 3) {
              return (
                <div key={idx} className="w-1/4 h-fit text-xs">
                  <label className="checkboxLabel">
                    <input
                      type="checkbox"
                      value={el}
                      {...register("language", { required: "true" })}
                    />
                    {el}
                  </label>
                </div>
              );
            }
          })}
        </div>
        <p className="w-11/12 text-xs text-negativeMessage mt-1 tablet:text-sm desktop:w-4/6">
          {errors?.language && <span>필수 정보입니다</span>}
        </p>
        <div className="flex w-full desktop:w-4/6 h-fit font-SCDream5 text-lg text-textColor mt-4 mb-2">
          요청사항
        </div>
        <input
          type="text"
          placeholder="요청사항을 입력하세요"
          className="w-11/12 desktop:w-4/6 h-fit p-2 border border-borderColor outline-pointColor rounded-xl font-SCDream4 text-xs text-textColor tablet:text-sm "
        />
        <div className="flex flex-row justify-center items-center w-full h-fit mt-10">
          <SmallBtn onClick={toWrite}>취 소</SmallBtn>
          <SmallBtn css="ml-5">신 청</SmallBtn>
        </div>
      </CreateModalContainer>
    </>
  );
};

export default ApplyModal;
