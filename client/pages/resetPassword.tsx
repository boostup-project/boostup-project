import AuthContainer from "components/reuse/AuthContainer";
import InputBox from "components/reuse/InputBox";
import AuthBtn from "components/reuse/AuthBtn";
import NewAuthBtn from "components/reuse/NewAuthBtn";
import FormInputBox from "components/reuse/FormInputBox";
import { ErrorMessage } from "@hookform/error-message";
import { useState } from "react";
import { useForm } from "react-hook-form";

const ResetPassword = () => {
  const [name, setName] = useState<string>("");
  const [email, setEmail] = useState<string>("");

  const {
    handleSubmit,
    register,
    formState: { errors },
  } = useForm();

  const handleNameChange = (e: any) => {
    setName(e.target.value);
  };

  const handleEmailChange = (e: any) => {
    setEmail(e.target.value);
  };

  const onSubmit = (e: any) => {
    console.log("Success Submit!");
  };

  const failSubmit = (e: any) => {
    console.log("Fail Submit");
  };

  return (
    <>
      <div className="flex flex-col bg-bgColor items-center justify-center w-full h-screen">
        <div className="flex flex-col w-full h-fit justify-center items-center font-SCDream5 text-xl text-textColor mb-2">
          비밀번호 변경
        </div>
        <div className="flex flex-col w-full h-fit justify-center items-center font-SCDream3 text-sm text-pointColor mb-10">
          본인인증 후 비밀번호를 재설정하세요!
        </div>
        <AuthContainer>
          <form
            onSubmit={handleSubmit(onSubmit, failSubmit)}
            className="w-full h-fit"
          >
            <div className="w-full h-fit font-SCDream3 text-sm text-textColor flex flex-col justify-center items-start mb-1">
              닉네임
            </div>
            <input
              type="text"
              placeholder="찾고자 하는 계정의 닉네임을 입력해주세요"
              value={name}
              className="w-full h-fit p-2 border  border-borderColor outline-pointColor rounded-md font-SCDream2 tablet:text-sm text-[11px] text-textColor"
              {...register("nickname", {
                required: "필수로 입력해야되는 값입니다.",
                onChange: handleNameChange,
                pattern: {
                  value: /^[a-z0-9]{5,12}$/,
                  message: "올바르지 않은 닉네임형식입니다.",
                },
              })}
            />
            <ErrorMessage
              errors={errors}
              name="nickname"
              render={({ message }) => {
                return (
                  <div className="w-full h-fit flex flex-col justify-center items-end font-SCDream2 text-negativeMessage text-xs mt-1">
                    {message}
                  </div>
                );
              }}
            />

            <div className="w-full h-fit font-SCDream3 text-sm text-textColor flex flex-col justify-center items-start mb-1 mt-4">
              이메일
            </div>
            <input
              type="text"
              placeholder="찾고자 하는 계정의 이메일을 입력해주세요"
              value={email}
              className="w-full h-fit p-2 border  border-borderColor outline-pointColor rounded-md font-SCDream2 tablet:text-sm text-[11px] text-textColor"
              {...register("email", {
                required: "필수로 입력해야되는 값입니다.",
                onChange: handleEmailChange,
                pattern: {
                  value: /\b[\w\.-]+@[\w\.-]+\.\w{2,4}\b/gi,
                  message: "올바르지 않은 이메일형식입니다.",
                },
              })}
            />
            <ErrorMessage
              errors={errors}
              name="email"
              render={({ message }) => {
                return (
                  <div className="w-full h-fit flex flex-col justify-center items-end font-SCDream2 text-negativeMessage text-xs mt-1">
                    {message}
                  </div>
                );
              }}
            />
            <div className="w-full h-fit flex flex-col justify-center items-center mt-7">
              <AuthBtn onClick={handleSubmit}>
                다 음
              </AuthBtn>
            </div>
          </form>
        </AuthContainer>
      </div>
    </>
  );
};
export default ResetPassword;
