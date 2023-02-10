import axios from "axios";

const getStudentInfo = async () => {
  const url = `/suggest/student`;
  return await axios.get(url, {
    baseURL: process.env.NEXT_PUBLIC_API_URL,
    headers: {
      "content-Type": `application/json`,

      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
};

export default getStudentInfo;
