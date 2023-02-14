import instance from "apis/module";

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
  return await instance.post(
    `/review/lesson/${lessonId}/suggest/${suggestId}
  `,
    body,
    {
      headers: {
        "content-Type": `application/json`,
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
    },
  );
};
export default postReview;
