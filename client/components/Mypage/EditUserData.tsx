import { modalImgTxt } from "assets/color/color";
import { IconImg } from "assets/icon";
import { useEffect } from "react";
import { ChangeEvent } from "react";
import { useState } from "react";
import { useForm } from "react-hook-form";
import { toast } from "react-toastify";
import SmallBtn from "components/reuse/btn/SmallBtn";
import AuthBtn from "components/reuse/btn/AuthBtn";
import ModalBackDrop from "components/reuse/container/ModalBackDrop";
import usePostMemberModi from "hooks/mypage/usePostMemberModi";
import usePostNameCheck from "hooks/mypage/usePostNameCheck";
import imageCompression from "browser-image-compression";
import CreateModalMypageContainer from "components/reuse/container/CreateModalMypageContainer";

interface Props {
  editProfile: () => void;
}
interface MemberEditData {
  name: string;
  profileImg: FileList;
}

const EditUserData = ({ editProfile }: Props) => {
  const [duplicationName, setDuplicationName] = useState("");
  const [previewImg, setPreviewImg] = useState<string>("");
  const { mutate, data, isSuccess } = usePostNameCheck();
  const {
    mutate: memberModi,
    isSuccess: isMemberSuccess,
    isError: isMemberError,
  } = usePostMemberModi();
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<MemberEditData>({ mode: "onBlur" });

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

  /** 이미지 압축 함수 */
  const compressImage = async (image: any) => {
    try {
      const options = {
        maxSizeMb: 2,
        maxWidthOrHeight: 300,
      };
      return await imageCompression(image, options);
    } catch (e) {
      console.log(e);
    }
  };

  /** 변경정보 제출 */
  const editSubmit = async (e: MemberEditData) => {
    const { name, profileImg } = e;
    const json = JSON.stringify({
      name,
    });
    const blob = new Blob([json], { type: "application/json" });
    const formData = new FormData();
    formData.append("data", blob);
    if (profileImg) {
      const compressImg = await compressImage(profileImg[0]);
      let compressFile;
      if (compressImg) {
        compressFile = new File([compressImg!], compressImg?.name);
        formData.append("profileImage", compressFile);
      }
    }
    memberModi(formData);
  };

  useEffect(() => {
    if (isMemberError) {
      toast.error("정보 수정이 되지 않았습니다. 다시 작성부탁드립니다", {
        autoClose: 2000,
        position: toast.POSITION.TOP_RIGHT,
      });
    }
    if (isMemberSuccess) {
      editProfile();
    }
  }, [isMemberError, isMemberSuccess]);

  return (
    <>
      <ModalBackDrop onClick={editProfile}>
        <CreateModalMypageContainer>
          <div className="w-full font-SCDream7 text-lg text-textColor mt-4 desktop:w-full">
            개인정보 수정하기
          </div>
          <form
            className="w-full font-SCDream5 text-sm desktop:w-full"
            onSubmit={handleSubmit(editSubmit)}
          >
            <div className="h-fit font-SCDream5 text-base text-textColor mt-2 mb-2">
              프로필 사진<span className="text-pointColor ">*</span>
            </div>
            <div className="w-full flex items-center rounded-xl justify-center h-fit border tablet:w-full">
              <label className="flex flex-col w-full h-32 border-borderColor hover:bg-gray-100 hover:border-gray-300 tablet:w-full">
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
            <div className="h-fit font-SCDream5 text-base text-textColor mt-4 mb-2">
              닉네임
            </div>
            <div className="w-full flex tablet:w-full">
              <label className="w-9/12">
                <input
                  id="duplicationName"
                  type="text"
                  placeholder="닉네임 중복검사를 해주세요"
                  className="w-full h-fit p-2 border border-borderColor outline-pointColor rounded-l-xl font-SCDream4 text-xs text-textColor tablet:text-sm"
                  {...register("name", {
                    required: false,
                    pattern: {
                      value: /^[a-zA-Zㄱ-힣0-9|s]{5,12}$/gm,
                      message: "5자이상 숫자,영문자로 작성해 주세요",
                    },
                  })}
                  onChange={e => nameReciever(e)}
                />
              </label>
              <div
                className="w-3/12 flex justify-center items-center text-sm border border-l-0 bg-borderColor border-borderColor rounded-r-xl text-bgColor cursor-pointer"
                onClick={duplicationCheck}
              >
                중복검사
              </div>
            </div>
            <p className="text-xs text-negativeMessage mt-1 tablet:text-sm">
              {errors?.name?.message}
            </p>
            <div className="mt-10 flex flex-row justify-center w-full h-fit items-center tablet:w-full">
              <SmallBtn
                css="mr-4"
                // className="flex justify-center items-center w-1/4 py-2 bg-borderColor rounded-xl text-negativeMessage text-sm mr-5 cursor-pointer"
                onClick={editProfile}
              >
                취 소
              </SmallBtn>
              <SmallBtn>변 경</SmallBtn>
            </div>
          </form>
        </CreateModalMypageContainer>
      </ModalBackDrop>
    </>
  );
};

export default EditUserData;
