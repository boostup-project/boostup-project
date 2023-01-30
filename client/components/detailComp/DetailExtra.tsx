import { AxiosResponse } from "axios";
import { useState } from "react";
import { useEffect } from "react";
import DetailExtraModi from "./DetailExtraModi";

interface CareerImage {
  [index: string]: string;
  careerImageId: string;
  careerImageUrl: string;
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

const DetailExtra = ({ extraData, lessonId }: Props) => {
  const [textData, setTextData] = useState<DetailTitles>();
  const [images, setImages] = useState<string[]>([]);
  const [isEdit, setIsEdit] = useState(false);
  // console.log("extraData", extraData!.data);
  useEffect(() => {
    if (extraData) {
      const prompt = {
        introduction: extraData.data.introduction,
        detailCompany: extraData.data.detailCompany,
        personality: extraData.data.personality,
        detailCost: extraData.data.detailCost,
        detailLocation: extraData.data.detailLocation,
      };
      setTextData(prompt);
      const careerImages = extraData.data.careerImage;
      if (careerImages) {
        const extractedImage = careerImages.map((image: CareerImage) => {
          image.careerImageUrl;
        });
        setImages(extractedImage);
      }
      setImages(extraData.data.careerImage);
    }
  }, [extraData]);
  const modalOpen = () => {
    setIsEdit(prev => !prev);
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
      <div className="h-full flex justify-end pt-6 px-6">
        <span
          className="text-pointColor font-SCDream3 cursor-pointer hover:underline"
          onClick={modalOpen}
        >
          edit
        </span>
      </div>
      <div className="w-full h-full pb-6 pt-3 px-6 text-base">
        {!textData ? (
          <div>Loading</div>
        ) : (
          detailTitlesArray.map((title, i) => (
            <div key={i} className="mb-7">
              <div className="font-SCDream5">{detailTitles[title]}</div>
              <div className="font-SCDream3">
                {title === "careerImage" ? (
                  <></>
                ) : (
                  <div>• {textData[title]}</div>
                )}
              </div>
            </div>
          ))
        )}
      </div>
    </div>
  );
};
export default DetailExtra;
