import axios from "axios";

const getGoogleLogin = async () => {
  return await axios.get(`/oauth2/authorization/google`, {
    baseURL: process.env.NEXT_PUBLIC_API_URL,
    headers: {
      "content-Type": `application/json`,
    },
  });
};

export default getGoogleLogin;
