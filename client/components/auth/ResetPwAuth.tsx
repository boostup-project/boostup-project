import AuthBtn from "components/reuse/AuthBtn";
import { ErrorMessage } from "@hookform/error-message";
import { Dispatch, useState, SetStateAction } from "react";
import { useForm } from "react-hook-form";
import usePostAuthPw from "hooks/auth/usePostAuthPw";
import { useRecoilState } from "recoil";
import { findPwEmail } from "../../atoms/auth/authAtom";

interface Props {
    setIsAuth : Dispatch<SetStateAction<boolean>>
}

const ResetPwAuth = ({setIsAuth}:Props) => {
  const [name, setName] = useState<string>("");
  const [email, setEmail] = useRecoilState<string>(findPwEmail);

  const {
    handleSubmit,
    register,
    formState: { errors },
  } = useForm({mode : "onBlur"});

  const { mutate } = usePostAuthPw();

  const handleNameChange = (e: any) => {
    setName(e.target.value);
  };

  const handleEmailChange = (e: any) => {
    setEmail(e.target.value);
  };

  const onSubmit = (e: any) => {
    console.log("Success Submit!");
    mutate({name, email});
    setIsAuth(true);
  };

  const failSubmit = (e: any) => {
    console.log("Fail Submit");
    setIsAuth(false);
  };

  return (
    <>
      <form
        onSubmit={handleSubmit(onSubmit, failSubmit)}
        className="w-full h-fit"
      >
        <div className="w-full h-fit font-SCDream5 text-sm text-textColor flex flex-col justify-center items-start mb-1">
          닉네임
        </div>
        <input
          type="text"
          placeholder="찾고자 하는 계정의 닉네임을 입력해주세요"
          value={name}
          className="w-full h-fit p-2 border  border-borderColor outline-pointColor rounded-xl font-SCDream4 tablet:text-sm text-[12px] text-textColor"
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
              <div className="w-full h-fit flex flex-col justify-center items-start font-SCDream3 text-negativeMessage text-xs mt-1">
                {message}
              </div>
            );
          }}
        />

        <div className="w-full h-fit font-SCDream5 text-sm text-textColor flex flex-col justify-center items-start mb-1 mt-4">
          이메일
        </div>
        <input
          type="text"
          placeholder="찾고자 하는 계정의 이메일을 입력해주세요"
          value={email}
          className="w-full h-fit p-2 border  border-borderColor outline-pointColor rounded-xl font-SCDream4 tablet:text-sm text-[12px] text-textColor"
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
              <div className="w-full h-fit flex flex-col justify-center items-start font-SCDream3 text-negativeMessage text-xs mt-1">
                {message}
              </div>
            );
          }}
        />
        <div className="w-full h-fit flex flex-col justify-center items-center mt-7">
          <AuthBtn onClick={handleSubmit}>다 음</AuthBtn>
        </div>
      </form>
    </>
  );
};

export default ResetPwAuth;
