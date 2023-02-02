import axios from "axios";

const getCloseClass = async (suggestId: number) => {
  const url = `/suggest/${suggestId}/done`;
  return await axios.get(url, {
    baseURL: process.env.NEXT_PUBLIC_API_URL,
    headers: {
      "content-Type": `application/json`,
      "ngrok-skip-browser-warning": "63490",
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
};

export default getCloseClass;
