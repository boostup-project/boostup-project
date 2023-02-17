import axios from "axios";

interface Props {
  email: string;
  password: string;
}

const postLogin = ({ email, password }: Props) => {
  const body = {
    email: email,
    password: password,
  };

  return axios.post("/member/login", body, {
    baseURL: process.env.NEXT_PUBLIC_API_URL,
    headers: {
      "content-Type": `application/json`,
      "ngrok-skip-browser-warning": "69420",
    },
  });
};

export default postLogin;
