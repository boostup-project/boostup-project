import instance from "apis/module";

const deleteReview = async (reviewId: number) => {
  return await instance.delete(`/review/${reviewId}`, {
    headers: {
      "content-Type": `application/json`,
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
};
export default deleteReview;
