import axios from "axios";

const getKakaoLogin = async () => {
  return await axios.get(`/oauth2/authorization/kakao`, {
    baseURL: process.env.NEXT_PUBLIC_API_URL,
    headers: {
      "content-Type": `application/json`,
    },
  });
};

export default getKakaoLogin;
