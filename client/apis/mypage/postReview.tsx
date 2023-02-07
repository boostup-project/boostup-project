import axios from "axios";

interface Review {
  comment: string;
  score: number;
  lessonId: number;
  suggestId: number;
}

const postReview = async ({ comment, score, lessonId, suggestId }: Review) => {
  const body = {
    comment: comment,
    score: score,
  };
  return await axios.post(
    `/review/lesson/${lessonId}/suggest/${suggestId}
  `,
    body,
    {
      baseURL: process.env.NEXT_PUBLIC_API_URL,
      headers: {
        "content-Type": `application/json`,
        "ngrok-skip-browser-warning": "69420",
        Authorization: `Bearer ${localStorage.getItem("token")}`,
        // RefreshToken: localStorage.getItem("refresh_token"),
      },
    },
  );
};
export default postReview;
