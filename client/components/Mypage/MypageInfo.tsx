import { IconMail, IconProfile } from "assets/icon";

const MypageInfo = () => {
  const editProfile = () => {
    console.log(localStorage);
    return;
  };
  return (
    <>
      <div className="w-full h-full flex flex-row justify-start items-center">
        <div className="w-1/3 tablet:w-1/2 w-full h-full flex flex-col justify-center items-center p-5">
          <img
            src={
              "https://play-lh.googleusercontent.com/38AGKCqmbjZ9OuWx4YjssAz3Y0DTWbiM5HB0ove1pNBq_o9mtWfGszjZNxZdwt_vgHo=w240-h480-rw"
            }
            // src={data.profileImage}
            alt="profile Image"
            width={200}
            height={200}
            className="rounded-xl border border-borderColor"
          />
        </div>
        <div className="w-full h-full flex flex-col justify-start items-start p-5">
          <div className="flex flex-row justify-start items-start w-full h-fit mt-3">
            <div className="desktop:w-9 tablet:w-7 w-5">
              <IconProfile />
            </div>
            <div className="ml-1.5 pt-1 w-fit h-fit flex flex-row justify-start items-center font-SCDream5 desktop:text-xl tablet:text-base text-textColor">
              {/* {localStorage.name} */}
              myname
            </div>
          </div>
          <div className="flex flex-row justify-start items-center w-full h-fit mt-8 tablet:mt-6 mt-2">
            <div className="desktop:w-9 tablet:w-7 w-5">
              <IconMail />
            </div>
            <div className="ml-1.5 pt-1 w-fit h-fit flex flex-row justify-start items-center font-SCDream5 desktop:text-xl text-base text-textColor">
              {/* {localStorage.email} */}
              wjdgksmf11@daum.net
            </div>
          </div>
          <div className="flex mt-8 tablet:mt-6 mt-2">
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
