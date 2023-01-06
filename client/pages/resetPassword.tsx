import AuthContainer from "components/reuse/AuthContainer";
import ResetPwAuth from "components/auth/ResetPwAuth";
import ResetPwSet from "components/auth/ResetPwSet";
import { useState } from "react";

const ResetPassword = () => {
  const [isAuth, setIsAuth] = useState<boolean>(false);

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
          {isAuth ? (
            <ResetPwSet />
          ) : (
            <ResetPwAuth setIsAuth={setIsAuth} />
          )}
        </AuthContainer>
      </div>
    </>
  );
};
export default ResetPassword;
