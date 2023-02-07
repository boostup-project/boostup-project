import {
  IconRibbon,
  IconWon,
  IconPaper,
  IconPlace,
  IconEmptyheart,
  IconFullheart,
} from "assets/icon";
import useGetMyReview from "hooks/mypage/useGetMyReview";
const StudentReview = () => {
  const { data: MyReview } = useGetMyReview();
  console.log(MyReview?.data.data);
  return (
    <>
      <div className="mt-6 flex flex-col w-full">
        <div className="w-full">
          {MyReview?.data.data.map((review: any) => (
            <div className="flex flex-row h-fit w-full rounded-lg border border-borderColor mt-3">
              {/* {left} */}
              <div className="flex flex-col w-2/3  justify-center desktop:pl-2 m-3">
                <div className="flex">
                  {review.languages?.map((el: any, idx: any) => {
                    return (
                      <div
                        key={idx}
                        className={`flex justify-center bg-${el} items-center font-SCDream5 text-white px-1 py-0.5 ml-1 mt-1 border rounded-xl desktop:text-xs tablet:text-[10px] text-[6px]`}
                      >
                        {el}
                      </div>
                    );
                  })}
                </div>
                <div className="flex justify-start items-start w-full h-fit font-SCDream5 desktop:text-xs tablet:text-[12px] text-[8px] text-textColor ml-2  my-1">
                  {review.name}
                </div>
                <div className="flex justify-start items-start w-full h-fit font-SCDream6 desktop:text-lg tablet:text-base text-xs text-textColor ml-1 mb-2 flex-wrap">
                  {review.title}
                </div>
                <div className="flex desktop:text-base tablet:text-sm text-[12px]">
                  만족도 {"★ ".repeat(5)}
                </div>
                <div className="flex desktop:text-base tablet:text-sm text-[12px] desktop:mt-3 tablet:mt-2">
                  {review.comment}
                </div>
              </div>
              {/* right */}
              <div className="flex flex-col w-1/2 justify-center items-end desktop:mr-4 tablet:mr-2 mr-2">
                <div className="flex  desktop:text-lg tablet:text-base text-[14px]">
                  {review.startTime?.slice(0, 10)}
                </div>
                <div className="flex  desktop:text-lg tablet:text-base text-[14px]">
                  ~{review.endTime?.slice(0, 10)}
                </div>
                <div className="flex text-borderColor desktop:text-lg tablet:text-base text-[14px]">
                  {review.createdAt}
                </div>
                <div className="flex mt-5">
                  <button className="text text-pointColor m-2 desktop:text-base tablet:text-sm text-[10px]">
                    수정하기
                  </button>
                  <button className="text text-negativeMessage m-2 desktop:text-base tablet:text-sm text-[10px]">
                    삭제하기
                  </button>
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>
    </>
  );
};
export default StudentReview;
