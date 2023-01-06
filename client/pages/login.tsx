import AuthBtn from "components/reuse/AuthBtn";
import AuthContainer from "components/reuse/AuthContainer";
import { useState } from "react";
import {useForm} from "react-hook-form";
import { ErrorMessage } from "@hookform/error-message";
import Link from "next/link";

const Login = () => {
    const [email, setEmail] = useState<string>('');
    const [password, setPassword] = useState<string>('');

    const {register, handleSubmit, formState: {errors}} = useForm({mode : "onBlur"});
    
    const handleEmailChange =(e:any) =>{
        setEmail(e.target.value);
    }
    const handlePasswordChange = (e:any) =>{
        setPassword(e.target.value);
    }

    const onSubmit = (e: any) => {
        console.log("Success Submit!");
      };
    
      const failSubmit = (e: any) => {
        console.log("Fail Submit");
      };

    return(<>
         <div className="flex flex-col bg-bgColor items-center mt-44 w-full h-screen">
          <div className="flex flex-col w-full h-fit justify-center items-center font-SCDream5 text-xl text-textColor mb-2">
            로그인</div>
            <div className="flex flex-col w-full h-fit justify-center items-center font-SCDream3 text-sm text-pointColor mb-2">
            로그인 후 원하는 선생님을 만나보세요!</div>
          <AuthContainer>
          <form onSubmit={handleSubmit(onSubmit, failSubmit)}
          className = "w-full h-fit">
            <div className="flex flex-col justify-center items-start w-full h-fit font-SCDream5 text-sm text-textColor mb-1">
                이메일</div>
            <input
                type="email"
                value = {email}
                placeholder="이메일을 입력하세요"
                className="w-full h-fit p-2 border border-borderColor outline-pointColor rounded-xl font-SCDream4 text-[11px] text-textColor tablet:text-sm"
                {...register("email", {
                    required: "필수로 입력해야되는 값입니다.",
                    onChange: handleEmailChange,
                    pattern: {
                        value:/\b[\w\.-]+@[\w\.-]+\.\w{2,4}\b/gi,
                        message: "올바르지 않은 이메일형식입니다.",
                    },
                })}
                />
                <ErrorMessage errors={errors} name="email"
                render={({message})=>{
                    return <div className="w-full h-fit flex flex-col justify-center items-start font-SCDream3 text-negativeMessage text-xs mt-1">
                        {message}
                    </div>}}/>

                 <div className="flex flex-col justify-center items-start w-full h-fit font-SCDream5 text-sm text-textColor mb-1 mt-4">
                 비밀번호</div>
                 <input
                type="password"
                value={password}
                placeholder="비밀번호를 입력하세요"
                className="w-full h-fit p-2 border border-borderColor outline-pointColor rounded-xl font-SCDream4 text-[11px] text-textColor tablet:text-sm"
                {...register("password", {
                    required: "필수로 입력해야되는 값입니다.",
                    onChange: handlePasswordChange,
                    pattern: {
                        value: /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,}$/gi,
                        message: "최소 8 자, 하나 이상의 대문자, 하나 이상의 소문자, 하나의 숫자 및 하나의 특수 문자를 포함합니다",
                    }
                })}
                />
                <ErrorMessage errors={errors} name="password"
                render={({message})=> {return <div className="w-full h-fit flex flex-col justify-center items-start font-SCDream3 text-negativeMessage text-xs mt-1">
                    {message}
                </div>}}/>

                <div className="w-full h-fit flex flex-col justify-center items-center mt-7">
                <AuthBtn onClick={handleSubmit}>로그인</AuthBtn>
                </div>
                <div className="w-full h-fit flex flex-row justify-center items-center text-pointColor mt-7">
                <Link href="/resetPassword">비밀번호를 잊으셨나요?</Link>  /   <Link href="/signup">  아직 회원이 아니신가요?</Link>
                </div>


            </form>
          </AuthContainer>
        </div>
    </>
          ); 
};
export default Login;