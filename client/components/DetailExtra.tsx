import { AxiosResponse } from "axios";
import { useState } from "react";
import { useEffect } from "react";

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

export interface Props {
  extraData: AxiosResponse<any, any> | undefined;
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

const DetailExtra = ({ extraData }: Props) => {
  const [textData, setTextData] = useState<DetailTitles>();
  const [images, setImages] = useState([]);
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
      setImages(extraData.data.careerImage);
    }
  }, [extraData]);

  return (
    <div className="w-full h-full">
      <div className="p-6 text-base">
        {!textData ? (
          <div>Loading</div>
        ) : (
          detailTitlesArray.map((title, i) => (
            <div key={i} className="mb-7">
              <div className="font-SCDream5">{detailTitles[title]}</div>
              <div className="font-SCDrea3">
                {title === "careerImage" ? (
                  <div>사진</div>
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
