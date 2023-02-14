import axios from "axios";

const postWrite = async (object: FormData) => {
  const url = "/lesson/registration";

  return await axios.post(url, object, {
    baseURL: process.env.NEXT_PUBLIC_API_URL,
    headers: {
      "Content-Type": "multipart/form-data",
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
};

export default postWrite;
