import axios from "axios";

interface Props {
  email: string;
  password: string;
  name: string;
}

const postSignUp = ({ email, password, name }: Props) => {
  const body = {
    email: email,
    password: password,
    name: name,
  };
  return axios.post("/member/join", body, {
    baseURL: process.env.NEXT_PUBLIC_API_URL,
    headers: {
      "content-Type": `application/json`,
      "ngrok-skip-browser-warning": "69420",
    },
  });
};

export default postSignUp;
