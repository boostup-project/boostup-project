import axios from "axios";

const postNameCheck = async (name: string) => {
  const url = "/member/name/overlap/check";
  return await axios.post(url, name, {
    baseURL: process.env.NEXT_PUBLIC_API_URL,
    headers: {
      "content-Type": `application/json`,
      "ngrok-skip-browser-warning": "69420",
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
};

export default postNameCheck;
