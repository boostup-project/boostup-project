import axios from "axios";

const getGoogleLogin = async () => {
  return await axios.get(`/oauth2/authorization/google`, {
    baseURL: process.env.NEXT_PUBLIC_API_URL,
    headers: {
      "content-Type": `application/json`,
    },
  });
};
//oauth2/authorization/google
//https://codueon.shop/login/oauth2/code/google
export default getGoogleLogin;
