import Image from "next/image";

const DetailReview = (reviewData: any) => {
  const changeToTime = (timeCode: Date) => {
    const createTime = new Date(timeCode);

    const utc =
      createTime.getTime() + createTime.getTimezoneOffset() * 60 * 1000;
    const KR_TIME_DIFF = 18 * 60 * 60 * 1000 + 22 * 1000;
    const kr_curr = new Date(utc + KR_TIME_DIFF);

    let [currYear, currMonth, currDay, currHour, currMinute, currSecond] = [
      kr_curr.getFullYear(),
      kr_curr.getMonth() + 1,
      kr_curr.getDate(),
      kr_curr.getHours(),
      kr_curr.getMinutes(),
      kr_curr.getSeconds(),
    ];

    return `${currYear}년 ${currMonth}월 ${currDay}일 ${currHour}시 ${currMinute}분`;
  };

  return (
    <>
      <div className="w-full min-h-[500px] h-fit flex flex-col justify-center items-center">
        {reviewData.reviewData?.data.length ? (
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
                  {reviewData.reviewData?.average}(
                  {reviewData.reviewData?.totalReviews})
                </div>
              </div>
            </div>

            {/* map */}
            {reviewData.reviewData.data.map((el: any, idx: number) => {
              return (
                <>
                  <div className="w-full h-fit flex flex-col justify-center items-center border-t border-borderColor py-3">
                    <div className="w-full h-fit flex flex-row justify-start items-center">
                      <div className="w-1/8 h-full flex flex-col justify-center items-center p-3">
                        <Image
                          src={el.image}
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
                            {"★ ".repeat(el.score)}
                          </div>
                        </div>
                        <div className="w-fit h-fit font-SCDream3 text-sm text-textColor mt-2">
                          {el.name}
                        </div>
                      </div>
                    </div>
                    <div className="w-full h-fit flex flex-col justify-center items-start text-sm font-SCDream3 text-textColor p-3">
                      {el.comment}
                    </div>
                    <div className="w-full h-fit flex flex-col justify-center items-end text-sm font-SCDream3 text-textColor p-3">
                      {changeToTime(el.createdAt)}
                    </div>
                  </div>
                </>
              );
            })}
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
