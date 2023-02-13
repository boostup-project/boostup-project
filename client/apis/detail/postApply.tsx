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
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
};
export default postApply;
