import axios from "axios";

const postPwdChange = async (changePassword: string) => {
  const url = "/member/password/resetting/my-page";
  return await axios.post(url, changePassword, {
    baseURL: process.env.NEXT_PUBLIC_API_URL,
    headers: {
      "content-Type": `application/json`,
      "ngrok-skip-browser-warning": "69420",
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
};

export default postPwdChange;
