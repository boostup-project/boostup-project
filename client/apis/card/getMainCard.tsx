import axios from "axios";

const getMainCard = async () => {
  const author = localStorage.getItem("token");

  if (author) {
    return await axios.get(`/lesson`, {
      baseURL: process.env.NEXT_PUBLIC_API_URL,
      headers: {
        "content-Type": `application/json`,
        "ngrok-skip-browser-warning": "69420",
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
    });
  } else {
    return await axios.get(`/lesson`, {
      baseURL: process.env.NEXT_PUBLIC_API_URL,
      headers: {
        "content-Type": `application/json`,
        "ngrok-skip-browser-warning": "69420",
      },
    });
  }
};
export default getMainCard;
