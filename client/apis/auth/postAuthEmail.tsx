import axios from "axios";

interface Props {
  email: string;
}

const postAuthEmail = async ({ email }: Props) => {
  const data = {
    email: email,
  };

  return await axios.post(`/email/code/transmission`, data, {
    baseURL: process.env.NEXT_PUBLIC_API_URL,
    headers: {
      "content-Type": `application/json`,
      "ngrok-skip-browser-warning": "69420",
    },
  });
};

export default postAuthEmail;
