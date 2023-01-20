import axios from "axios";

interface Props {
  email: string;
  pw: string;
}

const postResetPw = ({ email, pw }: Props) => {
  const body = {
    email: email,
    changePassword: pw,
  };

  return axios.post("/member/password/resetting", body, {
    baseURL: process.env.NEXT_PUBLIC_API_URL,
    headers: {
      "content-Type": `application/json`,
      "ngrok-skip-browser-warning": "69420",
    },
  });
};

export default postResetPw;
