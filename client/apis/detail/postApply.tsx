import axios from "axios";

interface Application {
  days: string;
  languages: string;
  requests: string;
  id: number;
}

const postApply = async ({ days, languages, requests, id }: Application) => {
  const body = {
    days: days,
    languages: languages,
    requests: requests,
  };

  return await axios.post(`/suggest/lesson/${id}`, body, {
    baseURL: process.env.NEXT_PUBLIC_API_URL,
    headers: {
      "content-Type": `application/json`,
      "ngrok-skip-browser-warning": "69420",
      Authorization: `Bearer ${localStorage.getItem("token")}`,
      // RefreshToken: localStorage.getItem("refresh_token"),
    },
  });
};
export default postApply;
