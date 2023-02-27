import axios from "axios";

const getFilteredBoard = async (languageId: number) => {
  return await axios.get(`/lesson/language/${languageId}`, {
    baseURL: process.env.NEXT_PUBLIC_API_URL,
    headers: {
      "content-Type": `application/json`,
      // "Access-Control-Allow-Origin": "*",
      // "Access-Control-Allow-Methods": "*",
      "ngrok-skip-browser-warning": "69420",
    },
  });
};

export default getFilteredBoard;
