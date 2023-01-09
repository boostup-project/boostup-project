import { useForm, Controller } from "react-hook-form";
import { langDict, addDict } from "../reuse/dict";
import AuthBtn from "../reuse/AuthBtn";
import { useState, useMemo } from "react";
import Select from "react-select";

interface Props {
  onSubmit: any;
  absolute?: boolean;
}

export const SearchPop = ({ onSubmit, absolute }: Props) => {
  const { control, register, handleSubmit } = useForm();
  const [isAdd, setIsAdd] = useState(false);
  const [isLang, setIsLang] = useState(false);

  const langArr = Object.keys(langDict);
  const addArr = Object.keys(addDict);

  return (
    <div
      className={`bg-white desktop:absolute tablet:top-[8px] desktop:top-[86px] desktop:w-full h-fit border border-borderColor rounded-br-xl rounded-bl-xl`}
    >
      <form
        className="flex flex-col justify-center items-center"
        onSubmit={handleSubmit(onSubmit)}
      >
        <label className="w-full  flex flex-col items-center mt-5">
          <div className="text-textColor text-center">닉네임</div>
          <input
            className="desktop:w-8/12 w-5/6 h-fit p-2 border  border-borderColor outline-pointColor rounded-xl font-SCDream2 text-sm text-textColor placeholder:text-center mt-5"
            type="text"
            placeholder="원하는 과외쌤의 닉네임을 입력학세요"
            {...register("name")}
          />
        </label>
        <label className="w-full flex flex-col items-center mt-4">
          <div className="text-textColor text-center">경력</div>
          <input
            className="desktop:w-8/12 w-5/6 h-fit p-2 border  border-borderColor outline-pointColor rounded-xl font-SCDream2 text-sm text-textColor placeholder:text-center mt-5"
            type="text"
            placeholder="원하는 과외쌤의 경력을 숫자로 입력하세요"
            {...register("carrer")}
          />
        </label>
        <label className="w-full flex flex-col items-center mt-5">
          <div className="text-textColor text-center">과외가능지역</div>
          <div className="desktop:w-8/12 w-5/6 flex justify-between items-center">
            <div className="w-5/12 text-center flex flex-col even:items-end">
              <div>시,도</div>
              <div className="w-full h-fit p-2 border border-borderColor outline-pointColor rounded-xl font-SCDream2 text-sm text-textColor bg-white text-center mt-5">
                <span>서울특별시</span>
              </div>
            </div>
            <div className="w-5/12 text-center flex flex-col items-center">
              <div>구</div>
              <div className="w-full h-fit rounded-xl mt-5">
                <Controller
                  name="language"
                  render={({ field }) => (
                    <Select
                      {...field}
                      placeholder={<div>구 선택</div>}
                      styles={{
                        control: (baseStyles, state) => ({
                          ...baseStyles,
                          borderRadius: "12px",
                          fontSize: "14px",
                          fontFamily: "SCDream2",
                          borderColor: "#A8A7A7",
                          borderWidth: "1px",
                        }),
                        menu: base => ({
                          ...base,
                          fontFamily: "SCDream2",
                          fontSize: "14px",
                        }),
                      }}
                      options={[
                        ...addArr.map((el, idx) => ({
                          value: addDict[el],
                          label: el,
                          key: idx,
                        })),
                      ]}
                    />
                  )}
                  control={control}
                  defaultValue=""
                />
              </div>
            </div>
          </div>
        </label>
        <label className="desktop:w-8/12 w-5/6 text-center flex flex-col items-center mt-5">
          <div className="w-full text-textColor text-center">개발언어</div>
          <div className="w-full h-fit rounded-xl mt-5">
            <Controller
              name="address"
              render={({ field }) => (
                <Select
                  {...field}
                  placeholder={<div>언어 선택</div>}
                  styles={{
                    control: (baseStyles, state) => ({
                      ...baseStyles,
                      borderRadius: "12px",
                      fontSize: "14px",
                      fontFamily: "SCDream2",
                      borderColor: "#A8A7A7",
                      borderWidth: "1px",
                    }),
                    menu: base => ({
                      ...base,
                      fontFamily: "SCDream2",
                      fontSize: "14px",
                    }),
                  }}
                  options={[
                    ...langArr.map((el, idx) => ({
                      value: langDict[el],
                      label: el,
                      key: idx,
                    })),
                  ]}
                />
              )}
              control={control}
              defaultValue=""
            />
          </div>
        </label>
        <label className="w-full flex flex-col items-center mt-5">
          <div className="text-textColor text-center">가격</div>
          <div className="desktop:w-8/12 w-5/6 flex justify-between items-center">
            <div className="w-5/12 text-center flex flex-col items-center">
              <span>최소금액</span>
              <input
                className="w-full h-fit p-2 border  border-borderColor outline-pointColor rounded-xl font-SCDream2 text-sm text-textColor placeholder:text-center mt-5"
                type="text"
                placeholder="최소금액"
                {...register("startCost")}
              />
            </div>
            <div className="flex flex-col justify-end pb-1">
              <div>~</div>
            </div>
            <div className="w-5/12 text-center flex flex-col items-center">
              <span>최대금액</span>
              <input
                className="w-full h-fit p-2 border  border-borderColor outline-pointColor rounded-xl font-SCDream2 text-sm text-textColor placeholder:text-center mt-4"
                type="text"
                placeholder="최대금액"
                {...register("endCost")}
              />
            </div>
          </div>
        </label>
        <div className="w-full h-fit mt-5 mb-4 flex justify-center items-center">
          <AuthBtn>검색</AuthBtn>
        </div>
      </form>
    </div>
  );
};
