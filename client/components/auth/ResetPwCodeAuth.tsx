import AuthBtn from "components/reuse/btn/AuthBtn";
import { ErrorMessage } from "@hookform/error-message";
import { Dispatch, useState, SetStateAction, useEffect } from "react";
import { useForm } from "react-hook-form";
import { useRecoilState } from "recoil";
import { findPwEmail } from "../../atoms/auth/authAtom";
import usePostCodeAuth from "hooks/auth/usePostCodeAuth";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

const ResetPwCodeAuth = () => {
  const [email, setEmail] = useRecoilState<string>(findPwEmail);
  const [code, setCode] = useState<string>("");

  const {
    handleSubmit,
    register,
    formState: { errors },
  } = useForm({ mode: "onBlur" });

  const { mutate, isSuccess, isError } = usePostCodeAuth();

  const handleCodeChange = (e: any) => {
    setCode(e.target.value);
  };

  const onSubmit = (e: any) => {
    mutate({ email, code });
  };

  const failSubmit = (e: any) => {
    console.log("Fail Submit");
  };

  useEffect(() => {
    if (isSuccess) {
      toast.success("인증에 성공하였습니다!", {
        autoClose: 3000,
        position: toast.POSITION.TOP_RIGHT,
      });
    }

    if (isError) {
      toast.error("올바르지 않은 인증코드입니다", {
        autoClose: 3000,
        position: toast.POSITION.TOP_RIGHT,
      });
    }
  }, [isSuccess, isError]);

  return (
    <>
      <ToastContainer />
      <form
        onSubmit={handleSubmit(onSubmit, failSubmit)}
        className="w-full h-fit"
      >
        <div className="w-full h-fit font-SCDream5 text-sm text-textColor flex flex-col justify-center items-start mb-1 mt-4">
          인증코드
        </div>
        <div className="w-full h-fit font-SCDream3 text-[10px] tablet:text-[12px] text-textColor/80 flex flex-col justify-center items-start mb-2">
          {`${email}로 인증코드가 발송되었습니다`}
        </div>
        <input
          type="text"
          placeholder="인증코드를 입력하세요"
          value={code}
          className="w-full h-fit p-2 border  border-borderColor outline-pointColor rounded-xl font-SCDream4 tablet:text-sm text-[12px] text-textColor"
          {...register("code", {
            required: "필수로 입력해야되는 값입니다.",
            onChange: handleCodeChange,
          })}
        />
        <ErrorMessage
          errors={errors}
          name="code"
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

export default ResetPwCodeAuth;
