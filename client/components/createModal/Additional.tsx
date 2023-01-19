import { modalImgTxt } from "assets/color/color";
import { useState, useEffect, SetStateAction } from "react";
import { useForm } from "react-hook-form";
import { IconImg } from "assets/icon/";
import SmallBtn from "components/reuse/btn/SmallBtn";
import { SetterOrUpdater } from "recoil";
import { Info } from "./WriteModal";
import { Dispatch } from "react";

interface Additional {
  [index: string]: string | string[];
}

interface Props {
  addInfo: Info;
  setAddInfo: Dispatch<SetStateAction<Info>>;
  setStep: SetterOrUpdater<number>;
}

const Additional = ({ addInfo, setAddInfo, setStep }: Props) => {
  const [previewImages, setPreviewImages] = useState<string[]>([]);
  const {
    register,
    handleSubmit,
    watch,
    formState: { errors },
  } = useForm<Additional>({
    mode: "onBlur",
  });

  /** 미리보기 이미지 렌더링함수, any 이외의 type 찾기 부족 **/
  const image = watch("referenceImage");
  useEffect(() => {
    if (image && image.length > 0) {
      const file: any = image[0];
      setPreviewImages(prev => prev.concat(URL.createObjectURL(file)));
    }
  }, [image]);

  /** 이미지 삭제 함수(배열에서 해당 ID값 이미지 삭제) **/
  const deleteImg = (e: any) => {
    console.log(e);
    setPreviewImages(
      previewImages.filter((image, idx) => e.target.id !== String(idx)),
    );
  };

  /** 제출 코드 **/
  const testSubmit = (addtionalData: Additional) => {
    setAddInfo(addtionalData);
    setStep(prev => prev + 1);
  };

  /** 이전 페이지 돌아가기 **/
  const toBack = (addtionalData: Additional) => {
    setAddInfo(addtionalData);
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
          <div className="w-full py-1.5 border flex justify-center items-center border-borderColor outline-pointColor rounded-xl font-SCDream4 text-xs text-textColor placeholder:text-center mt-2 tablet:text-sm">
            {previewImages.length >= 1 ? (
              previewImages.map((el, idx) => (
                <div key={idx} className="relative w-1/4 pr-1">
                  <img className="aspect-square rounded-xl relative" src={el} />
                  <span
                    id={`${idx}`}
                    className="absolute top-0 right-2 text-negativeMessage text-lg"
                    onClick={e => deleteImg(e)}
                  >
                    X
                  </span>
                </div>
              ))
            ) : (
              <div>
                <label
                  className="flex flex-col justify-center items-center text-modalImgTxt"
                  htmlFor="refImg"
                >
                  <IconImg width="69px" heigth="62px" fill={modalImgTxt} />
                  <div>클릭하여 사진을 첨부하세요</div>
                </label>
                <input
                  className="hidden"
                  multiple
                  id="refImg"
                  type="file"
                  {...register("referenceImage")}
                />
              </div>
            )}
          </div>
          {previewImages.length >= 1 && previewImages.length <= 2 && (
            <div className="w-full mt-2">
              <label
                className="flex justify-center items-center"
                htmlFor="refImg"
              >
                <span
                  className={`font-SCDream4 w-1/4 py-2 bg-pointColor rounded-xl text-white flex justify-center`}
                >
                  추가하기
                </span>
              </label>
              <input
                className="hidden"
                multiple
                id="refImg"
                type="file"
                {...register("referenceImage")}
              />
            </div>
          )}
        </div>
        <div className="flex flex-row justify-center items-center w-full h-fit mt-10">
          <SmallBtn onClick={e => toBack}>이 전</SmallBtn>
          <SmallBtn css="ml-5">다 음</SmallBtn>
        </div>
      </form>
    </>
  );
};
export default Additional;
