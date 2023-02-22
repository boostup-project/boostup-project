import AuthBtn from "components/reuse/btn/AuthBtn";
import AuthContainer from "components/reuse/container/AuthContainer";
import { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import { useRouter } from "next/router";
import { ErrorMessage } from "@hookform/error-message";
import Link from "next/link";
import usePostLogin from "hooks/auth/usePostLogin";
import { useRecoilValue } from "recoil";
import { loginErrorMessage } from "atoms/auth/authAtom";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import useGetKakaoLogin from "hooks/auth/useGetKakaoLogin";
import useGetGoogleLogin from "hooks/auth/useGetGoogleLogin";
import SeoHead from "components/reuse/SEO/SeoHead";
import { useSession, signIn, signOut } from "next-auth/react";
const Login = () => {
  const [email, setEmail] = useState<string>("");
  const [password, setPassword] = useState<string>("");
  const Error = useRecoilValue(loginErrorMessage);
  const router = useRouter();
  const { data: session } = useSession();
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm({ mode: "onBlur" });

  const { mutate, error, isSuccess, isError } = usePostLogin();

  const handleEmailChange = (e: any) => {
    setEmail(e.target.value);
  };
  const handlePasswordChange = (e: any) => {
    setPassword(e.target.value);
  };

  const onSubmit = (e: any) => {
    mutate({ email, password });
  };

  const failSubmit = (e: any) => {};

  useEffect(() => {
    if (isSuccess) {
      toast.success("로그인에 성공하였습니다!", {
        autoClose: 3000,
        position: toast.POSITION.TOP_RIGHT,
      });

      setTimeout(() => {
        router.push("/");
      }, 1000);
    }

    if (isError) {
      toast.error(`${Error}`, {
        autoClose: 3000,
        position: toast.POSITION.TOP_RIGHT,
      });
    }
  }, [isSuccess, isError]);

  const { refetch: kakaoRefetch } = useGetKakaoLogin();
  const kakaoLogin = () => {
    kakaoRefetch();
  };
  const { refetch: googleRefetch, data: googleData } = useGetGoogleLogin();
  const googleLogin = () => {
    signIn();
    console.log(googleData);
  };

  return (
    <>
      {!session && (
        <>
          <SeoHead metaType="Login" />
          <ToastContainer />
          <div className="flex flex-col bg-bgColor items-center mt-44 w-full h-screen">
            <div className="flex flex-col w-full h-fit justify-center items-center font-SCDream5 text-xl text-textColor mb-2">
              로그인
            </div>
            <div className="flex flex-col w-full h-fit justify-center items-center font-SCDream3 text-sm text-pointColor mb-2">
              로그인 후 원하는 선생님을 만나보세요!
            </div>
            <AuthContainer>
              <form
                onSubmit={handleSubmit(onSubmit, failSubmit)}
                className="w-full h-fit"
              >
                <div className="flex flex-col justify-center items-start w-full h-fit font-SCDream5 text-sm text-textColor mb-1">
                  이메일
                </div>
                <input
                  type="email"
                  value={email}
                  placeholder="이메일을 입력하세요"
                  className="w-full h-fit p-2 border border-borderColor outline-pointColor rounded-xl font-SCDream4 text-[11px] text-textColor tablet:text-sm"
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
                        /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,}$/gi,
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

                <div className="w-full h-fit flex flex-col justify-center items-center mt-7">
                  <AuthBtn onClick={handleSubmit}>로그인</AuthBtn>
                </div>
                <div className="w-full h-fit flex flex-row justify-center items-center font-SCDream3 desktop:text-[12px] tablet:text-[12px] text-[10px] text-pointColor mt-7">
                  <Link href="/resetPassword">비밀번호를 잊으셨나요?</Link> /{" "}
                  <Link href="/signup"> 아직 회원이 아니신가요?</Link>
                </div>
              </form>
              <div className="w-full">
                {/* <button
              className="flex items-center justify-center font-SCDream4 w-full h-10 my-2 bg-kakao hover:border-2 border border-kakaoBorder rounded-xl text-black text-sm mt-4"
              onClick={kakaoLogin}
            >
              <img
                className="w-6 h-fit"
                src="https://cdn.imweb.me/thumbnail/20190731/59ea3ebca6d79.png"
              />
              Login With Kakao
            </button> */}
                <button
                  className="flex items-center justify-center font-SCDream4 w-full h-10 my-2 bg-black/5 hover:border-2 border border-borderColor rounded-xl text-textColor text-sm mt-4"
                  onClick={() => googleLogin()}
                >
                  <img
                    className="w-6 h-fit"
                    src="https://img1.daumcdn.net/thumb/R800x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2Fdi0lwN%2Fbtq6llq0fqv%2Fj4pSykZA8KEPzKInXknTKk%2Fimg.png"
                  />
                  Login With Google
                </button>
              </div>
            </AuthContainer>
          </div>
        </>
      )}
      {session && (
        <>
          Signed in as {session.user?.name} <br />
          <button onClick={() => signOut()}>Sign out</button>
          {session.user?.email}
          {session.user?.name}
        </>
      )}
    </>
  );
};
export default Login;
