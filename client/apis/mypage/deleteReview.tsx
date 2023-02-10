import axios from "axios";

const deleteReview = async (reviewId: number) => {
  return await axios.delete(`/review/${reviewId}`, {
    baseURL: process.env.NEXT_PUBLIC_API_URL,
    headers: {
      "content-Type": `application/json`,
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
};
export default deleteReview;
