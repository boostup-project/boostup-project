import CreateModalContainer from "../reuse/container/CreateModalContainer";
import { useForm } from "react-hook-form";
import SmallBtn from "../reuse/btn/SmallBtn";
import { useRecoilState } from "recoil";
import { powerEditReviewModal, reviewCommentState } from "atoms/main/mainAtom";
import { reviewIdState } from "atoms/mypage/myPageAtom";
import usePatchReview from "hooks/mypage/usePatchReview";

interface Review {
  comment: string;
  score: number;
}

const ReviewEditModal = () => {
  const [powerModal, setPowerModal] = useRecoilState(powerEditReviewModal);
  const [reviewState, setReviewState] = useRecoilState(reviewCommentState);
  const [reviewIdNum, setReviewIdNum] = useRecoilState(reviewIdState);
  const { mutate } = usePatchReview();

  const {
    control,
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<Review>({ mode: "onBlur" });

  const handleClickBackDrop = () => {
    setPowerModal(false);
  };

  const handleClickCancel = () => {
    setPowerModal(false);
  };

  const onSubmit = (data: any) => {
    let score = Number(data.score);
    let comment = data.comment;
    mutate({ reviewIdNum, score, comment });
  };

  return (
    <>
      <div
        className="fixed z-10 top-0 left-0 bottom-0 right-0 bg-modalBgColor grid place-items-center"
        onClick={handleClickBackDrop}
      >
        <CreateModalContainer>
          <form
            className="flex flex-col items-center w-full text-sm z-20"
            onSubmit={handleSubmit(onSubmit)}
          >
            <div className="flex w-11/12 desktop:w-4/6 h-fit font-SCDream7 text-lg text-textColor mt-4">
              리뷰작성
            </div>
            <div className="flex w-11/12 desktop:w-4/6 h-fit font-SCDream5 text-base text-textColor mt-2 mb-2">
              과외의 평점을 입력해주세요
            </div>
            <div className="mx-5 flex flex-row-reverse justify-end text-2xl">
              <label htmlFor="score"></label>
              <input
                type="radio"
                className="peer hidden"
                id="value5"
                value="5"
                {...register("score", { required: "true" })}
              />
              <label
                htmlFor="value5"
                className="cursor-pointer text-gray-400 peer-hover:text-yellow-500 peer-checked:text-yellow-300"
              >
                ★
              </label>
              <input
                type="radio"
                className="peer hidden"
                id="value4"
                value="4"
                {...register("score", { required: "true" })}
              />
              <label
                htmlFor="value4"
                className="cursor-pointer text-gray-400 peer-hover:text-yellow-500 peer-checked:text-yellow-300"
              >
                ★
              </label>
              <input
                type="radio"
                className="peer hidden"
                id="value3"
                value="3"
                {...register("score", { required: "true" })}
              />
              <label
                htmlFor="value3"
                className="cursor-pointer text-gray-400 peer-hover:text-yellow-500 peer-checked:text-yellow-300"
              >
                ★
              </label>
              <input
                type="radio"
                className="peer hidden"
                id="value2"
                value="2"
                {...register("score", { required: "true" })}
              />
              <label
                htmlFor="value2"
                className="cursor-pointer text-gray-400 peer-hover:text-yellow-500 peer-checked:text-yellow-300"
              >
                ★
              </label>
              <input
                type="radio"
                className="peer hidden"
                id="value1"
                value="1"
                {...register("score", { required: "true" })}
              />
              <label
                htmlFor="value1"
                className="cursor-pointer peer text-gray-400 peer-hover:text-yellow-500 peer-checked:text-yellow-300"
              >
                ★
              </label>
            </div>
            <p className="w-11/12 text-xs text-negativeMessage ml-2 mt-1 tablet:text-sm desktop:w-4/6">
              {errors?.score && <span>필수 정보입니다</span>}
            </p>

            <div className="flex w-11/12 desktop:w-4/6 h-fit font-SCDream5 text-base text-textColor mt-2 mb-2">
              리뷰 내용을 입력해주세요
            </div>
            <input
              type="text"
              placeholder="리뷰 내용을 입력하세요"
              defaultValue={reviewState}
              className="w-11/12 desktop:w-4/6 h-fit p-2 border border-borderColor outline-pointColor rounded-xl font-SCDream4 text-xs text-textColor tablet:text-sm "
              {...register("comment", { required: "필수 정보입니다." })}
            />
            <p className="w-11/12 text-xs text-negativeMessage ml-2 mt-1 tablet:text-sm desktop:w-4/6">
              {errors?.comment && <span>필수 정보입니다</span>}
            </p>
            <div className="flex flex-row justify-center items-center w-full h-fit mt-10">
              <SmallBtn css="mr-4" onClick={handleClickCancel}>
                취 소
              </SmallBtn>
              <SmallBtn>확 인</SmallBtn>
            </div>
          </form>
        </CreateModalContainer>
      </div>
    </>
  );
};

export default ReviewEditModal;
