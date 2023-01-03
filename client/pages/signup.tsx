import InputBox from "components/reuse/InputBox";
import AuthBtn from "components/reuse/AuthBtn";
import AuthContainer from "components/reuse/AuthContainer"
import { useState } from "react";

const Signup = () => {
 const [name, setName] = useState('')
 const [email, setEmail] = useState('')
 const [password, setPassword] = useState('')
 const [passwordCheck, setPasswordCheck] = useState('')

 const handleName = (e:any) =>{
    setName(e.target.value)
 }
 const handleEmail = (e:any) =>{
    setEmail(e.target.value)
 }
 const handlePassword = (e:any) =>{
    setPassword(e.target.value)
 }
 const handlePasswordCheck = (e:any) =>{
    setPasswordCheck(e.target.value)
 }


    return(<>
         <div className="flex flex-col bg-bgColor items-center justify-items-center w-full h-screen">
            <AuthContainer>
            <div className="absolute left-12 top-28 my-2 font-SCDream6">닉네임</div>
            <InputBox placeholder="Enter your Nickname" value={name} onChange={handleName}></InputBox>
            <div className="absolute left-12 top-52 my-2 font-SCDream6">이메일</div>
            <InputBox placeholder="Enter your Email" value={email} onChange={handleEmail}></InputBox>
            <div className="absolute left-12 top-72 my-2 w-16 font-SCDream6">비밀번호</div>
            <InputBox placeholder="Enter your password" value={password} onChange={handlePassword}></InputBox>
            <div className="absolute left-12 bottom-60 my-2 w-30 font-SCDream6">비밀번호 확인</div>
            <InputBox placeholder="Enter same password" value={passwordCheck} onChange={handlePasswordCheck}></InputBox>
            <AuthBtn onClick={()=>{}}>
            가입하기 </AuthBtn>
            </AuthContainer>
         </div>
    </>); 
};
export default Signup;