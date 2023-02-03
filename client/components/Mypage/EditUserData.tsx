import { modalImgTxt } from "assets/color/color";
import { IconImg } from "assets/icon";
import AuthBtn from "components/reuse/btn/AuthBtn";
import CreateModalContainer from "components/reuse/container/CreateModalContainer";
import ModalBackDrop from "components/reuse/container/ModalBackDrop";
import usePostNameCheck from "hooks/mypage/usePostNameCheck";
import { useEffect } from "react";
import { ChangeEvent } from "react";
import { useState } from "react";
import { useForm } from "react-hook-form";
import { ToastContainer } from "react-toastify";

interface Props {
  editProfile: () => void;
}

const EditUserDataModal = ({ editProfile }: Props) => {
  const [duplicationName, setDuplicationName] = useState("");
  const [previewImg, setPreviewImg] = useState<string>("");
  const { mutate, data, isSuccess } = usePostNameCheck();
  const { register, handleSubmit } = useForm();

  useEffect(() => {
    if (isSuccess && data) {
      console.log(data.data);
    }
  }, [isSuccess]);

  const insertImg = (e: any) => {
    let reader = new FileReader();

    if (e.target.files[0]) {
      reader.readAsDataURL(e.target.files[0]);
    }
    reader.onloadend = () => {
      const previewImgUrl = reader.result as string;

      if (previewImgUrl) {
        setPreviewImg(previewImgUrl);
      }
    };
  };
  const deleteImg = () => {
    setPreviewImg("");
  };

  const nameReciever = (e: ChangeEvent<HTMLInputElement>) => {
    setDuplicationName(e.target.value);
  };
  const duplicationCheck = () => {
    console.log(duplicationName);
    mutate(duplicationName);
  };
  const editSubmit = () => {
    console.log("submit");
  };

  return (
    <>
      <ModalBackDrop onClick={editProfile}>
        <CreateModalContainer>
          <div className="w-full font-SCDream7 text-borderColor">
            개인정보 수정하기
          </div>
          <form
            className="w-full font-SCDream5 text-sm"
            onSubmit={handleSubmit(editSubmit)}
          >
            <div className="mt-4">
              프로필 사진<span className="text-pointColor">*</span>
            </div>
            <div className="w-full flex items-center rounded-xl justify-center h-fit border desktop:w-4/6">
              <label className="flex flex-col w-full h-32 border-borderColor hover:bg-gray-100 hover:border-gray-300">
                <div className="relative flex flex-col h-fit  items-center justify-center pt-7">
                  {previewImg ? (
                    <>
                      <img
                        id="preview"
                        className="absolute inset-0 w-full h-32 object-contain"
                        src={previewImg}
                      />
                      <button
                        className="absolute top-1 right-3  text-red-500 text-lg"
                        onClick={deleteImg}
                      >
                        삭제
                      </button>
                    </>
                  ) : (
                    <>
                      <IconImg width="69px" heigth="62px" fill={modalImgTxt} />
                      <p className="pt-1 text-sm h-fit tracking-wider text-modalImgTxt group-hover:text-gray-600">
                        클릭하여 사진을 첨부하세요
                      </p>
                    </>
                  )}
                </div>
                <input
                  type="file"
                  className="opacity-0 rounded-xl"
                  accept="image/jpeg,.txt"
                  {...register("profileImg", {
                    required: "필수 정보입니다",
                  })}
                  onChange={e => insertImg(e)}
                />
              </label>
            </div>
            <div className="mt-4">닉네임</div>
            <div className="w-full flex">
              <label className="w-9/12">
                <input
                  id="duplicationName"
                  type="text"
                  placeholder="닉네임 중복검사를 해주세요"
                  className="w-full desktop:w-4/6 h-fit p-2 border border-borderColor outline-pointColor rounded-l-xl font-SCDream4 text-xs text-textColor tablet:text-sm "
                  {...register("name")}
                  onChange={e => nameReciever(e)}
                />
              </label>
              <button
                className="w-3/12 text-xs border border-l-0 bg-borderColor border-borderColor rounded-r-xl text-bgColor"
                onClick={duplicationCheck}
              >
                중복검사
              </button>
            </div>
            <div className="mt-7 flex justify-center">
              <button className="font-SCDream4 w-1/4 py-2 bg-borderColor rounded-xl text-pointColor text-sm mr-5">
                취소하기
              </button>
              <AuthBtn>변경하기</AuthBtn>
            </div>
          </form>
        </CreateModalContainer>
      </ModalBackDrop>
    </>
  );
};

export default EditUserDataModal;
