import { modalImgTxt } from "assets/color/color";
import DefaultImg from "assets/icon/DefaultImg";
import { useState, useEffect } from "react";
import { useForm } from "react-hook-form";
import { ValueContainerProps } from "react-select";
import AuthBtn from "./reuse/btn/AuthBtn";

const Additional = () => {
  const { register, handleSubmit, watch } = useForm();
  const [previeImages, setPreviewImages] = useState<any[]>([]);

  /** 미리보기 이미지 렌더링함수 **/
  const image = watch("image");
  useEffect(() => {
    if (image && image.length > 0) {
      const file = image[0];
      setPreviewImages([...previeImages, URL.createObjectURL(file)]);
    }
  }, [image]);

  /** 제출 테스트 코드 **/
  const testSubmit = (data: any) => {
    console.log(data);
    console.log(previeImages);
  };

  /** 이미지 삭제 함수(배열에서 해당 ID값 이미지 삭제) **/
  const deleteImg = (e: any) => {
    console.log(e);
    setPreviewImages(
      previeImages.filter((image, idx) => e.target.id !== String(idx)),
    );
  };

  return (
    <form
      className="placeholder:text-center w-full flex flex-col items-center text-sm tablet:text-base"
      onSubmit={handleSubmit(testSubmit)}
    >
      <label className="w-4/5 mt-6">
        <div className="flex">
          <div>한줄 소개</div>
          <span className="text-pointColor">*</span>
        </div>
        <input
          className="w-full h-8 border border-borderColor outline-pointColor rounded-xl font-SCDream2 text-xs text-textColor placeholder:text-center mt-2 tablet:text-sm"
          placeholder="본인에 대한 한줄 소개를 입력하세요"
        />
      </label>
      <label className="w-4/5 mt-6">
        <div className="flex">
          <div>재직 회사 / 학력</div>
          <span className="text-pointColor">*</span>
        </div>
        <div></div>
        <textarea
          className="w-full h-20 border border-borderColor outline-pointColor rounded-xl font-SCDream2 text-xs text-textColor placeholder:text-center placeholder:leading-loose break-all tablet:text-sm"
          placeholder="재직중인 회사 또는 재학중인 학교를 입력하세요"
        />
      </label>
      <label className="w-4/5 mt-6">
        <div className="flex">
          <div>장소 세부사항</div>
          <span className="text-pointColor">*</span>
        </div>
        <input
          className="w-full h-8 border border-borderColor outline-pointColor rounded-xl font-SCDream2 text-xs text-textColor placeholder:text-center mt-2 tablet:text-sm"
          placeholder="ex)화이트보드가 사용 가능한 스터디룸"
        />
      </label>
      <label className="w-4/5 mt-6">
        <div className="flex">
          <div>성격</div>
          <span className="text-pointColor">*</span>
        </div>
        <input
          className="w-full h-8 border border-borderColor outline-pointColor rounded-xl font-SCDream2 text-xs text-textColor placeholder:text-center tablet:text-sm"
          placeholder="본인에 대한 성격을 입력하세요"
        />
      </label>
      <label className="w-4/5 mt-6">
        <div className="flex">
          <div>수업료</div>
          <span className="text-pointColor">*</span>
        </div>
        <input
          className="w-full h-8 border border-borderColor outline-pointColor rounded-xl font-SCDream2 text-xs text-textColor placeholder:text-center tablet:text-sm"
          placeholder="수업료에 대한 자세한 내용을 입력하세요"
        />
      </label>
      <div className="w-4/5 mt-6">
        <div className="flex">
          <div>참고사진(최대 3개)</div>
        </div>
        <div className="w-full py-1.5 border flex justify-center items-center border-borderColor outline-pointColor rounded-xl font-SCDream2 text-xs text-textColor placeholder:text-center mt-2 tablet:text-sm">
          {previeImages.length > 0 ? (
            previeImages.map((el, idx) => (
              <div key={idx} className="relative w-1/4 pr-1">
                <img className="aspect-square rounded-xl relative" src={el} />
                <span
                  id={`${idx}`}
                  className="absolute top-0 right-2 text-red-500 text-lg"
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
                <DefaultImg width="69px" heigth="62px" fill={modalImgTxt} />
                <div>클릭하여 사진을 첨부하세요</div>
              </label>
              <input
                className="hidden"
                id="refImg"
                type="file"
                accept="image/*"
                {...register("image")}
              />
            </div>
          )}
        </div>
        {previeImages.length >= 1 && previeImages.length <= 2 && (
          <div className="w-full">
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
              id="refImg"
              type="file"
              accept="image/*"
              {...register("image")}
            />
          </div>
        )}
      </div>
      <button>테스트 버튼</button>
    </form>
  );
};
export default Additional;
