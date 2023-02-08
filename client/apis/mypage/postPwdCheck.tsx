import axios from "axios";

const postPwdCheck = async (password: string) => {
  const url = "/member/password/check";
  return await axios.post(url, password, {
    baseURL: process.env.NEXT_PUBLIC_API_URL,
    headers: {
      "content-Type": `application/json`,
      "ngrok-skip-browser-warning": "69420",
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
};

export default postPwdCheck;
