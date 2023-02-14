import axios from "axios";

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

  return axios.patch(`/review/${reviewIdNum}/modification`, body, {
    baseURL: process.env.NEXT_PUBLIC_API_URL,
    headers: {
      "content-Type": `application/json`,

      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
};

export default patchReview;
