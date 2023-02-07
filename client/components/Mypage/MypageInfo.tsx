import { IconMail, IconProfile } from "assets/icon";
import { useState, useEffect } from "react";
import EditUserData from "./EditUserData";
import EditUserPwd from "./EditUserPwd";

const MypageInfo = () => {
  const [isMemberEdit, setIsMemberEdit] = useState(false);
  const [isPwdEdit, setIsPwdEdit] = useState(false);
  const [email, setEmail] = useState<string>("");
  const [name, setName] = useState<string>("");
  const editProfile = () => {
    setIsMemberEdit(prev => !prev);
  };
  const editPWd = () => {
    setIsPwdEdit(prev => !prev);
  };
  useEffect(() => {
    if (localStorage) {
      setEmail(localStorage.email);
      setName(localStorage.name);
    }
  }, [email, name]);
  return (
    <>
      <div className="w-full flex flex-row justify-start items-center">
        <div className="object-cover desktop:w-[260px] tablet:w-[250px] w-[200px] h-fit flex flex-col justify-start items-start p-5 ">
          <img
            src={
              "https://play-lh.googleusercontent.com/38AGKCqmbjZ9OuWx4YjssAz3Y0DTWbiM5HB0ove1pNBq_o9mtWfGszjZNxZdwt_vgHo=w240-h480-rw"
            }
            // src={data.profileImage}
            alt="profile Image"
            width={200}
            height={200}
            className="object-cover rounded-xl border border-borderColor"
          />
        </div>
        <div className="w-4/5 h-full flex flex-col justify-start items-start py-5">
          <div className="flex flex-row justify-start items-start w-full h-fit mt-3">
            <div className="desktop:w-9 tablet:w-7 w-5 fill=#A8A7A7">
              <IconProfile />
            </div>
            <div className="ml-1.5 pt-1 w-fit h-fit flex flex-row justify-start items-center font-SCDream5 desktop:text-xl tablet:text-base text-textColor">
              {name}
            </div>
          </div>
          <div className="flex flex-row justify-start items-center w-full h-fit desktop:mt-8 tablet:mt-6 mt-2">
            <div className="desktop:w-9 tablet:w-7 w-5">
              <IconMail />
            </div>
            <div className="ml-1.5 pt-1 w-fit h-fit flex flex-row justify-start items-center font-SCDream5 desktop:text-xl text-base text-textColor">
              {email}
            </div>
          </div>
          <div className="flex desktop:mt-8 tablet:mt-6 mt-2">
            <button className="mr-8 text text-pointColor" onClick={editProfile}>
              Edit
            </button>
            <button className="mr-8 text text-pointColor" onClick={editPWd}>
              비밀번호수정
            </button>
          </div>
        </div>
        {isMemberEdit && <EditUserData editProfile={editProfile} />}
        {isPwdEdit && <EditUserPwd editPWd={editPWd} />}
      </div>
    </>
  );
};
export default MypageInfo;
