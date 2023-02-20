import AuthBtn from "components/reuse/btn/AuthBtn";
import { ErrorMessage } from "@hookform/error-message";
import { useForm } from "react-hook-form";
import usePostAuthPw from "hooks/auth/usePostEmailAuth";
import usePostCheckEmail from "hooks/auth/usePostCheckEmail";
import { useRecoilState } from "recoil";
import { findPwEmail, resetPwStep } from "../../atoms/auth/authAtom";
import { useEffect } from "react";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

const ResetPwEmailAuth = () => {
  const [email, setEmail] = useRecoilState<string>(findPwEmail);
  const [step, setStep] = useRecoilState(resetPwStep);

  const {
    handleSubmit,
    register,
    formState: { errors },
  } = useForm({ mode: "onBlur" });

  const { mutate: requestCode } = usePostAuthPw();
  const { mutate: checkEmail, isSuccess, isError } = usePostCheckEmail();

  const handleEmailChange = (e: any) => {
    setEmail(e.target.value);
  };

  const onSubmit = (e: any) => {
    checkEmail({ email });
    // requestCode({ email });
  };

  const failSubmit = (e: any) => {
    console.log("Fail Submit");
  };

  useEffect(() => {
    if (isSuccess) {
      toast.success("인증번호를 발송하였습니다", {
        autoClose: 1500,
        position: toast.POSITION.TOP_RIGHT,
      });
      setStep(2);
      setTimeout(() => {
        requestCode({ email });
      }, 1500);
    }

    if (isError) {
      toast.error("등록되지 않은 이메일입니다", {
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
          <AuthBtn onClick={handleSubmit}>인증요청</AuthBtn>
        </div>
      </form>
    </>
  );
};

export default ResetPwEmailAuth;
