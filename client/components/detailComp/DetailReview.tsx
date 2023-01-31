import Image from "next/image";

const DetailReview = (reviewData: any) => {
  return (
    <>
      <div className="w-full min-h-[500px] h-fit flex flex-col justify-center items-center">
        {reviewData?.data?.length ? (
          <div className="w-full min-h-[500px] h-fit flex flex-col justify-start items-center mt-5 p-3">
            <div className="w-full h-fit flex flex-col justify-center items-center pb-5">
              <div className="w-full h-fit font-SCDream5 text-md text-textColor flex flex-col justify-center items-center">
                후기 총 평점
              </div>
              <div className="w-full h-fit flex flex-row justify-center items-center">
                <div className="w-fit h-fit font-SCDream5 text-md text-pointColor flex flex-col justify-center items-center mr-1">
                  ★
                </div>
                <div className="w-fit h-fit font-SCDream5 text-md text-textColor flex flex-col justify-center items-center">
                  {reviewData.reviewData?.data.average}(
                  {reviewData.reviewData?.data.totalReviews})
                </div>
              </div>
            </div>

            {/* map */}
            <div className="w-full h-fit flex flex-col justify-center items-center border-t border-borderColor py-3">
              <div className="w-full h-fit flex flex-row justify-start items-center">
                <div className="w-1/8 h-full flex flex-col justify-center items-center p-3">
                  <Image
                    src={"https://pbs.twimg.com/media/FgYA_RAWQAEWCw3.jpg"}
                    alt="image"
                    width={50}
                    height={50}
                  />
                </div>
                <div className="flex flex-col w-64 h-fit justify-center items-start">
                  <div className="flex flex-row w-fit h-full justify-start items-center">
                    <div className="w-fit h-fit font-SCDream5 text-sm text-textColor">
                      만족도
                    </div>
                    <div className="w-fit h-fit font-SCDream5 text-sm text-pointColor ml-3">
                      {/* "★ ".repeat(reviewData.reviewData?.data.data.score) */}
                      {"★ ".repeat(3)}
                    </div>
                  </div>
                  <div className="w-fit h-fit font-SCDream3 text-sm text-textColor mt-2">
                    {/* reviewData.reviewData?.data.data.name */}
                    서울 도봉구 학부모
                  </div>
                </div>
              </div>
              <div className="w-full h-fit flex flex-col justify-center items-start text-sm font-SCDream3 text-textColor p-3">
                {/* reviewData.reviewData?.data.data.comment */}
                개인별 실력을 매우 잘 체크하셔서 제 수준을 바로 파악하셨고
                부족한 점을 바로 보완하는 방식을 진행되었습니다. 특히 정해진
                시간 외에도 연락을 하면 꾸준히 의견과 해결 힌트를 주시면서
                수강생 스스로 발전할 수 있게 도움주셨습니다. 해결하는 과정이
                매우 오래 걸리더라도 스스로 해결한 결과 가면 갈 수록 탄력을
                얻었고 과외썜도 이를 인지하고 난이도 조절 및 문제 수도
                늘려가면서 도움을 주셨습니다. 정말 후회없는 과외이니 알고리즘이
                부족하신 분들은 꼭 신청하시는 것을 추천드립니다!
              </div>
              <div className="w-full h-fit flex flex-col justify-center items-end text-sm font-SCDream3 text-textColor p-3">
                {/* reviewData.reviewData?.data.data.createdAt */}
                2022.12.22
              </div>
            </div>
          </div>
        ) : (
          <div className="font-SCDream3 text-md w-full min-h-[500px] h-fit flex flex-col justify-center items-center text-textColor">
            후기가 아직 없어요😥
          </div>
        )}
      </div>
    </>
  );
};

export default DetailReview;
