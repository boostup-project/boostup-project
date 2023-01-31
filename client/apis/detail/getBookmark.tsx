import axios from "axios";

const getBookmark = async (lessonId: number) => {
  return await axios.get(`/bookmark/lesson/${lessonId}`, {
    baseURL: process.env.NEXT_PUBLIC_API_URL,
    headers: {
      "content-Type": `application/json`,
      "ngrok-skip-browser-warning": "69420",
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
};
export default getBookmark;
