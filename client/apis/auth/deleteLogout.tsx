import axios from "axios";

export const deleteLogout = async () => {
  const url = "/member/logout";

  return await axios.delete(url, {
    baseURL: process.env.NEXT_PUBLIC_API_URL,
    headers: {
      "content-Type": `application/json`,
      Authorization: `Bearer ${localStorage.getItem("token")}`,
      RefreshToken: "delete",
    },
  });
};

export default deleteLogout;
