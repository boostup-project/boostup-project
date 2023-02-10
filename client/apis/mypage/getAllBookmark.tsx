import axios from "axios";

const getAllBookmark = async () => {
  return await axios.get(`/bookmark`, {
    baseURL: process.env.NEXT_PUBLIC_API_URL,
    headers: {
      "content-Type": `application/json`,
      Authorization: `Bearer ${localStorage.getItem("token")}`,
      // RefreshToken: localStorage.getItem("refresh_token"),
    },
  });
};
export default getAllBookmark;
