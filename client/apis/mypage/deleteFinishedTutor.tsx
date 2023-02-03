//과외 종료 삭제(강사용)
import axios from "axios";
const deleteFinishedTutor = async (suggestId: number) => {
  return await axios.delete(`/suggest/${suggestId}/tutor`, {
    baseURL: process.env.NEXT_PUBLIC_API_URL,
    headers: {
      "content-Type": `application/json`,
      "ngrok-skip-browser-warning": "69420",
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
};
export default deleteFinishedTutor;
