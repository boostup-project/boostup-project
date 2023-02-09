import { useForm } from "react-hook-form";
import { modalImgTxt } from "assets/color/color";
import { IconImg } from "assets/icon";
import { useState, useRef } from "react";
import ExtraInfo from "../createModal/ExtraInfo";
import CreateModalContainer from "../reuse/container/CreateModalContainer";
import { DetailTitles } from "./DetailExtra";
import usePostExtraModi from "hooks/detail/usePostExtraModi";
import { useEffect } from "react";
import { toast } from "react-toastify";
import ModalBackDrop from "components/reuse/container/ModalBackDrop";
import imageCompression from "browser-image-compression";
import { useSetRecoilState } from "recoil";
import { refetchToggle } from "atoms/detail/detailAtom";

interface Props {
  modalOpen: () => void;
  textData: DetailTitles;
  images: string[];
  lessonId: number;
}

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

const DetailExtraModi = ({ modalOpen, textData, images, lessonId }: Props) => {
  const [previewImages, setPreviewImages] = useState<any[]>([]);
  const [detailImages, setDetailImages] = useState<any[]>([]);
  const [deletedArr, setDeletedArr] = useState<number[]>([]);
  const setToggle = useSetRecoilState(refetchToggle);

  const imageInput: any = useRef();
  const { mutate, isError, isSuccess } = usePostExtraModi();
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<ExtraInfo>({
    mode: "onBlur",
  });

  const {
    introduction,
    detailCompany,
    personality,
    detailCost,
    detailLocation,
  } = textData;

  /** 정보에 이미지 있을 시 집어넣기 **/
  useEffect(() => {
    if (images) {
      // 배열 안의 객체에서 filePath만 할당
      setPreviewImages(images.map((image: any) => image.filePath));
      // 배열모든 것을 Detail에 할당
      setDetailImages(images);
    }
  }, []);

  useEffect(() => {
    if (isError) {
      toast.error("다시 수정하여 보내주세요", {
        autoClose: 1500,
        position: toast.POSITION.TOP_RIGHT,
      });
    }
    if (isSuccess) {
      setToggle(prev => !prev);
    }
  }, [isError, isSuccess]);

  const onCickImageUpload = () => {
    imageInput.current.click();
  };

  /** previewImage만들어주는 url */
  const insertImg = (e: any) => {
    let reader = new FileReader();
    setDetailImages(prev => prev.concat(e.target.files));

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
  /** 이미지 삭제 기능
   * how: 서버에서 { id, url }을 준다.
   * 만약 바뀐 것이 없다면 careerImages[]에 넣어줌
   * 만약 바뀐 것이 있다면 변경 된 것의 id를 기재하여 넘기기.
   * 추가한 이미지는 careerImage로 넘기기
   */
  const deleteImg = (e: any) => {
    const targetId = Number(e.target.id);

    // /** 미리보기 삭제 */
    const deletedPreview = previewImages.filter(
      (image: any, idx) => idx !== targetId,
    );
    setPreviewImages(deletedPreview);

    /** 이미지 배열 삭제 */
    const deletedDetail = detailImages.filter((image: any, idx) => {
      return idx !== targetId;
    });
    setDetailImages(deletedDetail);

    // 삭제된 이미지의 ID값 가져오기
    const deletedId = detailImages
      .filter((image, idx) => {
        return idx === targetId;
      })
      .map(el => el.careerImageId)
      .filter(image => image);
    setDeletedArr(prev => prev.concat(deletedId));
  };

  const testSubmit = (e: any) => {
    const formData = new FormData();

    const detailTxt = {
      introduction: e.introduction,
      detailCompany: e.detailCompany,
      detailCost: e.detailCost,
      personality: e.personality,
      detailLocation: e.detailLocation,
      careerImages: deletedArr,
    };
    const json = JSON.stringify(detailTxt);
    const blob = new Blob([json], { type: "application/json" });
    formData.append("data", blob);

    const addImgs = detailImages
      .filter(image => typeof image[0] === "object")
      .map(image => image[0]);
    if (addImgs.length > 0) {
      addImgs.map(async img => {
        const compressImg = await compressImage(img);
        const compressImgFile = new File([compressImg!], compressImg?.name!);
        formData.append("careerImage", compressImgFile);
      });
    }
    const assemble = {
      object: formData,
      id: lessonId,
    };
    toast.info("이미지 최적화 중입니다...", {
      autoClose: 4000,
      position: toast.POSITION.TOP_RIGHT,
    });
    setTimeout(() => {
      mutate(assemble);
      modalOpen();
    }, 4500);
  };

  return (
    <ModalBackDrop onClick={modalOpen}>
      <CreateModalContainer>
        <div className="w-full h-fit font-SCDream5 text-xs text-center text-pointColor mt-2 tablet:text-sm">
          수정할 정보를 입력하세요
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
              defaultValue={introduction}
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
              defaultValue={detailCompany}
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
              defaultValue={detailLocation}
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
              defaultValue={personality}
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
              defaultValue={detailCost}
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
                  previewImages.map((el: any, idx: any) => (
                    <div key={idx} className="relative w-1/4 pr-1">
                      <img
                        className="aspect-square rounded-xl relative"
                        src={el}
                      />
                      <span
                        id={idx}
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
            {detailImages.length < 3 && (
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
            <button className="font-SCDream4 w-1/4 py-2 bg-pointColor rounded-md text-white">
              수정하기
            </button>
          </div>
        </form>
      </CreateModalContainer>
    </ModalBackDrop>
  );
};

export default DetailExtraModi;
