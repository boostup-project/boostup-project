import axios from "axios";

const getCurriculum = async (lessonId: number) => {
  const url = `/lesson/${lessonId}/curriculum`;
  return await axios.get(url, {
    baseURL: process.env.NEXT_PUBLIC_API_URL,
    headers: {
      "content-Type": `application/json`,
      "ngrok-skip-browser-warning": "63490",
    },
  });
};

export default getCurriculum;
