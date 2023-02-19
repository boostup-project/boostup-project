import AuthContainer from "components/reuse/container/AuthContainer";
import ResetPwEmailAuth from "components/auth/ResetPwEmailAuth";
import ResetPwSet from "components/auth/ResetPwSet";
import ResetPwCodeAuth from "components/auth/ResetPwCodeAuth";
import { useRecoilState } from "recoil";
import { resetPwStep, findPwEmail } from "atoms/auth/authAtom";
import { useEffect } from "react";

const ResetPassword = () => {
  const [step, setStep] = useRecoilState(resetPwStep);
  const [email, setEmail] = useRecoilState(findPwEmail);

  useEffect(() => {
    return () => {
      setStep(1);
      setEmail("");
    };
  }, []);
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
          {step === 1 ? (
            <ResetPwEmailAuth />
          ) : step === 2 ? (
            <ResetPwCodeAuth />
          ) : (
            <ResetPwSet />
          )}
        </AuthContainer>
      </div>
    </>
  );
};
export default ResetPassword;
