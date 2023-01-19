import axios from "axios";

const getFilteredBoard = async (languageId: number) => {
  return await axios.get(`/lesson/language/${languageId}`, {
    baseURL: process.env.NEXT_PUBLIC_API_URL,
  });
};

export default getFilteredBoard;
