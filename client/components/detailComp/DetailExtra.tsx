import { AxiosResponse } from "axios";
import { useState } from "react";
import { useEffect } from "react";
import DetailExtraModi from "./DetailExtraModi";
import FadeLoader from "react-spinners/FadeLoader";
import { loaderBlue } from "assets/color/color";
import DetailImageModal from "./DetailImageModal";

export interface CareerImage {
  [index: string]: string;
  careerImageId: string;
  filePath: string;
}

export interface DetailExtraInfo {
  [index: string]: string | Array<CareerImage>;
  introduction: string;
  detailCompany: string;
  personality: string;
  detailCost: string;
  detailLocation: string;
  careerImage: Array<CareerImage>;
}

export interface DetailTitles {
  [index: string]: string;
}

interface Props {
  extraData: AxiosResponse<any, any> | undefined;
  lessonId: number;
  editable: boolean | undefined;
}

const detailTitles: DetailTitles = {
  introduction: "한줄소개",
  detailCompany: "재직 회사 / 학력",
  personality: "성격",
  detailCost: "수업료",
  detailLocation: "장소 세부사항",
  careerImage: "참고사진",
};

const detailTitlesArray = Object.keys(detailTitles);

const DetailExtra = ({ extraData, lessonId, editable }: Props) => {
  const [textData, setTextData] = useState<DetailTitles>();
  const [images, setImages] = useState<CareerImage[]>([]);
  const [imagesToShow, setImagesToShow] = useState<string>("");
  const [isImageModal, setIsImageModal] = useState(false);
  const [isEdit, setIsEdit] = useState(false);
  console.log(imagesToShow);

  useEffect(() => {
    if (extraData) {
      const prompt = {
        introduction: extraData.data.introduction,
        detailCompany: extraData.data.detailCompany,
        personality: extraData.data.personality,
        detailCost:
          parseInt(extraData.data.detailCost.split("원")).toLocaleString(
            "ko-KR",
          ) + "원/회",
        detailLocation: extraData.data.detailLocation,
      };
      setTextData(prompt);
      setImages(extraData.data.careerImages);
    }
  }, [extraData]);
  const modalOpen = () => {
    setIsEdit(prev => !prev);
  };
  const modalImageOpen = (e: any) => {
    setImagesToShow(images[e.target.id].filePath);
    setIsImageModal(prev => !prev);
  };

  const modalImageClose = () => {
    setIsImageModal(prev => !prev);
  };

  return (
    <div className="w-full h-full">
      {isEdit && (
        <DetailExtraModi
          modalOpen={modalOpen}
          textData={textData!}
          images={images}
          lessonId={lessonId}
        />
      )}
      {editable && (
        <div className="h-full flex justify-end pt-6 px-6">
          <span
            className={`text-pointColor font-SCDream3 cursor-pointer hover:underline`}
            onClick={modalOpen}
          >
            edit
          </span>
        </div>
      )}
      <div className="w-full h-full pb-6 pt-3 px-6 text-base">
        {!textData ? (
          <div className="w-full h-full flex justify-center items-center">
            <FadeLoader
              color={loaderBlue}
              height={15}
              width={5}
              radius={2}
              margin={2}
            />
          </div>
        ) : (
          detailTitlesArray.map((title, i) => (
            <div key={i} className="mb-7">
              <div className="font-SCDream5">{detailTitles[title]}</div>
              <div className="font-SCDream3">
                {title === "careerImage" ? (
                  <div className="border border-borderColor w-72 h-[100px] flex">
                    {images?.map((image: any, idx: number) => (
                      <img
                        className="w-1/3 p-1 rounded-xl aspect-square cursor-pointer"
                        key={image.careerImageId}
                        id={String(idx)}
                        src={image.filePath}
                        alt="detailImage"
                        onClick={e => modalImageOpen(e)}
                      />
                    ))}
                  </div>
                ) : (
                  <div>• {textData[title]}</div>
                )}
              </div>
            </div>
          ))
        )}
      </div>
      {isImageModal && (
        <DetailImageModal
          modalImageClose={modalImageClose}
          imagesToShow={imagesToShow}
        />
      )}
    </div>
  );
};
export default DetailExtra;
