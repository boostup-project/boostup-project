import axios from "axios";

const getMyReview = async () => {
  const url = `/review`;
  return await axios.get(url, {
    baseURL: process.env.NEXT_PUBLIC_API_URL,
    headers: {
      "content-Type": `application/json`,
      "ngrok-skip-browser-warning": "63490",
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
};

export default getMyReview;
