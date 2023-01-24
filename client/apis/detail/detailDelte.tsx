import axios from "axios";

const detailDelete = async (lessonId: number) => {
  return await axios.delete(`/lesson/${lessonId}`, {
    baseURL: process.env.NEXT_PUBLIC_API_URL,
    headers: {
      "content-Type": `application/json`,
      "ngrok-skip-browser-warning": "69420",
    },
  });
};
export default detailDelete;
