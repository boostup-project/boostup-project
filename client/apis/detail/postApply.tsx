import axios from "axios";
interface ObjectPart {
  [index: string]: string | string[];
}

const postApply = async (object: ObjectPart, lessonId: Number) => {
  return await axios.post(`/suggest/lesson/${lessonId}`, object, {
    baseURL: process.env.NEXT_PUBLIC_API_URL,
    headers: {
      "content-Type": `application/json`,
      "ngrok-skip-browser-warning": "69420",
      Authorization: `Bearer ${localStorage.getItem("token")}`,
      RefreshToken: localStorage.getItem("refresh_token"),
    },
  });
};
export default postApply;
