interface CareerImage {
  [index: string]: string;
  careerImageId: string;
  careerImageUrl: string;
}

interface DetailExtraInfo {
  [index: string]: string | Array<CareerImage>;
  introduction: string;
  detailCompany: string;
  personality: string;
  detailCost: string;
  detailLocation: string;
  careerImage: Array<CareerImage>;
}

interface DetailTitles {
  [index: string]: string;
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

const dummy: DetailExtraInfo = {
  introduction: "테스트",
  detailCompany: "회사 테스트",
  personality: "성격 테스트",
  detailCost: "금액 테스트",
  detailLocation: "위치 테스트",
  careerImage: [
    {
      careerImageId: "1",
      careerImageUrl: "https://source.unsplash.com/random",
    },
    {
      careerImageId: "2",
      careerImageUrl: "https://source.unsplash.com/random",
    },
    {
      careerImageId: "3",
      careerImageUrl: "https://source.unsplash.com/random",
    },
  ],
};

const {
  introduction,
  detailCompany,
  personality,
  detailCost,
  detailLocation,
  careerImage,
} = dummy;

const textData: DetailTitles = {
  introduction,
  detailCompany,
  personality,
  detailCost,
  detailLocation,
};

const DetailExtra = () => {
  return (
    <div className="w-full h-full">
      <div className="p-6 text-base">
        {detailTitlesArray.map((title, i) => (
          <div className="mb-5">
            <div key={i} className="font-SCDream5">
              {detailTitles[title]}
            </div>
            <div className="font-SCDrea3">
              {title !== "careerImage" ? (
                <div>{textData[title]}</div>
              ) : (
                <div>hi</div>
              )}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};
export default DetailExtra;
