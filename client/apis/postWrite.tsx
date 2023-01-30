import axios from "axios";

const postWrite = async (object: FormData) => {
  const url = "/lesson/test/registration";

  return await axios.post(url, object, {
    baseURL: process.env.NEXT_PUBLIC_API_URL,
    headers: {
      "Content-Type": "multipart/form-data",
      "ngrok-skip-browser-warning": "63490",
      Authorization: `Bearer ${localStorage.getItem("token")}`,
      RefreshToken: localStorage.getItem("refresh_token"),
    },
  });
};

export default postWrite;
