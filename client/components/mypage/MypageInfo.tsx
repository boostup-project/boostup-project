import { IconMail, IconProfile } from "assets/icon";

const MypageInfo = () => {
  const editProfile = () => {
    console.log(localStorage);
    return;
  };
  return (
    <>
      <div className="w-full h-full flex flex-row justify-center items-center">
        <div className="w-1/3 h-full flex flex-col justify-center items-center p-5">
          <img
            src={
              "https://play-lh.googleusercontent.com/38AGKCqmbjZ9OuWx4YjssAz3Y0DTWbiM5HB0ove1pNBq_o9mtWfGszjZNxZdwt_vgHo=w240-h480-rw"
            }
            // src={basicInfo.profileImage}
            alt="profile Image"
            width={200}
            height={200}
            className="rounded-xl border border-borderColor"
          />
        </div>
        <div className="w-full h-full flex flex-col justify-center items-start p-5">
          <div className="flex flex-row justify-start items-center w-full h-fit mt-3">
            <IconProfile width="30" />
            <div className="ml-1.5 pt-1 w-fit h-fit flex flex-row justify-start items-center font-SCDream5 desktop:text-base text-sm text-textColor">
              {localStorage.name}
            </div>
          </div>
          <div className="flex flex-row justify-start items-center w-full h-fit mt-8">
            <IconMail width="32" />
            <div className="ml-1.5 pt-1 w-fit h-fit flex flex-row justify-start items-center font-SCDream5 desktop:text-base text-sm text-textColor">
              {localStorage.email}
            </div>
          </div>
          <div className="flex mt-8">
            <button className="mr-8 text text-pointColor" onClick={editProfile}>
              Edit
            </button>
            <button className="mr-8 text text-pointColor">비밀번호수정</button>
          </div>
        </div>
      </div>
    </>
  );
};
export default MypageInfo;
