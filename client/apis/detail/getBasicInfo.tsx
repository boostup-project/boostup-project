import axios from "axios";

const getBasicInfo = (lessonId: number) => {
  return axios.get(`/lesson/${lessonId}`, {
    baseURL: process.env.NEXT_PUBLIC_API_URL,
    headers: {
      "content-Type": `application/json`,
      "ngrok-skip-browser-warning": "69420",
    },
  });
};

export default getBasicInfo;
