//과외 종료 삭제(학생용)
import axios from "axios";

const deleteFinishedClass = async (suggestId: number) => {
  return await axios.delete(`/suggest/${suggestId}/student`, {
    baseURL: process.env.NEXT_PUBLIC_API_URL,
    headers: {
      "content-Type": `application/json`,
      "ngrok-skip-browser-warning": "69420",
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
};
export default deleteFinishedClass;
