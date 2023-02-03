import axios from "axios";

const postMemberModi = async (object: FormData) => {
  const url = "/member/modification";
  return await axios.post(url, object, {
    baseURL: process.env.NEXT_PUBLIC_API_URL,
    headers: {
      "content-Type": `multipart/form-data`,
      "ngrok-skip-browser-warning": "69420",
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
};

export default postMemberModi;
