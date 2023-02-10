import axios from "axios";

const deleteApply = async (suggestId: number) => {
  return await axios.delete(`/suggest/${suggestId}`, {
    baseURL: process.env.NEXT_PUBLIC_API_URL,
    headers: {
      "content-Type": `application/json`,
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
};
export default deleteApply;
