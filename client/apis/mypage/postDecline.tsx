import axios from "axios";

interface Decline {
  reason: string;
  suggestId: number;
}

const postDecline = async ({ reason, suggestId }: Decline) => {
  const body = {
    reason: reason,
  };
  return await axios.post(
    `/suggest/${suggestId}/decline
  `,
    body,
    {
      baseURL: process.env.NEXT_PUBLIC_API_URL,
      headers: {
        "content-Type": `application/json`,

        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
    },
  );
};
export default postDecline;
