import { IconMail, IconProfile } from "assets/icon";
import useDeleteAccount from "hooks/mypage/useDeleteAccount";
import { useState, useEffect } from "react";
import EditUserData from "./EditUserData";
import EditUserPwd from "./EditUserPwd";
import { useRouter } from "next/router";
import Swal from "sweetalert2";
const MypageInfo = () => {
  const [isMemberEdit, setIsMemberEdit] = useState(false);
  const [isPwdEdit, setIsPwdEdit] = useState(false);
  const [email, setEmail] = useState<string>("");
  const [name, setName] = useState<string>("");
  const router = useRouter();
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
  const { mutate, isSuccess, isError } = useDeleteAccount();
  const deleteMyAccount = () => {
    Swal.fire({
      title: "내 계정을 삭제하시겠습니까?",
      text: "삭제한 계정은 복구할 수 없습니다.",
      icon: "question",
      showCancelButton: true,
      confirmButtonColor: "#3085d6",
    }).then(result => {
      if (result.isConfirmed) {
        mutate();
      }
    });
  };
  useEffect(() => {
    if (isSuccess) {
      router.push("/");
      Swal.fire({
        text: "삭제가 완료되었습니다",
        icon: "success",
        confirmButtonColor: "#3085d6",
      });
    }
    if (isError) {
      Swal.fire({
        text: "다시 삭제해주세요",
        icon: "warning",
        confirmButtonColor: "#3085d6",
      });
    }
  }, [isSuccess, isError]);

  return (
    <>
      <div className="w-full flex flex-row justify-start items-center">
        <div className="object-cover desktop:w-[260px] tablet:w-[250px] w-[150px] h-fit flex flex-col justify-start items-start p-5 ">
          <img
            // src={localStorage.memberImage}
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
            <button
              className="mr-8 text text-negativeMessage"
              onClick={deleteMyAccount}
            >
              회원탈퇴
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
