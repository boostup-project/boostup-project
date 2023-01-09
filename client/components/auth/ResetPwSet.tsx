import AuthBtn from "components/reuse/btn/AuthBtn";
import { ErrorMessage } from "@hookform/error-message";
import { useState } from "react";
import { useForm } from "react-hook-form";

const ResetPwSet = () => {
  const [pw, setPw] = useState<string>("");
  const [rePw, setRePw] = useState<string>("");

  const {
    handleSubmit,
    register,
    formState: { errors },
    watch
  } = useForm({mode : "onBlur"});

  const handlePwChange = (e: any) => {
    setPw(e.target.value);
  };

  const handleCheckPwChange = (e: any) => {
    setRePw(e.target.value);
  };

  const onSubmit = (e: any) => {
    console.log("Success Submit!");
  };

  const failSubmit = (e: any) => {
    console.log("Fail Submit");
  };

  return (
    <>
      <form
        onSubmit={handleSubmit(onSubmit, failSubmit)}
        className="w-full h-fit"
      >
        <div className="w-full h-fit font-SCDream5 text-sm text-textColor flex flex-col justify-center items-start mb-1">
          새 비밀번호
        </div>
        <div className="w-full h-fit font-SCDream3 text-[10px] tablet:text-[12px] text-textColor/80 flex flex-col justify-center items-start mb-1">
          영어 대/소문자, 숫자, 특수문자 포함 8글자 이상
        </div>
        <input
          type="password"
          placeholder="새로운 비밀번호를 입력하세요"
          value={pw}
          className="w-full h-fit p-2 border  border-borderColor outline-pointColor rounded-xl font-SCDream4 tablet:text-sm text-[12px] text-textColor"
          {...register("password", {
            required: "필수로 입력해야되는 값입니다.",
            onChange: handlePwChange,
            pattern: {
              value: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/gm,
              // value : /^(?=.[a-z])(?=.[A-Z])(?=.\d)(?=.[@$!%?&])[A-Za-z\d@$!%?&]{8,}$/gm,
              message: "올바르지 않은 비밀번호형식입니다.",
            },
          })}
        />
        {/* <div className="w-full h-fit font-SCDream2 text-[11px] text-pointColor flex flex-col justify-center items-end mb-1">
          영어 대/소문자, 숫자, 특수문자 포함 8글자 이상으로 입력해주세요.
        </div> */}
        <ErrorMessage
          errors={errors}
          name="password"
          render={({ message }) => {
            return (
              <div className="w-full h-fit flex flex-col justify-center items-start font-SCDream3 text-negativeMessage text-xs mt-1">
                {message}
              </div>
            );
          }}
        />

        <div className="w-full h-fit font-SCDream5 text-sm text-textColor flex flex-col justify-center items-start mb-1 mt-4">
          비밀번호 확인
        </div>
        <input
          type="password"
          placeholder="다시한번 비밀번호를 입력하세요"
          value={rePw}
          className="w-full h-fit p-2 border  border-borderColor outline-pointColor rounded-xl font-SCDream4 tablet:text-sm text-[12px] text-textColor"
          {...register("checkPassword", {
            required: "필수로 입력해야되는 값입니다.",
            onChange: handleCheckPwChange,
            validate: (val:string) => {
                if (watch('password') !== val) {
                    return "비밀번호가 일치하지 않습니다.";
                }
            }
          })}
        />
        <ErrorMessage
          errors={errors}
          name="checkPassword"
          render={({ message }) => {
            return (
              <div className="w-full h-fit flex flex-col justify-center items-start font-SCDream3 text-negativeMessage text-xs mt-1">
                {message}
              </div>
            );
          }}
        />

        <div className="w-full h-fit flex flex-col justify-center items-center mt-7">
          <AuthBtn onClick={handleSubmit}>변 경</AuthBtn>
        </div>
      </form>
    </>
  );
};

export default ResetPwSet;
