import {
  IconRibbon,
  IconWon,
  IconPaper,
  IconPlace,
  IconEmptyheart,
  IconFullheart,
} from "assets/icon";
import useGetMyReview from "hooks/mypage/useGetMyReview";
import useDeleteReview from "hooks/mypage/useDeleteReview";
import Swal from "sweetalert2";
import ReviewEditModal from "./ReviewEditModal";
import { useRecoilState } from "recoil";
import { powerEditReviewModal, reviewCommentState } from "atoms/main/mainAtom";

const StudentReview = () => {
  const { data: MyReview } = useGetMyReview();
  const { mutate: deleteMyReview } = useDeleteReview();

  const [powerModal, setPowerModal] = useRecoilState(powerEditReviewModal);
  const [commentState, setCommentState] = useRecoilState(reviewCommentState);

  console.log(MyReview);
  const deleteReview = () => {
    console.log(MyReview);
    Swal.fire({
      title: "과외 후기를 삭제하시겠습니까?",
      icon: "question",
      showCancelButton: true,
      confirmButtonColor: "#3085d6",
    }).then(result => {
      if (result.isConfirmed) {
        // deleteMyReview(suggestId);
        return Swal.fire({
          text: "삭제가 완료되었습니다",
          icon: "success",
          confirmButtonColor: "#3085d6",
        });
      }
    });
  };

  const handleClickEdit = (comment: string) => {
    setPowerModal(true);
    setCommentState(comment);
  };

  return (
    <>
      {powerModal ? <ReviewEditModal /> : null}
      <div className="flex flex-col w-full min-h-[300px] bg-bgColor">
        <div className="w-full">
          {MyReview === undefined || MyReview?.data.data.length === 0 ? (
            <div className="flex flex-col justify-center items-center w-full h-36 font-SCDream3 text-lg text-textColor mt-20">
              아직 작성한 후기가 없어요🙂
            </div>
          ) : null}
          {MyReview?.data.data.map((review: any) => (
            <div
              key={review.lessonId}
              className="flex flex-row h-fit w-full rounded-lg border border-borderColor mt-3"
            >
              {/* {left} */}
              <div className="flex flex-col w-3/4  justify-center desktop:pl-2 m-3">
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
                <div className="flex justify-start items-start w-full h-fit font-SCDream5 desktop:text-xs tablet:text-[12px] text-[8px] text-textColor px-1 py-0.5 ml-1 mt-1">
                  {review.name}
                </div>
                <div className="flex justify-start items-start w-full h-fit font-SCDream6 desktop:text-lg tablet:text-base text-xs text-textColor px-1 py-0.5 ml-1 mt-1 flex-wrap">
                  {review.title}
                </div>
                <div className="flex desktop:text-base tablet:text-sm text-[12px] font-SCDream3 px-1 py-0.5 ml-1 mt-1">
                  만족도 {"★ ".repeat(review.score)} {review.reviewId}
                </div>
                <div className="flex desktop:text-base tablet:text-sm text-[12px] desktop:mt-3 tablet:mt-2 font-SCDream3 px-1 py-0.5 ml-1 mt-1">
                  {review.comment}
                </div>
              </div>
              {/* right */}
              <div className="flex flex-col w-1/2 justify-center items-end desktop:mr-4 tablet:mr-2 mr-2">
                <div className="flex  desktop:text-md tablet:text-base text-[12px] font-SCDream3">
                  {review.startTime?.slice(0, 10)}
                </div>
                <div className="flex  desktop:text-md tablet:text-base text-[12px] font-SCDream3">
                  ~{review.endTime?.slice(0, 10)}
                </div>

                <div className="flex flex-col justify-center items-end mt-5">
                  <div className="flex text-borderColor desktop:text-sm tablet:text-xs text-[12px] font-SCDream3 m-1 mt-4">
                    {review.createdAt?.slice(0, 10)}
                  </div>
                  <div className="flex mt-1">
                    <button
                      className="text text-pointColor m-1 desktop:text-md tablet:text-sm text-[10px] font-SCDream3"
                      onClick={() => handleClickEdit(review.comment)}
                    >
                      수정하기
                    </button>
                    <button
                      className="text text-negativeMessage m-1 desktop:text-md tablet:text-sm text-[10px] font-SCDream3"
                      onClick={deleteReview}
                    >
                      삭제하기
                    </button>
                  </div>
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
