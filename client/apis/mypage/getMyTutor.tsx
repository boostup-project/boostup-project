import axios from "axios";

const getMyTutor = async () => {
  const url = `/lesson/tutor`;
  return await axios.get(url, {
    baseURL: process.env.NEXT_PUBLIC_API_URL,
    headers: {
      "content-Type": `application/json`,

      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
};

export default getMyTutor;
