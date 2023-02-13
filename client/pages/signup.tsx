import AuthBtn from "components/reuse/btn/AuthBtn";
import AuthContainer from "components/reuse/container/AuthContainer";
import { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import { useRouter } from "next/router";
import { ErrorMessage } from "@hookform/error-message";
import Link from "next/link";
import usePostSignUp from "hooks/auth/usePostSignUp";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { useRecoilValue } from "recoil";
import { signUpErrorMessage } from "atoms/auth/authAtom";

const Signup = () => {
  const [name, setName] = useState<string>("");
  const [email, setEmail] = useState<string>("");
  const [password, setPassword] = useState<string>("");
  const [passwordCheck, setPasswordCheck] = useState<string>("");

  const Error = useRecoilValue(signUpErrorMessage);

  const router = useRouter();

  const {
    register,
    handleSubmit,
    formState: { errors },
    watch,
  } = useForm({ mode: "onBlur" });

  const { mutate, isSuccess, isError, data, error } = usePostSignUp();

  const handleNameChange = (e: any) => {
    setName(e.target.value);
  };
  const handleEmailChange = (e: any) => {
    setEmail(e.target.value);
  };
  const handlePasswordChange = (e: any) => {
    setPassword(e.target.value);
  };
  const handlePasswordCheckChange = (e: any) => {
    setPasswordCheck(e.target.value);
  };

  const onSubmit = (e: any) => {
    mutate({
      email,
      password,
      name,
    });
  };

  useEffect(() => {
    if (isSuccess) {
      toast.success("회원가입에 성공하였습니다!", {
        autoClose: 3000,
        position: toast.POSITION.TOP_RIGHT,
      });
      setTimeout(() => {
        toast.success("로그인페이지로 이동합니다!", {
          autoClose: 2000,
          position: toast.POSITION.TOP_RIGHT,
        });
      }, 1000);

      setTimeout(() => {
        router.push("/login");
      }, 3000);
    }

    if (isError) {
      toast.error(`${Error}`, {
        autoClose: 3000,
        position: toast.POSITION.TOP_RIGHT,
      });
    }
  }, [isSuccess, isError]);

  return (
    <>
      <ToastContainer />
      <div className="flex flex-col bg-bgColor items-center justify-center w-full h-screen">
        <div className="flex flex-col w-full h-fit justify-center items-center font-SCDream5 text-xl text-textColor mb-2">
          회원가입
        </div>
        <div className="flex flex-col w-full h-fit justify-center items-center font-SCDream3 text-sm text-pointColor mb-2">
          회원가입 후 원하는 선생님을 찜 해보세요!
        </div>
        <AuthContainer>
          <form onSubmit={handleSubmit(onSubmit)} className="w-full h-fit">
            <div className="flex flex-col justify-center items-start w-full h-fit font-SCDream5 text-sm text-textColor mb-1">
              닉네임
            </div>
            <input
              type="text"
              value={name}
              placeholder="닉네임을 입력하세요"
              className=" w-full h-fit p-2 border border-borderColor outline-pointColor rounded-xl font-SCDream4 text-[11px] text-textColor tablet:text-sm"
              {...register("name", {
                required: "필수로 입력해야되는 값입니다.",
                onChange: handleNameChange,
                pattern: {
                  value: /^[a-zA-Zㄱ-힣0-9|s]{3,12}$/gm,
                  message: "3자이상 숫자,영문자,한글로 작성해 주세요",
                },
              })}
            />
            <ErrorMessage
              errors={errors}
              name="name"
              render={({ message }) => {
                return (
                  <div className="flex flex-col w-full h-fit justify-center items-start font-SCDream3 text-negativeMessage text-xs mt-1">
                    {message}
                  </div>
                );
              }}
            />

            <div className="flex flex-col w-full h-fit justify-center items-start font-SCDream5 text-textColor text-sm mb-1 mt-4">
              이메일
            </div>
            <input
              type="email"
              value={email}
              placeholder="이메일을 입력하세요"
              className="w-full h-fit p-2 border border-borderColor outline-pointColor rounded-xl font-SCDream4 text-textColor text-[11px] tablet:text-sm"
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
                  <div className="w-full h-fit flex flex-col justify-center items-start text-SCDream3 text-negativeMessage text-xs mt-1">
                    {message}
                  </div>
                );
              }}
            />

            <div className="flex flex-col justify-center items-start w-full h-fit font-SCDream5 text-sm text-textColor mb-1 mt-4">
              비밀번호
            </div>
            <input
              type="password"
              value={password}
              placeholder="비밀번호를 입력하세요"
              className="w-full h-fit p-2 border border-borderColor outline-pointColor rounded-xl font-SCDream4 text-[11px] text-textColor tablet:text-sm"
              {...register("password", {
                required: "필수로 입력해야되는 값입니다.",
                onChange: handlePasswordChange,
                pattern: {
                  value:
                    /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/gm,
                  message:
                    "최소 8 자, 하나 이상의 대문자, 하나 이상의 소문자, 하나의 숫자 및 하나의 특수 문자를 포함합니다",
                },
              })}
            />
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

            <div className="flex flex-col justify-center items-start w-full h-fit font-SCDream5 text-sm text-textColor mb-1 mt-4">
              비밀번호 확인
            </div>
            <input
              type="password"
              value={passwordCheck}
              placeholder="동일한 비밀번호를 입력하세요"
              className="w-full h-fit p-2 border border-borderColor outline-pointColor rounded-xl font-SCDream4 text-[11px] text-textColor tablet:text-sm"
              {...register("passwordCheck", {
                required: "필수로 입력해야되는 값입니다.",
                onChange: handlePasswordCheckChange,
                // pattern: {
                //   value: /^(?=.\d)(?=.[a-z])(?=.[A-Z])(?=.[a-zA-Z]).{8,}$/gm,
                //   message: "동일한 비밀번호를 입력하세요",
                // },
                validate: (val: string) => {
                  if (watch("password") !== val) {
                    return "비밀번호가 일치하지 않습니다.";
                  }
                },
              })}
            />
            <ErrorMessage
              errors={errors}
              name="passwordCheck"
              render={({ message }) => {
                return (
                  <div className="w-full h-fit flex flex-col justify-center items-start font-SCDream3 text-negativeMessage text-xs mt-1">
                    {message}
                  </div>
                );
              }}
            />

            <div className="flex flex-col w-full h-fit justify-center items-center mt-7">
              <AuthBtn onClick={handleSubmit}>회원가입</AuthBtn>
            </div>

            <div className="w-full h-fit flex flex-col justify-center items-center text-pointColor mt-7">
              <Link href="/login">이미 가입하셨나요?</Link>
            </div>
          </form>
        </AuthContainer>
      </div>
    </>
  );
};
export default Signup;
