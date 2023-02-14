import instance from "apis/module";
interface Props {
  reviewIdNum: number;
  score: number;
  comment: string;
}

const patchReview = ({ reviewIdNum, score, comment }: Props) => {
  const body = {
    score,
    comment,
  };

  return instance.patch(`/review/${reviewIdNum}/modification`, body, {
    headers: {
      "content-Type": `application/json`,
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
};

export default patchReview;
