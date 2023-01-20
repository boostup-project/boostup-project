import axios from "axios";

interface Props {
  email: string;
}

const postCheckEmail = ({ email }: Props) => {
  const body = {
    email: email,
  };

  return axios.post("/member/email/check", body, {
    baseURL: process.env.NEXT_PUBLIC_API_URL,
    headers: {
      "content-Type": `application/json`,
      "ngrok-skip-browser-warning": "69420",
    },
  });
};

export default postCheckEmail;
