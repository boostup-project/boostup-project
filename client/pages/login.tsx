import InputBox from "components/reuse/InputBox";
import AuthBtn from "components/reuse/AuthBtn";
import AuthContainer from "components/reuse/AuthContainer"
import { useState } from "react";

const Login = () => {
    const [email, setEmail] = useState('')
    const [password, setPassword] = useState('')

    const handleEmail = (e:any) =>{
        setEmail(e.target.value)
     }
     const handlePassword = (e:any) =>{
        setPassword(e.target.value)
     }
    return(<>
         <div className="flex flex-col bg-bgColor items-center justify-center w-full h-screen">
         <AuthContainer>
         <div className="absolute left-12 top-52 my-2 font-SCDream6">이메일</div>
            <InputBox placeholder="Enter your Email" value={email} onChange={handleEmail}></InputBox>
            <div className="absolute left-12 top-72 my-2 w-24 font-SCDream6" >비밀번호</div>
            <InputBox placeholder="Enter your password" value={password} onChange={handlePassword}></InputBox>
           <AuthBtn  onClick={()=>{}}>
           로그인하기 </AuthBtn>
            </AuthContainer>
        </div>
    </>
          ); 
};
export default Login;