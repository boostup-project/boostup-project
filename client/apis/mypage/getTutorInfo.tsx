import axios from "axios";

const getTutorInfo = async (lessonId: Number, tabId: Number) => {
  const url = `/suggest/lesson/${lessonId}/tutor/tab/${tabId}`;
  return await axios.get(url, {
    baseURL: process.env.NEXT_PUBLIC_API_URL,
    headers: {
      "content-Type": `application/json`,
      "ngrok-skip-browser-warning": "63490",
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
};

export default getTutorInfo;
