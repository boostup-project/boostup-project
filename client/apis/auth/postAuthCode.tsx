import axios from "axios";

interface Props {
  email: string;
  code: string;
}

const postAuthCode = ({ email, code }: Props) => {
  const body = {
    email: email,
    emailCode: code,
  };

  return axios.post("/email/code/validation", body, {
    baseURL: process.env.NEXT_PUBLIC_API_URL,
    headers: {
      "content-Type": `application/json`,
      "ngrok-skip-browser-warning": "69420",
    },
  });
};

export default postAuthCode;
