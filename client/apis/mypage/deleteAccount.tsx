import axios from "axios";

const deleteAccount = async () => {
  return await axios.delete(`/member`, {
    baseURL: process.env.NEXT_PUBLIC_API_URL,
    headers: {
      "content-Type": `application/json`,
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
};
export default deleteAccount;
