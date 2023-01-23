import { modalImgTxt } from "assets/color/color";
import { useState, useRef, SetStateAction } from "react";
import { useForm } from "react-hook-form";
import { IconImg } from "assets/icon/";
import SmallBtn from "components/reuse/btn/SmallBtn";
import { SetterOrUpdater } from "recoil";
import { Info } from "./WriteModal";
import { Dispatch } from "react";

interface ExtraInfo {
  [index: string]: string | string[];
}

interface Props {
  setExtraInfo: Dispatch<SetStateAction<Info>>;
  setStep: SetterOrUpdater<number>;
}

const ExtraInfo = ({ setExtraInfo, setStep }: Props) => {
  const [previewImages, setPreviewImages] = useState([]);
  const [detailImage, setDetailImage] = useState<string[]>([]);
  const imageInput: any = useRef();
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<ExtraInfo>({
    mode: "onBlur",
  });

  const onCickImageUpload = () => {
    imageInput.current.click();
  };
  console.log("detail", detailImage);

  const insertImg = (e: any) => {
    let reader = new FileReader();
    setDetailImage(prev => prev.concat(e.target.files));

    if (e.target.files[0]) {
      reader.readAsDataURL(e.target.files[0]);
    }
    reader.onloadend = () => {
      const previewImgUrl: any = reader.result;

      if (previewImgUrl) {
        setPreviewImages(prev => prev.concat(previewImgUrl));
      }
    };
  };
  const deleteImg = (e: any) => {
    setPreviewImages(
      previewImages.filter((image, idx) => e.target.id !== String(idx)),
    );
    setDetailImage(
      detailImage.filter((image, idx) => e.target.id !== String(idx)),
    );
  };

  /** 제출 코드 **/
  const testSubmit = (addtionalData: ExtraInfo) => {
    addtionalData.detailImage = detailImage;
    setExtraInfo(addtionalData);
    setStep(prev => prev + 1);
  };

  /** 이전 페이지 돌아가기 **/
  const toBack = () => {
    setStep(prev => prev - 1);
  };

  return (
    <>
      <div className="w-full h-fit font-SCDream5 text-xs text-center text-pointColor mt-2 tablet:text-sm">
        등록할 과와에 대한 자세한 정보를 입력하세요
      </div>
      <form
        className="placeholder:text-center w-full flex flex-col items-center text-sm tablet:text-base"
        onSubmit={handleSubmit(testSubmit)}
      >
        <label className="w-11/12 tablet:w-11/12 desktop:w-4/6 mt-6">
          <div className="flex">
            <div className="font-SCDream5">한줄 소개</div>
            <span className="text-pointColor">*</span>
          </div>
          <input
            className="w-full h-8 px-2 border border-borderColor outline-pointColor rounded-xl font-SCDream4 text-xs text-textColor placeholder:text-center  mt-2 tablet:text-sm tablet:h-12"
            placeholder="본인에 대한 한줄 소개를 입력하세요"
            {...register("introduction", { required: "입력 필요" })}
            // defaultValue={add.introduction}
          />
        </label>
        <p className="text-xs text-negativeMessage mt-1 tablet:text-sm">
          {errors?.introduction?.message}
        </p>
        <label className="w-11/12 tablet:w-11/12 desktop:w-4/6 mt-6">
          <div className="flex">
            <div className="font-SCDream5">재직 회사 / 학력</div>
            <span className="text-pointColor">*</span>
          </div>
          <div></div>
          <textarea
            className="w-full h-20 p-2 border border-borderColor outline-pointColor rounded-xl font-SCDream4 text-xs text-textColor placeholder:text-center placeholder:translate-y-[110%] placeholder:leading-loose break-all tablet:text-sm tablet:h-24"
            placeholder="재직중인 회사 또는 재학중인 학교를 입력하세요"
            {...register("detailCompany", { required: "입력 필요" })}
            // defaultValue={add.detailCompany}
          />
        </label>
        <p className="text-xs text-negativeMessage mt-1 tablet:text-sm">
          {errors?.detailCompany?.message}
        </p>
        <label className="w-11/12 tablet:w-11/12 desktop:w-4/6 mt-6">
          <div className="flex">
            <div className="font-SCDream5">장소 세부사항</div>
            <span className="text-pointColor">*</span>
          </div>
          <input
            className="w-full h-8 px-2 border border-borderColor outline-pointColor rounded-xl font-SCDream4 text-xs text-textColor placeholder:text-center mt-2 tablet:text-sm tablet:h-12"
            placeholder="ex)화이트보드가 사용 가능한 스터디룸"
            {...register("detailLocation", { required: "입력 필요" })}
            // defaultValue={add.detailLocation}
          />
        </label>
        <p className="text-xs text-negativeMessage mt-1 tablet:text-sm">
          {errors?.detailLocation?.message}
        </p>
        <label className="w-11/12 tablet:w-11/12 desktop:w-4/6 mt-6">
          <div className="flex">
            <div className="font-SCDream5">성격</div>
            <span className="text-pointColor">*</span>
          </div>
          <input
            className="w-full h-8 px-2 border border-borderColor outline-pointColor rounded-xl font-SCDream4 text-xs text-textColor placeholder:text-center tablet:text-sm tablet:h-12"
            placeholder="본인에 대한 성격을 입력하세요"
            {...register("personality", { required: "입력 필요" })}
            // defaultValue={add.personality}
          />
        </label>
        <p className="text-xs text-negativeMessage mt-1 tablet:text-sm">
          {errors?.personality?.message}
        </p>
        <label className="w-11/12 tablet:w-11/12 desktop:w-4/6 mt-6">
          <div className="flex">
            <div className="font-SCDream5">수업료</div>
            <span className="text-pointColor">*</span>
          </div>
          <input
            className="w-full h-8 px-2 border border-borderColor outline-pointColor rounded-xl font-SCDream4 text-xs text-textColor placeholder:text-center tablet:text-sm tablet:h-12"
            placeholder="수업료에 대한 자세한 내용을 입력하세요"
            type="text"
            {...register("detailCost", { required: "입력 필요" })}
            // defaultValue={add.detailCost}
          />
        </label>
        <p className="text-xs text-negativeMessage mt-1 tablet:text-sm">
          {errors?.detailCost?.message}
        </p>

        <div className="w-11/12 tablet:w-11/12 desktop:w-4/6 mt-6">
          <div className="flex">
            <div className="font-SCDream5">참고사진(최대 3개)</div>
          </div>
          <div className="w-full py-3 border flex border-borderColor outline-pointColor rounded-xl font-SCDream4 text-xs text-textColor placeholder:text-center mt-2 tablet:text-smhover:bg-gray-100">
            <div className="w-full flex h-fit items-center justify-center">
              {previewImages.length >= 1 ? (
                previewImages.map((el, idx) => (
                  <div key={idx} className="relative w-1/4 pr-1">
                    <img
                      className="aspect-square rounded-xl relative"
                      src={el}
                    />
                    <span
                      id={`${idx}`}
                      className="absolute top-0 right-2 text-negativeMessage text-lg cursor-pointer"
                      onClick={e => deleteImg(e)}
                    >
                      X
                    </span>
                  </div>
                ))
              ) : (
                <div className="w-full flex flex-col justify-center items-center">
                  <IconImg width="69px" heigth="62px" fill={modalImgTxt} />
                  <p className="pt-1 text-sm h-fit tracking-wider text-modalImgTxt group-hover:text-gray-600">
                    추가하기를 눌러 사진을 첨부하세요
                  </p>
                </div>
              )}
            </div>
          </div>
          {detailImage.length < 3 && (
            <div className="w-full mt-2">
              <div
                onClick={onCickImageUpload}
                className="flex justify-center items-center cursor-pointer"
              >
                <span
                  className={`font-SCDream4 w-1/4 py-2 bg-pointColor rounded-xl text-white flex justify-center`}
                >
                  추가하기
                </span>
              </div>
              <input
                type="file"
                className="hidden"
                accept="image/jpeg,.txt"
                {...register("referenceImage")}
                ref={imageInput}
                onChange={e => insertImg(e)}
              />
            </div>
          )}
        </div>
        <div className="flex flex-row justify-center items-center w-full h-fit mt-10">
          <SmallBtn onClick={toBack}>이 전</SmallBtn>
          <SmallBtn css="ml-5">다 음</SmallBtn>
        </div>
      </form>
    </>
  );
};
export default ExtraInfo;
